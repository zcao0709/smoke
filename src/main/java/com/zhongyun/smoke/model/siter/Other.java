package com.zhongyun.smoke.model.siter;

import com.zhongyun.smoke.common.Util;

/**
 * Created by caozhennan on 2018/3/24.
 */
public class Other extends SiterW {
    private static final int REQ_GW_DATA_LEN = 0;
    private static final int REQ_DATA_LEN = 5;
    private static final int RESP_DATA_LEN = 1;

    protected Other(byte[] raw) {
        super(raw);
    }

    @Override
    public boolean validateCmd() {
        if (term() == TERM_GATEWAY) {
            return len() == REQ_GW_DATA_LEN;
        } else {
            return len() == REQ_DATA_LEN;
        }
    }

    @Override
    protected boolean hasChild() {
        if (term() == TERM_GATEWAY) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected String state() {
        switch (cmd()) {
            case CMD_DISC:
                return Util.SENSOR_DISCONN;
            case CMD_BATLOW:
                return Util.SENSOR_BATTERY;
            case CMD_POWEROFF:
                return Util.SENSOR_FAULT;
            case CMD_CONN:
            case CMD_BATNOR:
            case CMD_POWERON:
                return Util.SENSOR_NORMAL;
        }
        return Util.SENSOR_UNKNOWN;
    }

    @Override
    public byte[] responseCmd() {
        byte[] ret = new byte[MIN_LEN + RESP_DATA_LEN];
        ret[OFFSET_LEN] = RESP_DATA_LEN;

        return ret;
    }
}
