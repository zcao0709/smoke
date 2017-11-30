package com.zhongyun.smoke.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by caozhennan on 2017/11/30.
 */
@Entity
@Table(name = "gateway")
public class Gateway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long eui;
    private String model;
    private String type;
    private String room;
    private String location;

    @Column(name = "install_time")
    private Timestamp installTime;

    private String guarantee;
    private String status;

    @Column(name = "project_id")
    private long projectId;

    private Timestamp mtime;
    private Timestamp ctime;

    @OneToMany
    @JoinColumn(name = "gateway_id")
    private List<Sensor> sensors;

    public Gateway() {
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEui(long eui) {
        this.eui = eui;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setInstallTime(Timestamp installTime) {
        this.installTime = installTime;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public void setMtime(Timestamp mtime) {
        this.mtime = mtime;
    }

    public void setCtime(Timestamp ctime) {
        this.ctime = ctime;
    }

    public long getId() {
        return id;
    }

    public long getEui() {
        return eui;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public String getRoom() {
        return room;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getInstallTime() {
        return installTime;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public String getStatus() {
        return status;
    }

    public long getProjectId() {
        return projectId;
    }

    public Timestamp getMtime() {
        return mtime;
    }

    public Timestamp getCtime() {
        return ctime;
    }
}
