package com.zhongyun.smoke.model.monitor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by caozhennan on 2018/7/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resp {
    @JsonProperty("Code")
    private int code;
    @JsonProperty("Msg")
    private String msg;
    @JsonProperty("Data")
    private String data;
    @JsonProperty("Extra")
    private String extra;

    public Resp() {
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getData() {
        return data;
    }

    public String getExtra() {
        return extra;
    }
}
