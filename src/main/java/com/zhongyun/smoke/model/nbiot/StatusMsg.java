package com.zhongyun.smoke.model.nbiot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by caozhennan on 2018/6/2.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusMsg {
    private String deviceId;
    private String imei;
    private String msgType;
    private int smoke;
    private int battery;
    private String msgTime;

    public StatusMsg() {
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setSmoke(int smoke) {
        this.smoke = smoke;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getImei() {
        return imei;
    }

    public String getMsgType() {
        return msgType;
    }

    public int getSmoke() {
        return smoke;
    }

    public int getBattery() {
        return battery;
    }

    public String getMsgTime() {
        return msgTime;
    }
}
