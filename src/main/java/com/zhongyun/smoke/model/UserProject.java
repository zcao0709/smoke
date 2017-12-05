package com.zhongyun.smoke.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by caozhennan on 2017/11/26.
 */
@Entity
@Table(name = "user_project")
public class UserProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "project_id")
    private long projectId;

    private Timestamp mtime;
    private Timestamp ctime;

    public UserProject() {
    }

//    public void setUser(User user) {
//        this.user = user;
//    }

//    public void setProject(Project project) {
//        this.project = project;
//    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public long getUserId() {
        return userId;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
//    public User getUser() {
//        return user;
//    }

//    public Project getProject() {
//        return project;
//    }
}
