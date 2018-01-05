package com.zhongyun.smoke.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * Created by caozhennan on 2017/11/26.
 */
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String fullname;
    private String pass;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_project", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"))
    @Transient
    private List<Project> projects;

    private String type;
    private Timestamp mtime;
    private Timestamp ctime;

    public User() {
    }

    public User(long id, String name, String fullname, String type, Timestamp mtime, Timestamp ctime) {
        this.id = id;
        this.name = name;
        this.fullname = fullname;
        this.type = type;
        this.mtime = mtime;
        this.ctime = ctime;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public Timestamp getMtime() {
        return mtime;
    }

    public void setMtime(Timestamp mtime) {
        this.mtime = mtime;
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

    public String getPass() {
        return pass;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public String getType() {
        return type;
    }

    public Timestamp getCtime() {
        return ctime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCtime(Timestamp ctime) {
        this.ctime = ctime;
    }
}
