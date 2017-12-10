package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.model.UserProject;
import com.zhongyun.smoke.service.UserProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by caozhennan on 2017/11/26.
 */
@RestController
@RequestMapping("/api/up/v1")
public class UserProjectController {
    @Autowired
    private UserProjectService service;

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger("UserProjectController");

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserProject> post(@RequestBody UserProject up) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        return Resp.ok(service.add(up));
    }

    @RequestMapping(value = "/{userId}/{projectId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable long userId, @PathVariable long projectId) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        service.delete(userId, projectId);
        return Resp.ok(userId + ", " + projectId);
    }
}
