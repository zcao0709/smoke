package com.zhongyun.smoke.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * Created by caozhennan on 2017/11/26.
 */
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String province;
    private String city;
    private String district;
    private String address;
    @Column(name = "room_count")
    private int roomCount;

    private Timestamp mtime;
    private Timestamp ctime;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "projects")
    private List<User> users;

    @Override
    public String toString() {
        return name + "/" + Arrays.toString(users.toArray(new User[0]));
    }

    public Project beforeReturn() {
        if (users == null) {
            return this;
        }
        for (User u : users) {
            u.setProjects(null);
        }
        return this;
    }

    public Project() {
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCtime() {
        return ctime;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setMtime(Timestamp mtime) {
        this.mtime = mtime;
    }

    public Timestamp getMtime() {
        return mtime;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCtime(Timestamp ctime) {
        this.ctime = ctime;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
