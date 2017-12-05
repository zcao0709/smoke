package com.zhongyun.smoke.service;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.dao.mysql.ProjectRepository;
import com.zhongyun.smoke.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by caozhennan on 2017/11/26.
 */
@Service
public class ProjectService {
    @Autowired
    public ProjectRepository repository;

    public Project add(Project project) {
        project.setMtime(new Timestamp(System.currentTimeMillis()));
        project.setCtime(new Timestamp(System.currentTimeMillis()));
        return repository.save(project);
    }

    public void delete(long id) {
        repository.delete(id);
    }

    public Project update(Project project) {
        project.setMtime(new Timestamp(System.currentTimeMillis()));
        return repository.save(project);
    }

    public Project find(long id) {
        return repository.findOne(id);
    }

    public Page<Project> find(String partName, Pageable pageable) {
        return repository.findByNameLike(Util.like(partName), pageable);
    }
}
