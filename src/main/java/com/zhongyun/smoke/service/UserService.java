package com.zhongyun.smoke.service;

import static com.zhongyun.smoke.common.Util.*;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.dao.mysql.UserProjectRepository;
import com.zhongyun.smoke.dao.mysql.UserRepository;
import com.zhongyun.smoke.model.User;
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
public class UserService {
    @Autowired
    public UserRepository repository;

    @Autowired
    public UserProjectRepository upRepository;

    public User add(User user) {
        if (StringUtils.isEmpty(user.getPass())) {
            throw new IllegalArgumentException("no user password");
        }
        user.setPass(Util.ENCODER.encode(user.getPass()));
        validate(user);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        user.setMtime(ts);
        user.setCtime(ts);
        return repository.save(user);
    }

    public void delete(long id) {
        if (id == 1) {
            throw new IllegalArgumentException("cannot remove system administrator");
        }
        repository.delete(id);
        upRepository.deleteByUserId(id);
    }

    @Transactional
    public User update(User user) {
        if (user.getId() == 0) {
            throw new IllegalArgumentException("no user id");
        }
        validate(user);
        if (user.getPass().length() == 0) {
            repository.updateById(user.getName(), user.getFullname(), user.getType(), user.getId());
        } else {
            user.setPass(Util.ENCODER.encode(user.getPass()));
            repository.updateByIdWithPass(user.getName(), user.getPass(), user.getFullname(), user.getType(), user.getId());
        }
        return user;
    }

    public User find(long id) {
        return repository.findOne(id);
    }

    public User find(String name) {
        return repository.findByName(name);
    }

    public Page<User> find(String name, String fullname, String type, Pageable pageable) {
        return repository.findByNameLikeAndFullnameLikeAndTypeLike(like(name), like(fullname), like(type), pageable);
    }

    private void validate(User user) {

        if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getType())) {
            throw new IllegalArgumentException("not enough user info");
        }
    }
}
