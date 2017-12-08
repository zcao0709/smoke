package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.OpTask;
import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.service.OpTaskService;
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
    public ResponseEntity<Resp<List<OpTask>>> findAll(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {
        Page<OpTask> pages = service.findAll(new PageRequest(page - 1, limit, Sort.Direction.DESC, "status", "mtime"));
        List<OpTask> ots = pages.getContent();
        ots.forEach(v -> v.beforeReturn());

        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(pages.getTotalElements()));
        return new ResponseEntity<>(new Resp(ots), hs, HttpStatus.OK);
    }

    @RequestMapping(value = "unsolved", method = RequestMethod.GET)
    public ResponseEntity<Resp<List<OpTask>>> find(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {
        Page<OpTask> pages = service.findByStatus("", new PageRequest(page - 1, limit, Sort.Direction.DESC, "status", "mtime"));
        List<OpTask> ots = pages.getContent();
        ots.forEach(v -> v.beforeReturn());

        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(pages.getTotalElements()));
        return new ResponseEntity<>(new Resp(ots), hs, HttpStatus.OK);
    }
}
