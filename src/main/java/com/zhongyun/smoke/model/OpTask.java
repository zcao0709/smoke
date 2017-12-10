package com.zhongyun.smoke.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by caozhennan on 2017/11/30.
 */
@Entity
@Table(name = "op_task")
public class OpTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long eui;
    @Column(name = "post_user")
    private long postUser;

    @Column(name = "post_time")
    private Timestamp postTime;

    @Column(name = "op_user")
    private long opUser;

    @Column(name = "op_time")
    private Timestamp opTime;

    private String cause;
    private String handler = "";
    private String worker = "";
    private String status = "待处理";

    @Column(name = "project_id")
    private long projectId = 0;

    private Timestamp mtime;
    private Timestamp ctime;

    @Transient
    private Sensor sensor;
    @Transient
    private User op;
    @Transient
    private User poster;
    @Transient
    private boolean expired;
    @Transient
    private Project project;

    public OpTask() {
    }

    public OpTask(long eui, long postUser, Timestamp postTime, String cause, String status, long projectId) {
        this.eui = eui;
        this.postUser = postUser;
        this.postTime = postTime;
        this.cause = cause;
        this.status = status;
        this.projectId = projectId;
    }

    public OpTask(long id, long eui, long postUser, Timestamp postTime, long opUser, Timestamp opTime, String cause, String handler, String worker,
                  String status, long projectId, Timestamp mtime, Timestamp ctime) {
        this.eui = eui;
        this.postUser = postUser;
        this.postTime = postTime;
        this.opUser = opUser;
        this.opTime = opTime;
        this.cause = cause;
        this.handler = handler;
        this.worker = worker;
        this.status = status;
        this.projectId = projectId;
        this.mtime = mtime;
        this.ctime = ctime;
    }

    public OpTask(long id, long opUser, String handler, String worker, String status) {
        this.id = id;
        this.opUser = opUser;
        this.handler = handler;
        this.worker = worker;
        this.status = status;
    }

    public OpTask beforeReturn() {
        if (op != null) {
            op.setProjects(null);
        }
        if (poster != null) {
            poster.setProjects(null);
        }
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
//    public void setProjectName(String projectName) {
//        this.projectName = projectName;
//    }

//    public String getProjectName() {
//        return projectName;
//    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getWorker() {
        return worker;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setOp(User op) {
        this.op = op;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public User getOp() {
        return op;
    }

    public User getPoster() {
        return poster;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEui() {
        return eui;
    }

    public long getPostUser() {
        return postUser;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public long getOpUser() {
        return opUser;
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

    public String getStatus() {
        return status;
    }

    public Timestamp getMtime() {
        return mtime;
    }

    public Timestamp getCtime() {
        return ctime;
    }

    public void setEui(long eui) {
        this.eui = eui;
    }

    public void setPostUser(long postUser) {
        this.postUser = postUser;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public void setOpUser(long opUser) {
        this.opUser = opUser;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMtime(Timestamp mtime) {
        this.mtime = mtime;
    }

    public void setCtime(Timestamp ctime) {
        this.ctime = ctime;
    }
}
