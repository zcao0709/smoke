package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by caozhennan on 2017/11/30.
 */
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Sensor findByEui(long eui);
}
