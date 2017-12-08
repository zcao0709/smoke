package com.zhongyun.smoke.service;

import static com.zhongyun.smoke.common.Util.*;
import com.zhongyun.smoke.dao.mysql.UserRepository;
import com.zhongyun.smoke.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by caozhennan on 2017/11/26.
 */
@Service
public class UserService {
    @Autowired
    public UserRepository repository;

    public User add(User user) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        user.setMtime(ts);
        user.setCtime(ts);
        return repository.save(user);
    }

    public void delete(long id) {
        repository.delete(id);
    }

    public User update(User user) {
//        User u = repository.findOne(user.getId());
//        if (u == null) {
//            return null;
//        }
//        repository.delete(user.getId());
//        user.setProjects(u.getProjects());
        user.setMtime(new Timestamp(System.currentTimeMillis()));
        repository.save(user);
        return user;
    }

    public User find(long id) {
        return repository.findOne(id);
    }

    public Page<User> find(String name, String phone1, String phone2, String type, Pageable pageable) {
        return repository.findByNameLikeAndPhone1LikeAndPhone2LikeAndTypeLike(like(name), like(phone1), like(phone2), like(type), pageable);
    }
}
