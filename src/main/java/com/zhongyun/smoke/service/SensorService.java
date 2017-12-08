package com.zhongyun.smoke.service;

import com.zhongyun.smoke.common.Util;
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
    public SensorRepository repository;

    public Sensor add(Sensor sensor) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        sensor.setMtime(ts);
        sensor.setCtime(ts);
        return repository.save(sensor);
    }

    public void delete(long id) {
        repository.delete(id);
    }

    public Sensor update(Sensor sensor) {
        sensor.setMtime(new Timestamp(System.currentTimeMillis()));
        return repository.save(sensor);
    }

    public Sensor find(long id) {
        return repository.findOne(id);
    }

    public Sensor findByEui(long eui) {
        return repository.findByEui(eui);
    }

    public List<Sensor> findByProjectId(long projectId) {
        return repository.findByProjectIdAndType(projectId, Util.SENSOR_SMOKE);
    }

    public List<Sensor> findAlarmedByProjectId(long projectId) {
//        return repository.findByProjectIdAndTypeAndStatusIsNot(projectId, Util.SENSOR_GWRX, Util.SENSOR_NORMAL);
        return repository.findByProjectIdAndTypeAndStatusIsIn(projectId, Util.SENSOR_SMOKE, Util.CriticalSensorStatus);
    }

    public List<Sensor> findByType(String type) {
        return repository.findByType(type);
    }

    @Transactional
    public int updateLatiAndLongi(long gatewayId) {
        return repository.updateLatiAndLongiByGatewayId(gatewayId);
    }
}
