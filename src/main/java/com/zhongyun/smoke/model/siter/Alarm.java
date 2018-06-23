package com.zhongyun.smoke.model.siter;

import com.zhongyun.smoke.common.Util;

/**
 * Created by caozhennan on 2018/3/24.
 */
public class Alarm extends SiterW {
    private static final int REQ_DATA_LEN = 5;
    private static final int RESP_DATA_LEN = 1;

    protected Alarm(byte[] raw) {
        super(raw);
    }

    @Override
    protected boolean validateCmd() {
        return len() == REQ_DATA_LEN;
    }

    @Override
    protected boolean hasChild() {
        return true;
    }

    @Override
    public String state() {
        return Util.SENSOR_FIRE;
    }

    @Override
    protected byte[] responseCmd() {
        byte[] ret = new byte[MIN_LEN + RESP_DATA_LEN];
        ret[OFFSET_LEN] = RESP_DATA_LEN;

        return ret;
    }
}
