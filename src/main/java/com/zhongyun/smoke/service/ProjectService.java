package com.zhongyun.smoke.service;

import static com.zhongyun.smoke.common.Util.*;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.dao.mysql.ProjectRepository;
import com.zhongyun.smoke.dao.mysql.SensorRepository;
import com.zhongyun.smoke.dao.mysql.UserProjectRepository;
import com.zhongyun.smoke.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;

/**
 * Created by caozhennan on 2017/11/26.
 */
@Service
public class ProjectService {
    @Autowired
    public ProjectRepository repository;

    @Autowired
    public SensorRepository sensorRepository;

    @Autowired
    public UserProjectRepository upRepository;

    public Project add(Project project) {
        validate(project);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        project.setMtime(ts);
        project.setCtime(ts);
        return repository.save(project);
    }

    @Transactional
    public void delete(long id) {
        repository.delete(id);
        sensorRepository.clearProjectId(id);
        upRepository.deleteByProjectId(id);
    }

    @Transactional
    public Project update(Project project) {
        if (project.getId() == 0) {
            throw new IllegalArgumentException(ERR_INVALID_ID);
        }
        validate(project);
        repository.updateById(project.getName(), project.getProvince(), project.getCity(), project.getDistrict(), project.getAddress(),
                              project.getRoomCount(), project.getPhone(), project.getId());
        return project;
    }

    @Transactional
    public void updateGraph(long projectId, String path) {
        repository.updateGraphById(path, projectId);
    }

    public Project findById(long id) {
        return repository.findById(id);
    }

    public Page<Project> findLike(String name, String province, String city, String district, String address, String phone, int page, int limit) {
        return repository.findLike(
                like(name), like(province), like(city), like(district), like(address), like(phone), page, limit);
    }

    private void validate(Project project) {
        if (StringUtils.isEmpty(project.getName()) || StringUtils.isEmpty(project.getProvince()) || StringUtils.isEmpty(project.getCity())
            || StringUtils.isEmpty(project.getDistrict()) || StringUtils.isEmpty(project.getAddress()) || project.getRoomCount() <= 0) {
            throw new IllegalArgumentException(ERR_NOT_ENOUGH);
        }
    }
}
