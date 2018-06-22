package com.zhongyun.smoke.model.nbiot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import static com.zhongyun.smoke.common.Util.*;
import com.zhongyun.smoke.model.Sensor;

import java.sql.Timestamp;

/**
 * Created by caozhennan on 2018/6/2.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusMsg implements NbiotMsg {
    private String deviceId;
    private String imei;
    private String msgType;
    private int smoke;
    private int battery;
    private String msgTime;

    public StatusMsg() {
    }

    @Override
    public String toString() {
        return object2Json(this);
    }

    @Override
    public String getId() {
        return getDeviceId();
    }

    public String state() {
        switch (getMsgType()) {
            case "fire":
                return SENSOR_FIRE;
            case "fault":
            case "sampleerror":
            case "initerror":
                return SENSOR_FAULT;
            case "lowpower":
                return SENSOR_BATTERY;
            case "test":
                return SENSOR_TEST;
            default:
                return SENSOR_UNKNOWN;
        }
    }

    public Sensor toSensor() {
        return new Sensor(getDeviceId(), SENSOR_SMOKE, VENDOR_NBIOT, new Timestamp(System.currentTimeMillis()),
                          SENSOR_NORMAL, GATEWAY_UNSET, PROJECT_UNSET);
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
