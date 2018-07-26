package com.zhongyun.smoke.model.monitor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by caozhennan on 2018/7/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Upload {
    private String ownercode;
    private int istest;
    private String happentime;
    private int alarmtype;
    private String alarmsourcedesc;
    private String alarmdevicedesc;
    private String alarmwheredesc;
    private String alarmreserve;
    private String uploadtime;

    public Upload(String ownercode, int istest, String happentime, int alarmtype, String alarmsourcedesc,
                  String alarmdevicedesc, String alarmwheredesc, String alarmreserve, String uploadtime) {
        this.ownercode = ownercode;
        this.istest = istest;
        this.happentime = happentime;
        this.alarmtype = alarmtype;
        this.alarmsourcedesc = alarmsourcedesc;
        this.alarmdevicedesc = alarmdevicedesc;
        this.alarmwheredesc = alarmwheredesc;
        this.alarmreserve = alarmreserve;
        this.uploadtime = uploadtime;
    }

    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    public void setIstest(int istest) {
        this.istest = istest;
    }

    public void setHappentime(String happentime) {
        this.happentime = happentime;
    }

    public void setAlarmtype(int alarmtype) {
        this.alarmtype = alarmtype;
    }

    public void setAlarmsourcedesc(String alarmsourcedesc) {
        this.alarmsourcedesc = alarmsourcedesc;
    }

    public void setAlarmdevicedesc(String alarmdevicedesc) {
        this.alarmdevicedesc = alarmdevicedesc;
    }

    public void setAlarmwheredesc(String alarmwheredesc) {
        this.alarmwheredesc = alarmwheredesc;
    }

    public void setAlarmreserve(String alarmreserve) {
        this.alarmreserve = alarmreserve;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getOwnercode() {
        return ownercode;
    }

    public int getIstest() {
        return istest;
    }

    public String getHappentime() {
        return happentime;
    }

    public int getAlarmtype() {
        return alarmtype;
    }

    public String getAlarmsourcedesc() {
        return alarmsourcedesc;
    }

    public String getAlarmdevicedesc() {
        return alarmdevicedesc;
    }

    public String getAlarmwheredesc() {
        return alarmwheredesc;
    }

    public String getAlarmreserve() {
        return alarmreserve;
    }

    public String getUploadtime() {
        return uploadtime;
    }
}
