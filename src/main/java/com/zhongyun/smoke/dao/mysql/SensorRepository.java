package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by caozhennan on 2017/11/30.
 */
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Sensor findByEui(long eui);
    List<Sensor> findByProjectIdAndType(long projectId, String type);
    List<Sensor> findByProjectIdAndTypeAndStatusIsNot(long projectId, String type, String status);
    List<Sensor> findByType(String type);
}
