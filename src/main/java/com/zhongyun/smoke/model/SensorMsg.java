package com.zhongyun.smoke.model;

import com.zhongyun.smoke.service.SensorService;

/**
 * Created by caozhennan on 2018/6/23.
 */
public interface SensorMsg {
    Sensor toGateway();
    Sensor toSensor();
    String state();

    default Sensor newGateway(SensorService sensorService, Sensor dbSensor) {
        Sensor gateway = toGateway();
        if (gateway != null) {
            if (dbSensor != null) {
                gateway.setProjectId(dbSensor.getProjectId());
            }
            sensorService.add(gateway);
        }
        return gateway;
    }

    default void pingGateway(SensorService sensorService, Sensor dbGateway) {
        dbGateway.setStatus(state());
        sensorService.update(dbGateway);
    }

    default void upsertSensor(SensorService sensorService, Sensor dbGateway, Sensor dbSensor, long ts) {
        if (dbSensor == null) {
            dbSensor = toSensor();
            if (dbSensor != null) {
                if (dbGateway != null) {
                    dbSensor.setGatewayId(dbGateway.getId());
                }
                sensorService.add(dbSensor);
            }
        } else {
            sensorService.updateStatusAndGateway(state(), dbSensor, ts);
        }
    }
}
