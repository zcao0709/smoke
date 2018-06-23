package com.zhongyun.smoke.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhongyun.smoke.common.Util;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by caozhennan on 2017/11/30.
 */
@Entity
@Table(name = "sensor")
public class Sensor {
    private static final String MODEL_SMOKE = "GS517L";
    private static final String MODEL_GWRX = "GDOx01";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("eui10")
    private long eui;

    @JsonProperty("eui")
    private String eui16;

    private String model;
    private String type;
    private int vendor;
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

    private String phone = "";
    private Timestamp mtime;
    private Timestamp ctime;

    @Transient
    private long opCount;

    public Sensor() {
    }

    public Sensor(long eui, String type, int vendor, Timestamp installTime, String status, long gatewayId, long projectId) {
        this.eui = eui;
        this.eui16 = String.format("%X", eui);
        this.type = type;
        this.vendor = vendor;
        this.installTime = installTime;
        this.status = status;
        this.gatewayId = gatewayId;
        if (type.equals(Util.SENSOR_SMOKE)) {
            this.model = MODEL_SMOKE;
        } else {
            this.model = MODEL_GWRX;
        }
        this.projectId = projectId;
    }

    public Sensor(String eui16, String type, int vendor, Timestamp installTime, String status, String model,
                  long gatewayId, long projectId) {
        this.eui16 = eui16;
        try {
            this.eui = Long.parseLong(this.eui16);
        } catch (Exception e) {
            this.eui = System.nanoTime();
        }
        this.type = type;
        this.vendor = vendor;
        this.installTime = installTime;
        this.status = status;
        this.model = model;
        this.gatewayId = gatewayId;
        this.projectId = projectId;
    }

    public Sensor(long id, long eui, String eui16, String model, String type, String location, String lati, String longi, Timestamp installTime,
                  String guarantee, String status, long projectId, long gatewayId, String phone, Timestamp mtime, Timestamp ctime, long opCount) {
        this.id = id;
        this.eui = eui;
        this.eui16 = eui16;
        this.model = model;
        this.type = type;
        this.location = location;
        this.lati = lati;
        this.longi = longi;
        this.installTime = installTime;
        this.guarantee = guarantee;
        this.status = status;
        this.projectId = projectId;
        this.gatewayId = gatewayId;
        this.phone = phone;
        this.mtime = mtime;
        this.ctime = ctime;
        this.opCount = opCount;
    }

    public void setVendor(int vendor) {
        this.vendor = vendor;
    }

    public int getVendor() {
        return vendor;
    }

    @Override
    public String toString() {
        return type + eui16;
    }

    public void setEui16(String eui16) {
        this.eui16 = eui16;
    }

    public String getEui16() {
        return eui16;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setOpCount(long opCount) {
        this.opCount = opCount;
    }

    public long getOpCount() {
        return opCount;
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
