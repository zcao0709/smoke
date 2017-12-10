package com.zhongyun.smoke.service;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.dao.mysql.OpTaskRepository;
import com.zhongyun.smoke.dao.mysql.SensorRepository;
import com.zhongyun.smoke.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
        sensorRepository.updateById(sensor.getModel(), sensor.getRoom(), sensor.getLocation(), sensor.getGuarantee(), sensor.getProjectId(), sensor.getId());
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
        List<Sensor> sensors = sensorRepository.findByProjectIdAndType(projectId, Util.SENSOR_SMOKE);
        sensors.forEach(v -> complete(v));
        return sensors;
    }

    public List<Sensor> findAlarmedByProjectId(long projectId) {
        List<Sensor> sensors = sensorRepository.findByProjectIdAndTypeAndStatusIsIn(projectId, Util.SENSOR_SMOKE, Util.CriticalSensorStatus);
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
