package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.OpTask;
import com.zhongyun.smoke.model.OpTask;
import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.service.OpTaskService;
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
 * Created by caozhennan on 2017/12/1.
 */
@RestController
@RequestMapping("/api/op/v1")
public class OpTaskController {
    @Autowired
    private OpTaskService service;

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger("OpTaskController");

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable long id) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        service.delete(id);
        return Resp.ok(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<OpTask> update(@RequestBody OpTask opTask) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        return Resp.ok(service.update(opTask));
    }

    @RequestMapping(value = "unsolved", method = RequestMethod.PUT)
    public ResponseEntity<OpTask> updateUnsolved(@RequestBody OpTask opTask) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        return Resp.ok(service.update(opTask));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<OpTask> find(@PathVariable long id) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        OpTask o = service.find(id);
        if (o == null) {
            return Resp.not();
        }
//        return Resp.ok(OpTask.valueOf(o.beforeReturn()));
        return Resp.ok(o.beforeReturn());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<OpTask>> findAll(
            @RequestParam(value = "eui", defaultValue = "0") String eui16,
            @RequestParam(value = "project", defaultValue = "-1") long projectId,
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        Sort.Order o1 = new Sort.Order(Sort.Direction.ASC, "status");
        Sort.Order o2 = new Sort.Order(Sort.Direction.DESC, "mtime");

        Page<OpTask> pages;
        if (!eui16.equals("0")) {
            long eui = Long.parseLong(eui16, 16);
            pages = service.findBaseByEui(eui, new PageRequest(page - 1, limit, new Sort(o1, o2)));
        } else if (projectId != -1) {
            pages = service.findBaseByProjectId(projectId, new PageRequest(page - 1, limit, new Sort(o1, o2)));
        } else {
            pages = service.findAll(new PageRequest(page - 1, limit, new Sort(o1, o2)));
        }
        List<OpTask> ots = pages.getContent();
        ots.forEach(v -> v.beforeReturn());

        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(pages.getTotalElements()));
//        return new ResponseEntity<>(OpTask.valueOf(ots), hs, HttpStatus.OK);
        return new ResponseEntity<>(ots, hs, HttpStatus.OK);
    }

    @RequestMapping(value = "unsolved", method = RequestMethod.GET)
    public ResponseEntity<List<OpTask>> find(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        Sort.Order o1 = new Sort.Order(Sort.Direction.ASC, "status");
        Sort.Order o2 = new Sort.Order(Sort.Direction.DESC, "mtime");
        Page<OpTask> pages = service.findUnsolved(new PageRequest(page - 1, limit, new Sort(o1, o2)));
        List<OpTask> ots = pages.getContent();
        ots.forEach(v -> v.beforeReturn());

        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(pages.getTotalElements()));
//        return new ResponseEntity<>(OpTask.valueOf(ots), hs, HttpStatus.OK);
        return new ResponseEntity<>(ots, hs, HttpStatus.OK);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity<List<OpTask>> findLike(
//
//            @RequestParam(value = "_page", defaultValue = "1") int page,
//            @RequestParam(value = "_limit", defaultValue = "10") int limit) {
//    ) {

//    }

}
