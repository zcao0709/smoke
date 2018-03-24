package com.zhongyun.smoke.model.siter;

import com.zhongyun.smoke.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * Created by caozhennan on 2018/3/15.
 */
public class Frame {

    public static final int W_HEAD_LEN = 13;
    public static final int W_MIN_LEN = W_HEAD_LEN + 1; // including checksum
    public static final int M_MIN_LEN = 4;
    public static final int MAX_LEN = W_HEAD_LEN + 256 + 1;

    public static final int OFFSET_HEAD = 0;
    public static final int OFFSET_SEQ  = 1;
    public static final int OFFSET_ENCY = 2;
    public static final int OFFSET_ID   = 3;
    public static final int OFFSET_REG  = 6;
    public static final int OFFSET_TERM = 10;
    public static final int OFFSET_CMD  = 11;
    public static final int OFFSET_LEN  = 12;
    public static final int OFFSET_DATA = 13;

    public static final byte HEAD_W = 0x57;
    public static final byte HEAD_M = 0x4D;
    public static final byte ENCY = 0x00;
    public static final byte TERM_SENSOR = 0x04;
    public static final byte TERM_GATEWAY = 0x0A;

    public static final byte CMD_LOGIN = 0x01;
    public static final byte CMD_HB    = 0x02;
    public static final byte CMD_DISC  = 0x03;
    public static final byte CMD_CONN  = 0x13;
    public static final byte CMD_ALARM = 0x04;
    public static final byte CMD_LOW   = 0x05;
    public static final byte CMD_NORMAL= 0x15;

    public static final byte ACK_OK = 0;

    public static final int LEN_LOGIN = 8;

    public static final int FULL_LEN_HB = 2;

    private static final Logger logger = LoggerFactory.getLogger("Frame");

    private byte[] raw;

    public Frame(byte[] raw) {
        this.raw = raw;
    }

    public static Frame parse(ByteBuffer buffer) {
        int limit = buffer.position();
        byte[] buf = buffer.array();
        logger.info("buffer received: " + buffer.toString() + " - " + Util.byteArray(buf, 0, limit));

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
        int end = 0;
        if (buf[start] == HEAD_W) {
            end = start + W_MIN_LEN;  // exclusive
            if (limit >= end) {
                end += buf[start+OFFSET_LEN];
            }
        } else if (buf[start] == HEAD_W) {
            end = start + M_MIN_LEN;
        }
        if (limit < end) {
            logger.error("not enough data in buffer, expected: " + end + ", but got: " + limit);
            return null;
        }
        buffer.clear();

        byte[] real = new byte[end-start];
        for (int i = 0; i < real.length; i++) {
            real[i] = buf[start+i];
        }
        return Frame.create(real);
    }

    public static Frame create(byte[] bytes) {
        logger.info("raw frame: " + Util.byteArray(bytes));
        Frame f = new Frame(bytes);

        if (f.ency() != ENCY) {
            logger.error("unsupported encryption: " + f.ency());
            return null;
        }
        if (f.term() != TERM_GATEWAY && f.term() != TERM_SENSOR) {
            logger.error("unsupported termination: " + f.term());
            return null;
        }
        if (f.checksum()) {
            logger.error("checksum failed: " + Util.byteArray(bytes));
            return null;
        }
        return f;
    }

    public byte head() {
        return raw[OFFSET_HEAD];
    }

    public byte seq() {
        return raw[OFFSET_SEQ];
    }

    public byte ency() {
        return raw[OFFSET_ENCY];
    }

    public int id() {
        int id = 0;
//        int mask = 0xFF;
        for (int i = OFFSET_ID; i < OFFSET_REG; i++) {
            id += (raw[i] & 0xFF);
            if (i == 5)
                break;
            id <<= 8;
        }
        return id;
    }

    public byte term() {
        return raw[OFFSET_TERM];
    }

    public byte cmd() {
        return raw[OFFSET_CMD];
    }

    public byte len() {
        return raw[OFFSET_LEN];
    }

    public boolean checksum() {
        return calcChecksum(raw) == raw[raw.length-1];
    }

    public static byte calcChecksum(byte[] raw) {
        byte sum = 0;
        for (int i = 0; i < raw.length-1; i++) {
            sum += raw[i];
        }
        return sum;
    }

    @Override
    public String toString() {
        return Util.byteArray(raw);
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

    public byte[] response() {
        if (head() == HEAD_M) {
            return hbResp();

        } else if (head() == HEAD_W) {
            switch (cmd()) {
                case CMD_LOGIN:
                    return loginResp();
                default:
                    logger.error("unsupported command: " + cmd());
                    return null;
            }
        }
        logger.error("unsupported head: " + head());
        return null;
    }

    private byte[] loginResp() {
        byte[] ret = new byte[W_MIN_LEN+LEN_LOGIN];
        ret[0] = HEAD_W;
        ret[1] = seq();
        ret[2] = ency();
        for (int i = OFFSET_ID; i < OFFSET_TERM; i++) {
            ret[i] = raw[i];
        }
        ret[10] = term();
        ret[11] = CMD_LOGIN;
        ret[12] = LEN_LOGIN;

        ret[13] = ACK_OK;
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        ret[14] = (byte)Integer.parseInt(year.substring(0, 2), 16);
        ret[15] = (byte)Integer.parseInt(year.substring(2, 4), 16);
        ret[16] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.MONTH) + 1), 16);
        ret[17] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.DAY_OF_MONTH)), 16);
        ret[18] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.HOUR_OF_DAY)), 16);
        ret[19] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.MINUTE)), 16);
        ret[20] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.SECOND)), 16);
        ret[21] = calcChecksum(ret);
        return ret;
    }

    private byte[] hbResp() {
        byte[] ret = new byte[FULL_LEN_HB];
        ret[0] = HEAD_M;
        ret[1] = ACK_OK;
        return ret;
    }

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        System.out.printf("%02X\n", Integer.parseInt(year.substring(0, 2), 16));
        System.out.printf("%02X\n", Integer.parseInt(year.substring(2, 4), 16));
        System.out.printf("%02X\n", Integer.parseInt(String.valueOf(c.get(Calendar.MONTH) + 1), 16));
        System.out.printf("%02X\n", Integer.parseInt(String.valueOf(c.get(Calendar.DAY_OF_MONTH)), 16));
        System.out.printf("%02X\n", Integer.parseInt(String.valueOf(c.get(Calendar.HOUR_OF_DAY)), 16));
        System.out.printf("%02X\n", Integer.parseInt(String.valueOf(c.get(Calendar.MINUTE)), 16));
        System.out.printf("%02X\n", Integer.parseInt(String.valueOf(c.get(Calendar.SECOND)), 16));
    }
}
