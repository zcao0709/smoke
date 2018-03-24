package com.zhongyun.smoke.model.siter;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by caozhennan on 2018/3/24.
 */
public class SiterM extends SiterFrame {
    private static final int REQ_LEN  = 4;
    private static final int RESP_LEN = 2;

    private static final int OFFSET_ID = 1;

    private byte[] raw;

    public SiterM(byte[] bs) {
        raw = bs;
    }

    private static final Logger logger = LoggerFactory.getLogger("SiterM");

    public static SiterM parse(byte[] buf, int start, int limit) {
        int end = start + REQ_LEN;
        if (limit < end) {
            logger.error("not enough data in buffer, expected: " + end + ", but got: " + limit);
            return null;
        }
        byte[] real = new byte[end-start];
        for (int i = 0; i < real.length; i++) {
            real[i] = buf[start+i];
        }
        return new SiterM(real);
    }

    @Override
    public long id() {
        return id(raw, OFFSET_ID, raw.length);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public byte[] response() {
        byte[] ret = new byte[RESP_LEN];
        ret[0] = HEAD_M;
        ret[1] = ACK_OK;
        return ret;
    }

    @Override
    public void persist(SensorService sensorService, ApplicationConfig config) {
        long id = id();
        Sensor sg = sensorService.findBaseByEui(id());
        if (sg == null) {
            logger.error("recv a frame from unknown sensor: " + id);
        } else {
            // update mtime
            sensorService.update(sg);
        }
    }

    @Override
    public String toString() {
        return String.format("head-%c/id-%d/%s", raw[0], id(), Util.byteArray(raw));
    }
}
