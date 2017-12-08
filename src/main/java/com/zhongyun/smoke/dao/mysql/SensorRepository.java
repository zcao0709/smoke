package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by caozhennan on 2017/11/30.
 */
public interface SensorRepository extends JpaRepository<Sensor, Long>, SensorOther {
    Sensor findByEui(long eui);
    List<Sensor> findByProjectIdAndType(long projectId, String type);
    List<Sensor> findByProjectIdAndTypeAndStatusIsNot(long projectId, String type, String status);
    List<Sensor> findByProjectIdAndTypeAndStatusIsIn(long projectId, String type, Set<String> statuses);
    List<Sensor> findByType(String type);
}
