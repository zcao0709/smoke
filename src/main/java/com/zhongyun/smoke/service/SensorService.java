package com.zhongyun.smoke.service;

import static com.zhongyun.smoke.common.Util.*;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.dao.mysql.OpTaskRepository;
import com.zhongyun.smoke.dao.mysql.ProjectRepository;
import com.zhongyun.smoke.dao.mysql.SensorRepository;
import com.zhongyun.smoke.model.OpTask;
import com.zhongyun.smoke.model.Project;
import com.zhongyun.smoke.model.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caozhennan on 2017/11/30.
 */
@Service
public class SensorService {
    @Autowired
    private ApplicationConfig config;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private OpTaskRepository opTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SmsService smsService;

    private static final Logger logger = LoggerFactory.getLogger("SensorService");

    public Sensor add(Sensor sensor) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        sensor.setEui16(String.format("%X", sensor.getEui()));
        sensor.setMtime(ts);
        sensor.setCtime(ts);
        return sensorRepository.save(sensor);
    }

    public void delete(long id) {
        sensorRepository.delete(id);
    }

    @Transactional
    public void updateMtime() {
        sensorRepository.updateMtime();
    }

    @Transactional
    public Sensor update(Sensor sensor) {
        if (sensor.getId() == 0) {
            throw new IllegalArgumentException(ERR_INVALID_ID);
        }
        sensorRepository.updateById(sensor.getModel(), sensor.getLocation(), sensor.getGuarantee(), sensor.getStatus(),
                                    sensor.getProjectId(), sensor.getPhone(), sensor.getId());
        return sensor;
    }

    @Transactional
    public void updateStatusAndGateway(String status, Sensor sensor, long ts) {
        if (!testEnv() && SENSOR_TEST.equals(status)) {
            status = SENSOR_NORMAL;
        }
        sensorRepository.updateStatusAndGatewayById(status.equals(SENSOR_TEST) ? SENSOR_NORMAL : status, sensor.getGatewayId(), sensor.getId());

        if (OpTaskAlarmCause.contains(status)) {
            OpTask ot = new OpTask(sensor.getEui(), 1, new Timestamp(ts), status, OPTASK_UNSOLVED, sensor.getProjectId());
            opTaskRepository.save(ot);

            if (OpTaskSmsCause.contains(status)) {

                List<String> recvs = new ArrayList<>(2);
                if (validatePhone(sensor.getPhone())) {
                    recvs.add(sensor.getPhone());
                }

                Project p = null;
                if (sensor.getProjectId() > 0) {
                    p = projectRepository.findOne(sensor.getProjectId());
                }
                if (p != null && validatePhone(p.getPhone()) && (recvs.size() < 1 || !recvs.get(0).equals(p.getPhone()))) {
                    recvs.add(p.getPhone());
                }
                if (recvs.size() == 0) {
                    recvs.add(config.getAdminPhone());
                }
                smsService.send(recvs, p.fullAddress() + " " + sensor.getLocation(), FORMAT.format(new Date(ts)), p.getPhone());
            }
        }
    }

    @Transactional
    public void updateLocationAndStatus(String lati, String longi, String status, long id) {
        sensorRepository.updateLatiAndLongiAndStatusById(lati, longi, status, id);
        sensorRepository.updateLatiAndLongiByGatewayId(id);
    }

    public Sensor find(long id) {
        return sensorRepository.findById(id);
    }

    public Sensor findBaseByEui(long eui) {
        return sensorRepository.findByEui(eui);
    }

    public long countByGatewayId(long gatewayId) {
        return sensorRepository.countByGatewayId(gatewayId);
    }

    public Page<Sensor> findLike(long projectId, String eui, String model, String type, String location, String guarantee,
                                 String status, String phone, Date start, Date end, int page, int limit) {
        Page<Sensor> pages;
        if (projectId < 0) {
            pages = sensorRepository.findLike
                    (postLike(eui), like(model), like(type), like(location), like(guarantee), like(status), like(phone), start, end, page, limit);
        } else {
            pages = sensorRepository.findLike
                    (projectId, postLike(eui), like(model), like(type), like(location), like(guarantee), like(status), like(phone), start, end, page, limit);
        }
        return pages;
    }

    public List<Sensor> findAlarmedByProjectId(long projectId) {
        List<Sensor> sensors = sensorRepository.findByProjectIdAndTypeAndStatusIsIn(projectId, SENSOR_SMOKE, CriticalSensorStatus);
        sensors.forEach(v -> complete(v));
        return sensors;
    }

    public List<Sensor> findBaseByType(String type) {
        return sensorRepository.findByType(type);
    }

    public List<Sensor> findByMtimeBefore(Date date) {
        return sensorRepository.findByMtimeBefore(date);
    }

    private void complete(Sensor sensor) {
        sensor.setOpCount(opTaskRepository.countByEui(sensor.getEui()));
    }
}
