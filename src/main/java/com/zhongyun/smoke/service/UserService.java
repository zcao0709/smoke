package com.zhongyun.smoke.service;

import static com.zhongyun.smoke.common.Util.*;
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

    public User add(User user) {
        if (StringUtils.isEmpty(user.getPass())) {
            throw new IllegalArgumentException("no user password");
        }
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
    }

    @Transactional
    public User update(User user) {
        if (user.getId() == 0) {
            throw new IllegalArgumentException("no user id");
        }
        validate(user);
        repository.updateById(user.getName(), user.getPhone1(), user.getPhone2(), user.getType(), user.getId());
        return user;
    }

    public User find(long id) {
        return repository.findOne(id);
    }

    public Page<User> find(String name, String phone1, String phone2, String type, Pageable pageable) {
        return repository.findByNameLikeAndPhone1LikeAndPhone2LikeAndTypeLike(like(name), like(phone1), like(phone2), like(type), pageable);
    }

    private void validate(User user) {
        if (StringUtils.isEmpty(user.getPhone1())) {
            user.setPhone1(user.getPhone2());
            user.setPhone2("");
        }
        if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPhone1()) || StringUtils.isEmpty(user.getType())) {
            throw new IllegalArgumentException("not enough user info");
        }
    }
}
