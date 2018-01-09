package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.model.Sensor;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by caozhennan on 2017/12/8.
 */
public interface SensorOther {
    int updateLatiAndLongiByGatewayId(long gatewayId);
    Sensor findById(long id);
    Page<Sensor> findLike(String eui, String model, String type, String location, String gurantee, String status, String phone,
                          Date installTimeStart, Date installTimeEnd, int page, int limit);
    Page<Sensor> findLike(long projectId, String eui, String model, String type, String location, String gurantee, String status, String phone,
                          Date installTimeStart, Date installTimeEnd, int page, int limit);
    List<Sensor> findByProjectIdAndTypeAndStatusIsIn(long projectId, String type, Set<String> status);
}
