package com.zhongyun.smoke.model.nbiot;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.model.siter.SiterFrame;
import com.zhongyun.smoke.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.Timestamp;

/**
 * Created by caozhennan on 2018/5/20.
 */
public class NbiotFrame {

    public static final int MAX_LEN = 1 + 15 + 1 + 1;  // fixed

    private static final byte HEAD = 0x5A;
    private static final byte ACK_OK = 1;

    private static final int OFFSET_IMEI = 1;
    private static final int OFFSET_STATE = 16;

    private static final byte STATE_ALARM = 0x01;
    private static final byte STATE_BATTERY = 0x02;
    private static final byte STATE_FAULT = 0x04;
    private static final byte STATE_SAMPLEFAULT = 0x08;
    private static final byte STATE_INITFAULT = 0x10;
    private static final byte STATE_DIRTYFAULT = 0x20;
    private static final byte STATE_TEST = 0x40;

    private static final Logger logger = LoggerFactory.getLogger("NbiotFrame");

    private byte[] raw;

    public NbiotFrame(byte[] raw) {
        this.raw = raw;
    }

    public static final NbiotFrame parse(ByteBuffer buffer, SensorService sensorService, ApplicationConfig config) {
        int limit = buffer.position();
        byte[] buf = buffer.array();
        logger.info("recv: " + buffer.toString() + " - " + Util.byteArray(buf, 0, limit));

        int start = 0;
        for (; start < limit; start++) {
            if (buf[start] == HEAD) {
                break;
            }
        }
        if (start == limit) {
            buffer.clear();
            return null;
        }
        int end = start + MAX_LEN;
        if (limit < end) {
            logger.error("not enough data in buffer, expected: " + end + ", but got: " + limit);
            return null;
        }
        byte[] real = new byte[end-start];
        for (int i = 0; i < real.length; i++) {
            real[i] = buf[start+i];
        }
        NbiotFrame f = new NbiotFrame(real);
        buffer.clear();
        if (f.validate()) {
            f.persist(sensorService, config);
            return f;
        } else {
            return null;
        }
    }

    public long id() {
        return SiterFrame.id(raw, OFFSET_IMEI, OFFSET_STATE);
    }

    public boolean validate() {
        byte sum = 0;
        for (int i = OFFSET_IMEI; i < OFFSET_STATE; i++) {
            sum += raw[i];
        }
        return sum == raw[MAX_LEN-1];
    }

    public String state() {
        byte s = raw[OFFSET_STATE];
        if ((s & STATE_ALARM) > 0) {
            return Util.SENSOR_FIRE;
        } else if ((s & STATE_BATTERY) > 0) {
            return Util.SENSOR_BATTERY;
        } else if ((s & STATE_FAULT) > 0) {
            return Util.SENSOR_FAULT;
        } else if ((s & STATE_SAMPLEFAULT) > 0) {
            return Util.SENSOR_FAULT;
        } else if ((s & STATE_INITFAULT) > 0) {
            return Util.SENSOR_FAULT;
        } else if ((s & STATE_DIRTYFAULT) > 0) {
            return Util.SENSOR_FAULT;
        } else if ((s & STATE_TEST) > 0) {
            return Util.SENSOR_TEST;
        } else {
            return Util.SENSOR_NORMAL;
        }
    }

    public void persist(SensorService sensorService, ApplicationConfig config) {
        long id = id();
        Sensor s = sensorService.findBaseByEui(id());
        long ts = System.currentTimeMillis();
        if (s == null) {
            s = new Sensor(id, Util.SENSOR_SMOKE, Util.VENDOR_ORENA, new Timestamp(ts), Util.SENSOR_NORMAL,
                           Util.GATEWAY_UNSET, Util.PROJECT_UNSET);
            s.setPhone(config.getAdminPhone());
            s = sensorService.add(s);
        } else {
            sensorService.updateStatusAndGateway(state(), s, ts);
        }
    }

    public byte[] response() {
        byte[] ret = raw.clone();
        ret[OFFSET_STATE] = 1;

        return ret;
    }
}
