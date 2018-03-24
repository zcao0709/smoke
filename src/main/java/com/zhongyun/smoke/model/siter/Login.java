package com.zhongyun.smoke.model.siter;

import com.zhongyun.smoke.common.Util;

import java.util.Calendar;

/**
 * Created by caozhennan on 2018/3/24.
 */
public class Login extends SiterW {
    private static final int REQ_DATA_LEN = 1;
    private static final int RESP_DATA_LEN = 8;

    protected Login(byte[] raw) {
        super(raw);
    }

    @Override
    protected boolean validateCmd() {
        return len() == REQ_DATA_LEN;
    }

    @Override
    protected boolean hasChild() {
        return false;
    }

    @Override
    protected String state() {
        return Util.SENSOR_NORMAL;
    }

    @Override
    protected byte[] responseCmd() {
        byte[] ret = new byte[MIN_LEN + RESP_DATA_LEN];
        ret[OFFSET_LEN] = RESP_DATA_LEN;

        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        ret[OFFSET_DATA+1] = (byte)Integer.parseInt(year.substring(0, 2), 16);
        ret[OFFSET_DATA+2] = (byte)Integer.parseInt(year.substring(2, 4), 16);
        ret[OFFSET_DATA+3] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.MONTH) + 1), 16);
        ret[OFFSET_DATA+4] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.DAY_OF_MONTH)), 16);
        ret[OFFSET_DATA+5] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.HOUR_OF_DAY)), 16);
        ret[OFFSET_DATA+6] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.MINUTE)), 16);
        ret[OFFSET_DATA+7] = (byte)Integer.parseInt(String.valueOf(c.get(Calendar.SECOND)), 16);

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
