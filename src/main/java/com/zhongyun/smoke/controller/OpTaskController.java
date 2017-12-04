package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.OpTask;
import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.service.OpTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/**
 * Created by caozhennan on 2017/12/1.
 */
@RestController
@RequestMapping("/api/op/v1")
public class OpTaskController {
    @Autowired
    private OpTaskService service;

    @RequestMapping(method = RequestMethod.POST)
    public Resp<OpTask> post(@RequestBody OpTask opTask) {
        return new Resp(service.add(opTask));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Resp<String> delete(@PathVariable long id) {
        service.delete(id);
        return new Resp("");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Resp<OpTask> update(@RequestBody OpTask opTask) {
        return new Resp(service.update(opTask));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resp<OpTask> find(@PathVariable long id) {
        return new Resp(service.find(id).beforeReturn());
    }

    @RequestMapping(method = RequestMethod.GET)
    public Resp<Page<OpTask>> findAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        Page<OpTask> ots = service.findAll(new PageRequest(page - 1, limit, Sort.Direction.DESC, "status", "mtime"));
        ots.getContent().forEach(v -> v.beforeReturn());
        return new Resp(ots);
    }
}
