package com.zhongyun.smoke.model.nbiot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import static com.zhongyun.smoke.common.Util.*;
import com.zhongyun.smoke.model.Sensor;

import java.sql.Timestamp;

/**
 * Created by caozhennan on 2018/6/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SJStatusMsg implements NbiotMsg {
    private String deviceType;
    private String deviceId;
    private String deviceName;
    private String at;
    private String type;
    private String streamId;
    private String data;
    private int level;
    private String location;
    private long oid;
    private String rawData;

    private RawData raw;

    private static final String model = "FHDJ001";

    public SJStatusMsg() {
    }

    @Override
    public String toString() {
        return object2Json(this);
    }

    @Override
    @JsonIgnore
    public String getId() {
        return getDeviceId();
    }

    @Override
    public String state() {
        String msg = rawData;
        if (raw == null && rawData.startsWith("{")) {
            raw = json2Object(rawData, RawData.class);
        }
        if (raw != null) {
            msg = raw.getMsg();
        }
        if (msg.length() < 4 || !"02".equals(msg.substring(0, 2))) {
            return null;
        }
        switch (msg.substring(2, 4)) {
            case "00":
            case "04":
            case "05":
            case "06":
            case "07":
            case "0E":
                return SENSOR_NORMAL;
            case "0D":
                return SENSOR_TEST;
            case "01":
                return SENSOR_FIRE;
            case "02":
                return SENSOR_FAULT;
            case "03":
                return SENSOR_BATTERY;
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
        return new Sensor(getDeviceId(), SENSOR_SMOKE, VENDOR_SANJI, new Timestamp(System.currentTimeMillis()),
                          SENSOR_NORMAL, model, GATEWAY_UNSET, PROJECT_UNSET);
    }

    @Override
    public boolean valid() {
        String s = state();
        return s != null && s.length() > 0;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getAt() {
        return at;
    }

    public String getType() {
        return type;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getData() {
        return data;
    }

    public int getLevel() {
        return level;
    }

    public String getLocation() {
        return location;
    }

    public long getOid() {
        return oid;
    }

    public String getRawData() {
        return rawData;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RawData {
        private String msg;

        public RawData() {
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }
}
