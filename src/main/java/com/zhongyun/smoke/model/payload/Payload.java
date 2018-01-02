package com.zhongyun.smoke.model.payload;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.OpTask;
import com.zhongyun.smoke.service.OpTaskService;
import com.zhongyun.smoke.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by caozhennan on 2017/11/25.
 */
public abstract class Payload {

    public static Map<Integer, String> status = new HashMap<Integer, String>() {{
        put(1, Util.SENSOR_FIRE);
        put(2, Util.SENSOR_BATTERY);
        put(3, Util.SENSOR_FAULT);
        put(7, Util.SENSOR_MUTE);
        put(9, Util.SENSOR_NORMAL);
        put(-1, Util.SENSOR_NORMAL);
    }};

    static {
        if (System.getProperty("spring.profiles.active").equals("dev")) {
            status.put(9, Util.SENSOR_TEST);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger("Payload");

    public abstract App parseApp();

    public static void parse(String payload, MongoTemplate mongo, SensorService sensorService, ConcurrentMap<Long, Long> gatewayTs) {
        if (payload.startsWith("{\"gateway\"")) {
            Gwrx g = Util.json2Object(payload, Gwrx.class);
            g.update(sensorService, gatewayTs);

        } else {
            Payload p;
            if (payload.startsWith("{\"immeAPP\"")) {
                p = Util.json2Object(payload, ImmeApp.class);
            } else if (payload.startsWith("{\"app\"")) {
                p = Util.json2Object(payload, UpApp.class);
            } else {
                return;
            }
            if (p == null) {
                logger.error("invalid app message: " + payload);
                return;
            }
            App app = p.parseApp();
            mongo.save(app, Util.MONGO_COLLECTION);
            app.update(sensorService, gatewayTs);
        }
    }

    public static String decode(String raw) {
        if (raw == null) {
            logger.error("empty app message payload");
            return Util.SENSOR_UNKNOWN;
        }
        byte[] bs = Util.fromBase64(raw);
        if (bs.length == 0) {
            logger.error("invalid app message payload: " + raw + ", cannot decode");
            return Util.SENSOR_UNKNOWN;
        }
        String s = status.get((int) bs[bs.length - 1]);
        if (s == null) {
            logger.error("invalid app message payload: " + raw + ", decoded: " + new String(bs));
            return Util.SENSOR_UNKNOWN;
        }
        return s;
    }
}
