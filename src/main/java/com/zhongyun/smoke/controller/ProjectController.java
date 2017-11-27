package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.model.Project;
import com.zhongyun.smoke.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/**
 * Created by caozhennan on 2017/11/26.
 */
@RestController
@RequestMapping("/api/project/v1")
public class ProjectController {
    @Autowired
    private ProjectService service;

    @RequestMapping(method = RequestMethod.POST)
    public Resp<Project> post(@RequestBody Project project) {
        return new Resp(service.add(project));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Resp<String> delete(@PathVariable long id) {
        service.delete(id);
        return new Resp("");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Resp<Project> update(@RequestBody Project project) {
        return new Resp(service.update(project));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resp<Project> find(@PathVariable long id) {
        return new Resp(service.find(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Resp<Page<Project>> find(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return new Resp(service.find(name, new PageRequest(page - 1, limit, Sort.Direction.DESC, "mtime")));
    }
}
