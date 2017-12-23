package com.zhongyun.smoke.service;

import static com.zhongyun.smoke.common.Util.*;
import com.zhongyun.smoke.dao.mysql.OpTaskRepository;
import com.zhongyun.smoke.dao.mysql.SensorRepository;
import com.zhongyun.smoke.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by caozhennan on 2017/11/30.
 */
@Service
public class SensorService {
    @Autowired
    public SensorRepository sensorRepository;

    @Autowired
    public OpTaskRepository opTaskRepository;

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
    public Sensor update(Sensor sensor) {
        if (sensor.getId() == 0) {
            throw new IllegalArgumentException("no sensor id");
        }
        sensorRepository.updateById(sensor.getModel(), sensor.getLocation(), sensor.getGuarantee(), sensor.getStatus(),
                                    sensor.getProjectId(), sensor.getPhone(), sensor.getId());
        return sensor;
    }

    @Transactional
    public void updateStatusAndGateway(String status, long gatewayId, long id) {
        if (id == 0) {
            return;
        }
        sensorRepository.updateStatusAndGatewayById(status, gatewayId, id);
    }

    @Transactional
    public int updateLatiAndLongiById(String lati, String longi, long id) {
        return sensorRepository.updateLatiAndLongiById(lati, longi, id);
    }

    @Transactional
    public int updateLatiAndLongiByGatewayId(long gatewayId) {
        return sensorRepository.updateLatiAndLongiByGatewayId(gatewayId);
    }

    public Sensor find(long id) {
        Sensor s = sensorRepository.findOne(id);
        if (s != null) {
            complete(s);
        }
        return s;
    }

    public Sensor findBaseByEui(long eui) {
        return sensorRepository.findByEui(eui);
    }

    public List<Sensor> findByProjectId(long projectId) {
        List<Sensor> sensors = sensorRepository.findByProjectIdAndType(projectId, SENSOR_SMOKE);
        sensors.forEach(v -> complete(v));
        return sensors;
    }

    public Page<Sensor> findLike(long projectId, String eui, String model, String type, String location, String guarantee,
                                 String status, String phone, long start, long end, Pageable pageable) {
        if (projectId < 0) {
            return sensorRepository.findByEui16LikeAndModelLikeAndTypeLikeAndLocationLikeAndGuaranteeLikeAndStatusLikeAndPhoneLikeAndInstallTimeGreaterThanEqualAndInstallTimeLessThanEqual
                    (like(eui), like(model), like(type), like(location), like(guarantee), like(status), like(phone), start, end, pageable);
        } else {
            return sensorRepository.findByProjectIdAndEui16LikeAndModelLikeAndTypeLikeAndLocationLikeAndGuaranteeLikeAndStatusLikeAndPhoneLikeAndInstallTimeGreaterThanEqualAndInstallTimeLessThanEqual
                    (projectId, like(eui), like(model), like(type), like(location), like(guarantee), like(status), like(phone), start, end, pageable);
        }
    }

    public List<Sensor> findAlarmedByProjectId(long projectId) {
        List<Sensor> sensors = sensorRepository.findByProjectIdAndTypeAndStatusIsIn(projectId, SENSOR_SMOKE, CriticalSensorStatus);
        sensors.forEach(v -> complete(v));
        return sensors;
    }

    public List<Sensor> findBaseByType(String type) {
        return sensorRepository.findByType(type);
    }

    private void complete(Sensor sensor) {
        sensor.setOpCount(opTaskRepository.countByEui(sensor.getEui()));
    }
}
