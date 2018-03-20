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
    public static final byte TERM = 0x04;

    private static final Logger logger = LoggerFactory.getLogger("Frame");

    private int seq;
    private int cmd;
    private int id;

    public Frame() {
    }

    public static Frame parse(ByteBuffer buffer) {
        int p = buffer.position();
        if (p >= MIN_LEN) {
            byte[] buf = buffer.array();
            logger.debug("buffer: " + Arrays.toString(buf));
            int size = buf[HEADER_LEN - 1];
            if (size == 0) {
                Frame f = Frame.create(buf, 0, MIN_LEN);
                buffer.clear();
                if (p > MIN_LEN) {
                    buffer.put(buf, MIN_LEN, p-MIN_LEN);
                }
                return f;
            } else {
                if (p >= MIN_LEN+size) {
                    Frame f = Frame.create(buf, 0, MIN_LEN+size);
                    buffer.clear();
                    if (p > MIN_LEN+size) {
                        buffer.put(buf, MIN_LEN+size, p-MIN_LEN-size);
                    }
                    return f;
                }
                return null;
            }
        } else {
            logger.error("not enough data read: " + buffer.toString());
            return null;
        }
    }

    public static Frame create(byte[] bytes, int offset, int len) {
        if (bytes[offset] != HEAD) {
            logger.error("unsupported head: " + bytes[offset]);
            return null;
        }
        if (bytes[offset+2] != ENCY) {
            logger.error("unsupported encryption: " + bytes[offset+2]);
            return null;
        }
        if (bytes[offset+10] != TERM) {
            logger.error("unsupported termination: " + bytes[offset+10]);
            return null;
        }
        int c = checksum(bytes, offset, len-1);
        if (c != bytes[offset+len-1]) {
            logger.error("checksum failed: " + bytes[offset+len-1] + " != " + c);
            return null;
        }
        Frame f = new Frame();
        f.seq = bytes[offset+1];
        f.cmd = bytes[offset+11];
        f.id = 0;
        for (int i = 3; i < 6; i++) {
            f.id += bytes[offset+i];
            f.id <<= 8;
        }
        return f;
    }

    @Override
    public String toString() {
        return id + "-" + seq + "-" + cmd;
    }

    public static int checksum(byte[] bytes, int offset, int len) {
        int sum = 0;
        for (int i = offset; i < len; i++) {
            sum += bytes[i];
        }
        return sum;
    }
}
