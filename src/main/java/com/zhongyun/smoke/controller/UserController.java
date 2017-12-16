package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.model.User;
import com.zhongyun.smoke.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    @RequestMapping(method = RequestMethod.POST)
//    public String post() {
//        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());
//        return "login success";
//    }

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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> find(@PathVariable long id) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        User u = service.find(id);
        if (u == null) {
            return Resp.not(id);
        }
        return Resp.ok(u.beforeReturn());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> find(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        Page<User> pages = service.find(name, fullname, type, new PageRequest(page - 1, limit, Sort.Direction.DESC, "mtime"));
        List<User> users = pages.getContent();
        users.forEach(v -> v.beforeReturn());

        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(pages.getTotalElements()));
        return new ResponseEntity<>(users, hs, HttpStatus.OK);
    }
}
