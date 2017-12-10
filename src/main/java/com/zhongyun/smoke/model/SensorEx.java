package com.zhongyun.smoke.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by caozhennan on 2017/12/10.
 */
public class SensorEx {
    private long id;

    private String eui;
    private String model;
    private String type;
    private String room;
    private String location;

    private String lati;
    private String longi;

    private Timestamp installTime;

    private String guarantee;
    private String status;

    private long projectId;

    private Timestamp mtime;
    private Timestamp ctime;

    private long opCount;

    public static SensorEx valueOf(Sensor s) {
        if (s == null) {
            return null;
        }
        return new SensorEx(s.getId(), String.format("%X", s.getEui()), s.getModel(), s.getType(), s.getRoom(), s.getLocation(), s.getLati(),
                            s.getLongi(), s.getInstallTime(), s.getGuarantee(), s.getStatus(), s.getProjectId(), s.getMtime(), s.getCtime(),
                            s.getOpCount());
    }

    public static List<SensorEx> valueOf(List<Sensor> sensors) {
        if (sensors == null) {
            return null;
        }
        return sensors.stream().map(v -> SensorEx.valueOf(v)).collect(Collectors.toList());
    }

    public SensorEx(long id, String eui, String model, String type, String room, String location, String lati, String longi,
                    Timestamp installTime, String guarantee, String status, long projectId, Timestamp mtime, Timestamp ctime, long opCount) {
        this.id = id;
        this.eui = eui;
        this.model = model;
        this.type = type;
        this.room = room;
        this.location = location;
        this.lati = lati;
        this.longi = longi;
        this.installTime = installTime;
        this.guarantee = guarantee;
        this.status = status;
        this.projectId = projectId;
        this.mtime = mtime;
        this.ctime = ctime;
        this.opCount = opCount;
    }

    public Sensor toSensor() {
        return new Sensor(id, model, room, location, guarantee, status, projectId);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEui(String eui) {
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

    public void setLati(String lati) {
        this.lati = lati;
    }

    public void setLongi(String longi) {
        this.longi = longi;
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

    public void setOpCount(long opCount) {
        this.opCount = opCount;
    }

    public long getId() {
        return id;
    }

    public String getEui() {
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

    public String getLati() {
        return lati;
    }

    public String getLongi() {
        return longi;
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

    public long getOpCount() {
        return opCount;
    }
}
