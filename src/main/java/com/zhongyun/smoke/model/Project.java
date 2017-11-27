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
    private Timestamp mtime;
    private Timestamp ctime;
    private String province;
    private String city;
    private String address;
    private String area;
    @Column(name = "room_count")
    private int roomCount;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "projects")
    private List<User> users;

    @Override
    public String toString() {
        return name + "/" + Arrays.toString(users.toArray(new User[0]));
    }

    public void beforeReturn() {
        if (users == null) {
            return;
        }
        for (User u : users) {
            u.setProjects(null);
        }
    }

    public Project() {
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

    public String getArea() {
        return area;
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

    public void setArea(String area) {
        this.area = area;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
