package com.zhongyun.smoke.model.nbiot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by caozhennan on 2018/6/2.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivateMsg {
    private String deviceId;
    private String imei;
    private String msgType;
    private int resultCode;
    private String msgTime;

    public ActivateMsg() {
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

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
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

    public int getResultCode() {
        return resultCode;
    }

    public String getMsgTime() {
        return msgTime;
    }
}
