package com.zhongyun.smoke.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by caozhennan on 2017/11/26.
 */
public class Resp<T> {
    private HttpStatus code;
    private String msg;
    private T data;

    public Resp() {
    }

    public Resp(T data) {
        this.code = HttpStatus.OK;
        this.msg = "OK";
        this.data = data;
    }

    public Resp(HttpStatus code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public static ResponseEntity ok(Object data) {
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    public static ResponseEntity bad(Object data) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
    }

    public static ResponseEntity not(Object data) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
    }

    public static ResponseEntity ser(Object data) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
    }

    public HttpStatus getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}
