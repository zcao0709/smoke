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

    public static final int HEADER_LEN = 13;
    public static final int MIN_LEN = HEADER_LEN + 1; // add 1 byte checksum
    public static final int MAX_LEN = HEADER_LEN + 256 + 1;

    public static final byte HEAD = 0x57;
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

    private static final Logger logger = LoggerFactory.getLogger("Frame");

    private byte seq;
    private byte cmd;
    private byte[] rawId;
    private int id;
    private byte term;

    public Frame() {
    }

    public static Frame parse(ByteBuffer buffer) {
        int p = buffer.position();
        byte[] buf = buffer.array();
        logger.info("buffer received: " + Util.byteArray(buf));
        int startIndex = 0;
        for (; startIndex < p; startIndex++) {
            if (buf[startIndex] == HEAD) {
                break;
            }
        }
        if (startIndex == p) {
            buffer.clear();
            return null;
        }
        int endIndex = startIndex + MIN_LEN;  // exclusive
        if (p < endIndex) {
            logger.error("not enough data read: " + buffer.toString() + ", need " + MIN_LEN);
            resetBuffer(buffer, buf, startIndex, p);
            return null;
        }
        int lenIndex = startIndex + HEADER_LEN - 1;
        endIndex += buf[lenIndex];
        if (p < endIndex) {
            logger.error("not enough data read: " + buffer.toString() + ", need " + MIN_LEN + buf[lenIndex]);
            resetBuffer(buffer, buf, startIndex, p);
            return null;
        }
        Frame f = Frame.create(buf, startIndex, endIndex);
        resetBuffer(buffer, buf, endIndex, p);
        return f;
    }

    public static Frame create(byte[] bytes, int start, int end) {
        logger.info("frame start at " + start + ", end at " + end);

        if (bytes[start] != HEAD) {
            logger.error("unsupported head: " + bytes[start]);
            return null;
        }
        if (bytes[start+2] != ENCY) {
            logger.error("unsupported encryption: " + bytes[start+2]);
            return null;
        }
        if (bytes[start+10] != TERM_GATEWAY && bytes[start+10] != TERM_SENSOR) {
            logger.error("unsupported termination: " + bytes[start+10]);
            return null;
        }
        int c = checksum(bytes, start, end-1);
        if (c != bytes[end-1]) {
            logger.error("checksum failed: " + bytes[end-1] + " != " + c);
            return null;
        }
        Frame f = new Frame();
        f.seq = bytes[start+1];
        f.cmd = bytes[start+11];
        f.rawId = new byte[3];
        f.id = 0;
        int mask = 0xFF;
        for (int i = 3; i < 6; i++) {
            f.id += (bytes[start+i] & mask);
            f.rawId[i-3] = bytes[start+i];
            if (i == 5)
                break;
            f.id <<= 8;
        }
        f.term = bytes[start+10];
        logger.info(f.toString());
        return f;
    }

    @Override
    public String toString() {
        return id + "(" + Util.byteArray(rawId) + ")-" + seq + "-" + term + "-" + cmd;
    }

    public static byte checksum(byte[] bytes, int start, int end) {
        byte sum = 0;
        for (int i = start; i < end; i++) {
            sum += bytes[i];
        }
        return sum;
    }

    public static byte checksum(byte[] bytes) {
        byte sum = 0;
        for (int i = 0; i < bytes.length-1; i++) {
            sum += bytes[i];
        }
        return sum;
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
        if (cmd == CMD_LOGIN) {
            return loginResponse();
        }
        return null;
    }

    private byte[] loginResponse() {
        byte[] ret = new byte[MIN_LEN+8];
        ret[0] = HEAD;
        ret[1] = seq;
        ret[2] = ENCY;
        ret[3] = rawId[0];
        ret[4] = rawId[1];
        ret[5] = rawId[2];
        // reg code
        ret[10] = term;
        ret[11] = CMD_LOGIN;
        ret[12] = 8;
        // data
        ret[13] = 0; // successful ack
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        ret[14] = (byte)Integer.parseInt(year.substring(0, 2), 16);
        ret[15] = (byte)Integer.parseInt(year.substring(2, 4), 16);
        ret[16] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.MONTH) + 1), 16);
        ret[17] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.DAY_OF_MONTH)), 16);
        ret[18] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.HOUR_OF_DAY)), 16);
        ret[19] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.MINUTE)), 16);
        ret[20] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.SECOND)), 16);

        ret[21] = checksum(ret);
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
