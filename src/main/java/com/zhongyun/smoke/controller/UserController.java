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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return new Resp(service.find(id).beforeReturn());
    }

    @RequestMapping(value = "/like", method = RequestMethod.GET)
    public ResponseEntity<Resp<List<User>>> find(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone1", required = false) String phone1,
            @RequestParam(value = "phone2", required = false) String phone2,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {
        Page<User> pages = service.find(name, phone1, phone2, type, new PageRequest(page - 1, limit, Sort.Direction.DESC, "mtime"));
        List<User> users = pages .getContent();
        users.forEach(v -> v.beforeReturn());

        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(pages.getTotalElements()));
        return new ResponseEntity<Resp<List<User>>>(new Resp(users), hs, HttpStatus.OK);
    }
}
