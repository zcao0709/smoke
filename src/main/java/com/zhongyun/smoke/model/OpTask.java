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

    private long eid;
    @Column(name = "post_user")
    private long postUser;

    @OneToOne
    @JoinColumn(name = "id")
    private User poster;

    @Column(name = "post_time")
    private Timestamp postTime;

    @Column(name = "op_user")
    private long opUser;

    @OneToOne
    @JoinColumn(name = "id")
    private User op;

    @Column(name = "op_time")
    private Timestamp opTime;

    private String cause;
    private String handler;
    private String status;

    private Timestamp mtime;
    private Timestamp ctime;

    public OpTask() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEid() {
        return eid;
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

    public void setEid(long eid) {
        this.eid = eid;
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
