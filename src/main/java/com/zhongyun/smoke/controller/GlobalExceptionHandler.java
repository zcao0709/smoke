package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Resp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Created by caozhennan on 2017/12/8.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> sqlHandler(HttpServletRequest request, SQLException ex) {
        return Resp.ser("database error: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> argumentHandler(HttpServletRequest request, Exception ex) {
        return Resp.bad(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> gloablHandler(HttpServletRequest request, Exception ex) {
        return Resp.ser(ex.getMessage());
    }
}
