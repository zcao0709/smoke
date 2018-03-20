package com.zhongyun.smoke.model.siter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;

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

    private static final Logger logger = LoggerFactory.getLogger("Frame");

    private int seq;
    private int cmd;
    private int id;

    public Frame() {
    }

    public static Frame parse(ByteBuffer buffer) {
        int p = buffer.position();
        byte[] buf = buffer.array();
        logger.info("buffer received: " + Arrays.toString(buf));
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
        f.id = 0;
        for (int i = 3; i < 6; i++) {
            f.id += bytes[start+i];
            f.id <<= 8;
        }
        return f;
    }

    @Override
    public String toString() {
        return id + "-" + seq + "-" + cmd;
    }

    public static byte checksum(byte[] bytes, int offset, int end) {
        byte sum = 0;
        for (int i = offset; i < end; i++) {
            sum += bytes[i];
        }
        return sum;
    }

    private static void resetBuffer(ByteBuffer buffer, byte[] buf, int start, int end) {
        if (start == 0) {
            return;
        }
        buffer.reset();
        if (end > start) {
            buffer.put(buf, start, end-start);
        }
    }
}
