package com.zhongyun.smoke.service;

import com.zhongyun.smoke.dao.mysql.UserProjectRepository;
import com.zhongyun.smoke.model.UserProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by caozhennan on 2017/11/26.
 */
@Service
public class UserProjectService {
    @Autowired
    private UserProjectRepository repository;

    public UserProject add(UserProject up) {
        up.setMtime(new Timestamp(System.currentTimeMillis()));
        return repository.save(up);
    }

    public void delete(long userId, long projectId) {
        repository.deleteByUserIdAndProjectId(userId, projectId);
    }
}
