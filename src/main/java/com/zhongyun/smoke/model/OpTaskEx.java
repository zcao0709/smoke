package com.zhongyun.smoke.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by caozhennan on 2017/12/10.
 */
public class OpTaskEx {
    private long id;

    private String eui;

    private Timestamp postTime;
    private Timestamp opTime;

    private String cause;
    private String handler = "";
    private String worker = "";
    private String status = "待处理";

    private Timestamp mtime;
    private Timestamp ctime;

    private SensorEx sensor;
    private User op;
    private User poster;
    private boolean expired;
    private String projectName;

    public static OpTaskEx valueOf(OpTask o) {
        if (o == null) {
            return null;
        }
        String projectName = "";
        if (o.getProject() != null) {
            projectName = o.getProject().getName();
        }
        return new OpTaskEx(o.getId(), String.format("%X", o.getEui()), o.getPostTime(), o.getOpTime(), o.getCause(), o.getHandler(),
                            o.getWorker(), o.getStatus(), o.getMtime(), o.getCtime(), o.getSensor(), o.getOp(), o.getPoster(), o.isExpired(),
                            projectName);
    }

    public static List<OpTaskEx> valueOf(List<OpTask> ops) {
        if (ops == null) {
            return null;
        }
        return ops.stream().map(v -> OpTaskEx.valueOf(v)).collect(Collectors.toList());
    }

    public OpTaskEx(long id, String eui, Timestamp postTime, Timestamp opTime, String cause, String handler, String worker, String status,
                    Timestamp mtime, Timestamp ctime, Sensor sensor, User op, User poster, boolean expired, String projectName) {
        this.id = id;
        this.eui = eui;
        this.postTime = postTime;
        this.opTime = opTime;
        this.cause = cause;
        this.handler = handler;
        this.worker = worker;
        this.status = status;
        this.mtime = mtime;
        this.ctime = ctime;
        this.sensor = SensorEx.valueOf(sensor);
        this.op = op;
        this.poster = poster;
        this.expired = expired;
        this.projectName = projectName;
    }

    public OpTask toOpTask() {
        return new OpTask(id, op.getId(), handler, worker, status);
    }

    public void setSensor(SensorEx sensor) {
        this.sensor = sensor;
    }

    public SensorEx getSensor() {
        return sensor;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEui(String eui) {
        this.eui = eui;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public void setOpTime(Timestamp opTime) {
        this.opTime = opTime;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMtime(Timestamp mtime) {
        this.mtime = mtime;
    }

    public void setCtime(Timestamp ctime) {
        this.ctime = ctime;
    }

    public void setOp(User op) {
        this.op = op;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getId() {
        return id;
    }

    public String getEui() {
        return eui;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public Timestamp getOpTime() {
        return opTime;
    }

    public String getCause() {
        return cause;
    }

    public String getHandler() {
        return handler;
    }

    public String getWorker() {
        return worker;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getMtime() {
        return mtime;
    }

    public Timestamp getCtime() {
        return ctime;
    }

    public User getOp() {
        return op;
    }

    public User getPoster() {
        return poster;
    }

    public boolean isExpired() {
        return expired;
    }

    public String getProjectName() {
        return projectName;
    }
}
