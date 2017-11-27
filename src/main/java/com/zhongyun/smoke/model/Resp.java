package com.zhongyun.smoke.model;

/**
 * Created by caozhennan on 2017/11/26.
 */
public class Resp<T> {
    private int code;
    private String msg;
    private T data;

    public Resp() {
    }

    public Resp(T data) {
        this.code = 200;
        this.msg = "OK";
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}
