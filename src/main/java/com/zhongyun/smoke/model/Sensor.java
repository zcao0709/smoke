package com.zhongyun.smoke.model;

import com.zhongyun.smoke.common.Util;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by caozhennan on 2017/11/30.
 */
@Entity
@Table(name = "sensor")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long eui;
    private String model = "";
    private String type;
    private String room = "";
    private String location = "";

    private String lati = "0";
    private String longi = "0";

    @Column(name = "install_time")
    private Timestamp installTime;

    private String guarantee = "在保";
    private String status = Util.SENSOR_NORMAL;

    @Column(name = "project_id")
    private long projectId = 0;

    @Column(name = "gateway_id")
    private long gatewayId;  // refer to sensor.id

    private Timestamp mtime;
    private Timestamp ctime;

    public Sensor() {
    }

    public Sensor(long eui, String type, Timestamp installTime, String status, long gatewayId) {
        this.eui = eui;
        this.type = type;
        this.installTime = installTime;
        this.status = status;
        this.gatewayId = gatewayId;
    }

    public long getId() {
        return id;
    }

    public void setGatewayId(long gatewayId) {
        this.gatewayId = gatewayId;
    }

    public long getGatewayId() {
        return gatewayId;
    }

    public long getEui() {
        return eui;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getLati() {
        return lati;
    }

    public String getLongi() {
        return longi;
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
}
