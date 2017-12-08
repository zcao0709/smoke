package com.zhongyun.smoke.service;

import com.zhongyun.smoke.dao.mysql.UserProjectRepository;
import com.zhongyun.smoke.model.UserProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;

/**
 * Created by caozhennan on 2017/11/26.
 */
@Service
public class UserProjectService {
    @Autowired
    private UserProjectRepository repository;

    public UserProject add(UserProject up) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        up.setMtime(ts);
        up.setCtime(ts);
        return repository.save(up);
    }

    @Transactional
    public void delete(long userId, long projectId) {
        repository.deleteByUserIdAndProjectId(userId, projectId);
    }
}
