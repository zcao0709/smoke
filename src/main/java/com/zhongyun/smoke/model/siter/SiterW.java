package com.zhongyun.smoke.model.siter;

import com.zhongyun.smoke.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by caozhennan on 2018/3/24.
 */
public class SiterW extends SiterFrame {
    private static final int HEAD_LEN = 13;
    private static final int MIN_LEN = HEAD_LEN + 1; // including checksum

    private static final int OFFSET_HEAD = 0;
    private static final int OFFSET_SEQ  = 1;
    private static final int OFFSET_ENCY = 2;
    private static final int OFFSET_ID   = 3;
    private static final int OFFSET_REG  = 6;
    private static final int OFFSET_TERM = 10;
    private static final int OFFSET_CMD  = 11;
    private static final int OFFSET_LEN  = 12;
    private static final int OFFSET_DATA = 13;

    private static final byte ENCY = 0x00;
    private static final byte TERM_SENSOR = 0x04;
    private static final byte TERM_GATEWAY = 0x0A;

    private static final byte CMD_LOGIN = 0x01;
    private static final byte CMD_HB    = 0x02;
    private static final byte CMD_DISC  = 0x03;
    private static final byte CMD_CONN  = 0x13;
    private static final byte CMD_ALARM = 0x04;
    private static final byte CMD_LOW   = 0x05;
    private static final byte CMD_NORMAL= 0x15;

    private static final int LEN_LOGIN = 8;

    private static final Logger logger = LoggerFactory.getLogger("SiterW");

    private byte[] raw;

    public SiterW(byte[] raw) {
        this.raw = raw;
    }

    public static SiterW parse(byte[] buf, int start, int limit) {
        int end = start + MIN_LEN;
        if (limit >= end) {
            end += buf[start+OFFSET_LEN];
        }
        if (limit < end) {
            logger.error("not enough data in buffer, expected: " + end + ", but got: " + limit);
            return null;
        }
        byte[] real = new byte[end-start];
        for (int i = 0; i < real.length; i++) {
            real[i] = buf[start+i];
        }
        return new SiterW(real);
    }

    @Override
    public boolean validate() {
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

    private byte head() {
        return raw[OFFSET_HEAD];
    }

    private byte seq() {
        return raw[OFFSET_SEQ];
    }

    private byte ency() {
        return raw[OFFSET_ENCY];
    }

    @Override
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

    private byte term() {
        return raw[OFFSET_TERM];
    }

    private byte cmd() {
        return raw[OFFSET_CMD];
    }

    private byte len() {
        return raw[OFFSET_LEN];
    }

    private boolean checksum() {
        return calcChecksum(raw) == raw[raw.length-1];
    }

    private static byte calcChecksum(byte[] raw) {
        byte sum = 0;
        for (int i = 0; i < raw.length-1; i++) {
            sum += raw[i];
        }
        return sum;
    }

    @Override
    public String toString() {
        return String.format("head-%c/seq-%d/id-%d/term-%d/cmd-%d/len-%d/%s", head(), seq(), id(), term(), cmd(), len(), Util.byteArray(raw));
    }

    @Override
    public byte[] response() {
        switch (cmd()) {
            case CMD_LOGIN:
                return loginResp();
            default:
                logger.error("unsupported command: " + cmd());
                return null;
        }
    }

    private byte[] loginResp() {
        byte[] ret = new byte[MIN_LEN +LEN_LOGIN];
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
