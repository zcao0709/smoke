package com.zhongyun.smoke.model.nbiot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import static com.zhongyun.smoke.common.Util.*;
import com.zhongyun.smoke.model.Sensor;

import java.sql.Timestamp;
import java.util.Map;

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

    private static String[] def = {""};
    private static final String model = "ARN001";

    public StatusMsg() {
    }

    public StatusMsg(String deviceId, String imei, String msgType, String smoke, String battery, String msgTime) {
        this.deviceId = deviceId;
        this.imei = imei;
        this.msgType = msgType;
        this.msgTime = msgTime;
        try {
            this.smoke = Integer.parseInt(smoke);
            this.battery = Integer.parseInt(battery);
        } catch (Exception e) {
            this.smoke = 0;
            this.battery = 0;
        }
    }

    public StatusMsg(Map<String, String[]> map) {
        this(map.getOrDefault("deviceId", def)[0], map.getOrDefault("imei", def)[0], map.getOrDefault("msgType", def)[0],
             map.getOrDefault("smoke", def)[0], map.getOrDefault("battery", def)[0], map.getOrDefault("msgTime", def)[0]);
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

    @Override
    public Sensor toGateway() {
        return null;
    }

    @Override
    public Sensor toSensor() {
        return new Sensor(getDeviceId(), SENSOR_SMOKE, VENDOR_ORENA, new Timestamp(System.currentTimeMillis()),
                          SENSOR_NORMAL, model, GATEWAY_UNSET, PROJECT_UNSET);
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
