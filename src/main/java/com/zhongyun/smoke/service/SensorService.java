package com.zhongyun.smoke.service;

import com.zhongyun.smoke.dao.mysql.SensorRepository;
import com.zhongyun.smoke.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        sensor.setMtime(new Timestamp(System.currentTimeMillis()));
        sensor.setCtime(new Timestamp(System.currentTimeMillis()));
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

    public List<Sensor> findAll() {
        return repository.findAll();
    }
}
