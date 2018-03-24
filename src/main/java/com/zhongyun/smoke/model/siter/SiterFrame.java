package com.zhongyun.smoke.model.siter;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by caozhennan on 2018/3/15.
 */
public abstract class SiterFrame {

    public static final int MAX_LEN = 13 + 256 + 1;  // header + data + checksum

    protected static final byte HEAD_W = 0x57;
    protected static final byte HEAD_M = 0x4D;
    protected static final byte ACK_OK = 0;

    protected static final int INVALID_ID = -1;

    private static final Logger logger = LoggerFactory.getLogger("SiterFrame");

    public static final SiterFrame parse(ByteBuffer buffer) {
        int limit = buffer.position();
        byte[] buf = buffer.array();
        logger.info("recv: " + buffer.toString() + " - " + Util.byteArray(buf, 0, limit));

        int start = 0;
        for (; start < limit; start++) {
            if (buf[start] == HEAD_W || buf[start] == HEAD_M) {
                break;
            }
        }
        if (start == limit) {
            buffer.clear();
            return null;
        }
        SiterFrame f = null;
        if (buf[start] == HEAD_W) {
            f = SiterW.parse(buf, start, limit);
        } else if (buf[start] == HEAD_M) {
            f = SiterM.parse(buf, start, limit);
        }
        if (f == null) {
            return null;
        }
        buffer.clear();
        if (f.validate()) {
            return f;
        } else {
            return null;
        }
    }

    private static void resetBuffer(ByteBuffer buffer, byte[] buf, int start, int end) {
        if (start == 0) {
            return;
        }
        buffer.clear();
        if (end > start) {
            byte[] src = new byte[end-start];
            for (int i = 0; i < src.length; i++) {
                src[i] = buf[start+i];
            }
            buffer.put(src, 0, src.length);
        }
    }

    public final long id(byte[] bs, int start, int end) {
        if (end <= start || bs.length < end) {
            return INVALID_ID;
        }
        long id = 0;
        long mask = 0xFF;
        for (int i = start; i < end; i++) {
            id += (bs[i] & mask);
            if (i == end-1)
                break;
            id <<= 8;
        }
        return id;
    }

    public abstract long id();

    public abstract boolean validate();

    public abstract byte[] response();

    public abstract void persist(SensorService sensorService, ApplicationConfig config);
}
