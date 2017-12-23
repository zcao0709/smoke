package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.Sensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by caozhennan on 2017/11/30.
 */
public interface SensorRepository extends JpaRepository<Sensor, Long>, SensorOther {
    Sensor findByEui(long eui);
    List<Sensor> findByProjectIdAndType(long projectId, String type);
    List<Sensor> findByProjectIdAndTypeAndStatusIsIn(long projectId, String type, Set<String> statuses);
    List<Sensor> findByType(String type);
    long countByProjectIdAndType(long projectId, String type);
    Page<Sensor> findByEui16LikeAndModelLikeAndTypeLikeAndLocationLikeAndGuaranteeLikeAndStatusLikeAndPhoneLikeAndInstallTimeBetween(
            String eui, String model, String type, String location, String gurantee, String status, String phone,
            Date installTimeStart, Date installTimeEnd, Pageable pageable);
    Page<Sensor> findByProjectIdAndEui16LikeAndModelLikeAndTypeLikeAndLocationLikeAndGuaranteeLikeAndStatusLikeAndPhoneLikeAndInstallTimeBetween(
            long projectId, String eui, String model, String type, String location, String gurantee, String status, String phone,
            Date installTimeStart, Date installTimeEnd, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE sensor SET project_id = 0 WHERE project_id = ?1", nativeQuery = true)
    void clearProjectId(long projectId);

    @Modifying
    @Query(value = "UPDATE sensor SET model = ?1, location = ?2, guarantee = ?3, status = ?4, project_id = ?5, phone = ?6, mtime = NOW() " +
            "WHERE id = ?7", nativeQuery = true)
    void updateById(String model, String location, String guarantee, String status, long projectId, String phone, long id);

    @Modifying
    @Query(value = "UPDATE sensor SET status = ?1, gateway_id = ?2, mtime = NOW() WHERE id = ?3", nativeQuery = true)
    void updateStatusAndGatewayById(String status, long gatewayId, long id);

    @Modifying
    @Query(value = "UPDATE sensor SET lati = ?1, longi = ?2, mtime = NOW() WHERE id = ?3", nativeQuery = true)
    int updateLatiAndLongiById(String lati, String longi, long id);
}
