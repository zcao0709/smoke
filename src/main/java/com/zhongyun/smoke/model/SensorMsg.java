package com.zhongyun.smoke.model;

import com.zhongyun.smoke.service.SensorService;

/**
 * Created by caozhennan on 2018/6/23.
 */
public interface SensorMsg {
    Sensor toGateway();
    Sensor toSensor();
//    Sensor dbGateway(SensorService sensorService);
//    Sensor dbSensor(SensorService sensorService);
    boolean valid();
    String state();

    Sensor[] defReturn = {null, null};

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

    default Sensor updateGateway(SensorService sensorService, Sensor dbGateway) {
        dbGateway.setStatus(state());
        sensorService.update(dbGateway);
        return dbGateway;
    }

    default Sensor upsertSensor(SensorService sensorService, Sensor dbGateway, Sensor dbSensor, long ts) {
        if (dbSensor == null) {
            dbSensor = toSensor();
            if (dbSensor != null) {
                if (dbGateway != null) {
                    dbSensor.setGatewayId(dbGateway.getId());
                }
                sensorService.add(dbSensor);
            }
            return dbSensor;
        } else {
            dbSensor.setStatus(state());
            sensorService.updateStatusAndGateway(dbSensor, ts);
            return dbSensor;
        }
    }

    /*
    default Sensor[] handleMsg(SensorService sensorService) {
        if (!valid()) {
            return defReturn;
        }
        Sensor dbGateway = dbGateway(sensorService);
        Sensor dbSensor = dbSensor(sensorService);

        if (dbGateway == null) {
            dbGateway = newGateway(sensorService, dbSensor);
        } else {
            dbGateway = updateGateway(sensorService, dbGateway);
        }
        dbSensor = upsertSensor(sensorService, dbGateway, dbSensor, System.currentTimeMillis());

        return new Sensor[]{dbGateway, dbSensor};
    }
    */
}
