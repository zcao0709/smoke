package com.zhongyun.smoke.model.siter;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by caozhennan on 2018/3/24.
 */
public abstract class SiterW extends SiterFrame {
    //    private static final int HEAD_LEN = 13;
    protected static final int MIN_LEN = 14; // including checksum

    protected static final int OFFSET_HEAD = 0;
    protected static final int OFFSET_SEQ = 1;
    protected static final int OFFSET_ENCY = 2;
    protected static final int OFFSET_ID = 3;
    protected static final int OFFSET_REG = 6;
    protected static final int OFFSET_TERM = 10;
    protected static final int OFFSET_CMD = 11;
    protected static final int OFFSET_LEN = 12;
    protected static final int OFFSET_DATA = 13;
    protected static final int OFFSET_CHILD = 13;
    protected static final int OFFSET_SERIAL = 16;

    private static final byte ENCY = 0x00;
    protected static final byte TERM_SENSOR = 0x04;
    protected static final byte TERM_GATEWAY = 0x0A;

    protected static final byte CMD_LOGIN = 0x01;
    protected static final byte CMD_DISC = 0x03;
    protected static final byte CMD_CONN = 0x13;
    protected static final byte CMD_ALARM = 0x04;
    protected static final byte CMD_BATLOW = 0x05;
    protected static final byte CMD_BATNOR = 0x15;
    protected static final byte CMD_POWEROFF = 0x0D;
    protected static final byte CMD_POWERON = 0x0E;

    private static final Logger logger = LoggerFactory.getLogger("SiterW");

    private byte[] raw;

    public SiterW(byte[] raw) {
        this.raw = raw;
    }

    public static SiterW parse(byte[] buf, int start, int limit) {
        int end = start + MIN_LEN;
        if (limit >= end) {
            end += buf[start + OFFSET_LEN];
        }
        if (limit < end) {
            logger.error("not enough data in buffer, expected: " + end + ", but got: " + limit);
            return null;
        }
        byte[] real = new byte[end - start];
        for (int i = 0; i < real.length; i++) {
            real[i] = buf[start + i];
        }
        switch (buf[OFFSET_CMD]) {
            case CMD_LOGIN:
                return new Login(real);
            case CMD_ALARM:
                return new Alarm(real);
            case CMD_DISC:
            case CMD_CONN:
            case CMD_POWEROFF:
            case CMD_POWERON:
            case CMD_BATLOW:
            case CMD_BATNOR:
                return new Other(real);
            default:
                logger.error("unsupported command: " + buf[OFFSET_CMD]);
                return null;
        }
    }

    @Override
    public boolean validate() {
        if (validateHeader()) {
            if (validateCmd()) {
                return true;
            } else {
                logger.error("unsupported data len: " + len() + " in cmd: " + cmd());
            }
        }
        return false;
    }

    protected boolean validateHeader() {
        logger.info("raw frame: " + Util.byteArray(raw));

        if (ency() != ENCY) {
            logger.error("unsupported encryption: " + ency());
            return false;
        }
        if (term() != TERM_GATEWAY && term() != TERM_SENSOR) {
            logger.error("unsupported termination: " + term());
            return false;
        }
        if (!checksum()) {
            logger.error("checksum failed: " + Util.byteArray(raw));
            return false;
        }
        return true;
    }

    protected final byte head() {
        return raw[OFFSET_HEAD];
    }

    protected final byte seq() {
        return raw[OFFSET_SEQ];
    }

    protected final byte ency() {
        return raw[OFFSET_ENCY];
    }

    @Override
    public final long id() {
        return id(raw, OFFSET_ID, OFFSET_REG);
    }

    public long child() {
        if (hasChild()) {
            return id(raw, OFFSET_CHILD, OFFSET_SERIAL);
        }
        return INVALID_ID;
    }

    protected final byte term() {
        return raw[OFFSET_TERM];
    }

    protected final byte cmd() {
        return raw[OFFSET_CMD];
    }

    protected final byte len() {
        return raw[OFFSET_LEN];
    }

    protected final boolean checksum() {
        return calcChecksum(raw) == raw[raw.length - 1];
    }

    protected static byte calcChecksum(byte[] raw) {
        byte sum = 0;
        for (int i = 0; i < raw.length - 1; i++) {
            sum += raw[i];
        }
        return sum;
    }

    @Override
    public String toString() {
        return String.format("head-%c/seq-%d/id-%d/term-%d/cmd-%d/len-%d/%s", head(), seq(), id(), term(), cmd(), len(), Util.byteArray(raw));
    }

    @Override
    public final void persist(SensorService sensorService, ApplicationConfig config) {
        long id = id();
        Sensor sg = sensorService.findBaseByEui(id());
        long ts = System.currentTimeMillis();
        if (sg == null) {
            sg = new Sensor(id, Util.SENSOR_GWRX, Util.VENDOR_SITER, new Timestamp(ts), Util.SENSOR_NORMAL,
                            Util.GATEWAY_UNSET, Util.PROJECT_UNSET);
            sg.setPhone(config.getAdminPhone());
            sg = sensorService.add(sg);
        } else {
            sg.setStatus(state());
            sensorService.update(sg);
        }
        long child = child();
        if (child < 0) {
            return;
        }
        Sensor s = sensorService.findBaseByEui(child);
        if (s == null) {
            // 新的烟感器，项目固定为0，由管理员后续更新
            s = new Sensor(child, Util.SENSOR_SMOKE, Util.VENDOR_SITER, new Timestamp(ts), state(), sg.getId(), Util.PROJECT_UNSET);
            sensorService.add(s);
        } else {
            s.setGatewayId(sg.getId());
            sensorService.updateStatusAndGateway(state(), s, ts);
        }
    }

    @Override
    public byte[] response() {
        byte[] ret = responseCmd();
        // common part
        ret[OFFSET_HEAD] = HEAD_W;
        ret[OFFSET_SEQ] = seq();
        ret[OFFSET_ENCY] = ency();
        for (int i = OFFSET_ID; i < OFFSET_TERM; i++) {
            ret[i] = raw[i];
        }
        ret[OFFSET_TERM] = term();
        ret[OFFSET_CMD]  = raw[OFFSET_CMD];
        ret[OFFSET_DATA] = ACK_OK;
        ret[ret.length-1] = calcChecksum(ret);

        return ret;
    }

    protected abstract boolean validateCmd();

    protected abstract byte[] responseCmd();

    protected abstract boolean hasChild();

    protected abstract String state();
}
