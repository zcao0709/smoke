package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * Created by caozhennan on 2017/11/30.
 */
public interface SensorRepository extends JpaRepository<Sensor, Long>, SensorOther {
    Sensor findByEui(long eui);
    List<Sensor> findByProjectIdAndType(long projectId, String type);
//    List<Sensor> findByProjectIdAndTypeAndStatusIsNot(long projectId, String type, String status);
    List<Sensor> findByProjectIdAndTypeAndStatusIsIn(long projectId, String type, Set<String> statuses);
    List<Sensor> findByType(String type);
    long countByProjectIdAndType(long projectId, String type);

    @Modifying
    @Query(value = "UPDATE sensor SET model = ?1, room = ?2, location = ?3, guarantee = ?4, project_id = ?5, mtime = NOW() WHERE id = ?6", nativeQuery = true)
    void updateById(String model, String room, String location, String guarantee, long projectId, long id);

    @Modifying
    @Query(value = "UPDATE sensor SET status = ?1, gateway_id = ?2, mtime = NOW() WHERE id = ?3", nativeQuery = true)
    void updateStatusAndGatewayById(String status, long gatewayId, long id);
}
