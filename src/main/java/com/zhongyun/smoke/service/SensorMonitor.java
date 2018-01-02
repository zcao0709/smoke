package com.zhongyun.smoke.service;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by caozhennan on 2017/12/28.
 */
@Service
public class SensorMonitor {
    @Autowired
    private SensorService service;

    private ConcurrentMap<Long, Long> gwrxTimer = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        service.deleteUselessGateway();
        List<Sensor> sensors = service.findBaseByType(Util.SENSOR_GWRX);
        sensors.forEach(v -> gwrxTimer.put(v.getEui(), v.getMtime().getTime()));
    }
}
