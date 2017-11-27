package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.model.UserProject;
import com.zhongyun.smoke.service.UserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by caozhennan on 2017/11/26.
 */
@RestController
@RequestMapping("/api/up/v1")
public class UserProjectController {
    @Autowired
    private UserProjectService service;

    @RequestMapping(method = RequestMethod.POST)
    public Resp<UserProject> post(@RequestBody UserProject up) {
        return new Resp(service.add(up));
    }

    @RequestMapping(value = "/{userId}/{projectId}", method = RequestMethod.DELETE)
    public Resp<String> delete(@PathVariable long userId, @PathVariable long projectId) {
        service.delete(userId, projectId);
        return new Resp("");
    }
}
