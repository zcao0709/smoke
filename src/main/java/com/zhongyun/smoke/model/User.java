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
    private String pass;
    private String phone1;
    private String phone2;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_project", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"))
    private List<Project> projects;

    private String type;
    private Timestamp mtime;
    private Timestamp ctime;

    @Override
    public String toString() {
        return name + "/" + Arrays.toString(projects.toArray(new Project[0]));
    }

    public User beforeReturn() {
        if (projects == null) {
            return this;
        }
        for (Project p : projects) {
            p.setUsers(null);
        }
        return this;
    }

    public User() {
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

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
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

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
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
