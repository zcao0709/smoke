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
    private String lati;
    private String longi;

    private Timestamp mtime;
    private Timestamp ctime;

    public Gateway() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEui(long eui) {
        this.eui = eui;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public void setLongi(String longi) {
        this.longi = longi;
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

    public String getLati() {
        return lati;
    }

    public String getLongi() {
        return longi;
    }

    public Timestamp getMtime() {
        return mtime;
    }

    public Timestamp getCtime() {
        return ctime;
    }
}
