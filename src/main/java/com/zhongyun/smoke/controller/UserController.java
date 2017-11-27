package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.model.User;
import com.zhongyun.smoke.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by caozhennan on 2017/11/26.
 */
@RestController
@RequestMapping("/api/user/v1")
public class UserController {
    @Autowired
    private UserService service;

    private static final Logger logger = LoggerFactory.getLogger("UserController");

    @RequestMapping(method = RequestMethod.POST)
    public Resp<User> post(@RequestBody User user) {
        return new Resp(service.add(user));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Resp<String> delete(@PathVariable long id) {
        service.delete(id);
        return new Resp("");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Resp<User> update(@RequestBody User user) {
        return new Resp(service.update(user));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resp<User> find(@PathVariable long id) {
        User u = service.find(id);
        u.beforeReturn();
        logger.info(u.toString());
        return new Resp(u);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Resp<Page<User>> find(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        Page<User> users = service.find(name, new PageRequest(page - 1, limit, Sort.Direction.DESC, "mtime"));
        for (User u : users.getContent()) {
            u.beforeReturn();
        }
        return new Resp(users);
    }
}
