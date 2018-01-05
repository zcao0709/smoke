package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.model.Sensor;

import java.util.Date;

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
}
