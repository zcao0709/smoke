package com.zhongyun.smoke.service;

import static com.zhongyun.smoke.common.Util.*;
import com.zhongyun.smoke.dao.mysql.ProjectRepository;
import com.zhongyun.smoke.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public SensorService sensorService;

    public Project add(Project project) {
        validate(project);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        project.setMtime(ts);
        project.setCtime(ts);
        return repository.save(project);
    }

    public void delete(long id) {
        repository.delete(id);
    }

    @Transactional
    public Project update(Project project) {
        if (project.getId() == 0) {
            throw new IllegalArgumentException("no project id");
        }
        validate(project);
        repository.updateById(project.getName(), project.getProvince(), project.getCity(), project.getDistrict(), project.getAddress(),
                              project.getRoomCount(), project.getId());
        return project;
    }

    public Project find(long id) {
        Project p = repository.findOne(id);
        if (p != null) {
            p.setSensorCount(sensorService.countByProjectId(id));
        }
        return p;
    }

    public Page<Project> find(String name, String province, String city, String district, String address, Pageable pageable) {
        Page<Project> pages = repository.findByNameLikeAndProvinceLikeAndCityLikeAndDistrictLikeAndAddressLike(like(name), like(province), like(city),
                                                                                                               like(district), like(address), pageable);
        pages.getContent().forEach(v -> v.setSensorCount(sensorService.countByProjectId(v.getId())));
        return pages;
    }

    private void validate(Project project) {
        if (StringUtils.isEmpty(project.getName()) || StringUtils.isEmpty(project.getProvince()) || StringUtils.isEmpty(project.getCity())
            || StringUtils.isEmpty(project.getDistrict()) || StringUtils.isEmpty(project.getAddress()) || project.getRoomCount() <= 0) {
            throw new IllegalArgumentException("not enough project info");
        }
    }
}
