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
    private String msgTime;
    private String productModel;
    private String productType;
    private String productVersion;
    private int fireStatus;
    private int faultStatus;
    private int lowpowerStatus;
    private int smoke;
    private int battery;

    private static final String[] def = {""};
//    private static final String model = "ARN001";

    public StatusMsg() {
    }

    public StatusMsg(String deviceId, String imei, String msgType, String msgTime, String productModel,
                     String productType, String productVersion, String fireStatus, String faultStatus,
                     String lowpowerStatus, String smoke, String battery) {
        this.deviceId = deviceId;
        this.imei = imei;
        this.msgType = msgType;
        this.msgTime = msgTime;
        this.productModel = productModel;
        this.productType = productType;
        this.productVersion = productVersion;
        this.fireStatus = atoi(fireStatus);
        this.faultStatus = atoi(faultStatus);
        this.lowpowerStatus = atoi(lowpowerStatus);
        this.smoke = atoi(smoke);
        this.battery = atoi(battery);
    }

    private int atoi(String s) {
        try {
            return s == null || s.length() == 0 ? 0 : Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public StatusMsg(Map<String, String[]> map) {
        this(map.getOrDefault("deviceId", def)[0], map.getOrDefault("imei", def)[0],
             map.getOrDefault("msgType", def)[0], map.getOrDefault("msgTime", def)[0],
             map.getOrDefault("productModel", def)[0], map.getOrDefault("productType", def)[0],
             map.getOrDefault("productVersion", def)[0], map.getOrDefault("fireStatus", def)[0],
             map.getOrDefault("faultStatus", def)[0], map.getOrDefault("lowpowerStatus", def)[0],
             map.getOrDefault("smoke", def)[0], map.getOrDefault("battery", def)[0]);
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
            case "actived": // ignore this
                return null;
            case "start":
                return SENSOR_NORMAL;
            case "status":
                if (getFireStatus() == 1) {
                    return SENSOR_FIRE;
                } else if (getFaultStatus() == 1) {
                    return SENSOR_FAULT;
                } else if (getLowpowerStatus() == 1) {
                    return SENSOR_BATTERY;
                }
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
                          SENSOR_NORMAL, getProductModel(), GATEWAY_UNSET, PROJECT_UNSET);
    }

    @Override
    public boolean valid() {
        String s = state();
        return s != null && s.length() > 0;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public void setFireStatus(int fireStatus) {
        this.fireStatus = fireStatus;
    }

    public void setFaultStatus(int faultStatus) {
        this.faultStatus = faultStatus;
    }

    public void setLowpowerStatus(int lowpowerStatus) {
        this.lowpowerStatus = lowpowerStatus;
    }

    public String getProductModel() {
        return productModel;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public int getFireStatus() {
        return fireStatus;
    }

    public int getFaultStatus() {
        return faultStatus;
    }

    public int getLowpowerStatus() {
        return lowpowerStatus;
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
