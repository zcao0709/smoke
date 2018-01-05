package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.model.User;
import com.zhongyun.smoke.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by caozhennan on 2017/11/26.
 */
@RestController
@RequestMapping("/api/user/v1")
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger("UserController");

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> post(@RequestBody User user) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        return Resp.ok(service.add(user));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable long id) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        service.delete(id);
        return Resp.ok(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<User> update(@RequestBody User user) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        return Resp.ok(service.update(user));
    }

    @RequestMapping(value = "/logined", method = RequestMethod.GET)
    public ResponseEntity<User> find() {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("username in cookie: " + name);

        User u = service.findByName(name);
        if (u == null) {
            return Resp.ser("无此用户: " + name);
        }
        logger.info("user: " + u.getName() + ", " + u.getFullname());
        return Resp.ok(u);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> find(@PathVariable long id) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        User u = service.findById(id);
        if (u == null) {
            return Resp.not();
        }
        return Resp.ok(u);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> findLike(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        Page<User> p = service.findLike(name, fullname, type, page, limit);
        return Util.resp(p.getContent(), p.getTotal());
    }
}
