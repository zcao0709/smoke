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
    public ResponseEntity<OpTask> post(@RequestBody OpTask opTask) {
        return Resp.ok(service.add(opTask));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable long id) {
        service.delete(id);
        return Resp.ok(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<OpTask> update(@RequestBody OpTask opTask) {
        return Resp.ok(service.update(opTask));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<OpTask> find(@PathVariable long id) {
        OpTask o = service.find(id);
        if (o == null) {
            return Resp.not(id);
        }
        return Resp.ok(service.find(id).beforeReturn());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<OpTask>> findAll(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {
        Page<OpTask> pages = service.findAll(new PageRequest(page - 1, limit, Sort.Direction.DESC, "status", "mtime"));
        List<OpTask> ots = pages.getContent();
        ots.forEach(v -> v.beforeReturn());

        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(pages.getTotalElements()));
        return new ResponseEntity<>(ots, hs, HttpStatus.OK);
    }

    @RequestMapping(value = "unsolved", method = RequestMethod.GET)
    public ResponseEntity<List<OpTask>> find(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {
        Page<OpTask> pages = service.findUnsolved(new PageRequest(page - 1, limit, Sort.Direction.DESC, "status", "mtime"));
        List<OpTask> ots = pages.getContent();
        ots.forEach(v -> v.beforeReturn());

        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(pages.getTotalElements()));
        return new ResponseEntity<>(ots, hs, HttpStatus.OK);
    }
}
