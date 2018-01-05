package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.model.Project;
import com.zhongyun.smoke.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by caozhennan on 2017/11/26.
 */
@RestController
@RequestMapping("/api/project/v1")
public class ProjectController {
    @Autowired
    private ProjectService service;

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger("ProjectController");

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Project> post(@RequestBody Project project) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        return Resp.ok(service.add(project));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable long id) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        service.delete(id);
        return Resp.ok(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Project> update(@RequestBody Project project) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        return Resp.ok(service.update(project));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Project> findById(@PathVariable long id) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        Project p = service.findById(id);
        if (p == null) {
            return Resp.not();
        }
        return Resp.ok(p.beforeReturn());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Project>> find(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        Page<Project> p = service.findLike(name, province, city, district, address, phone, page, limit);
        return Util.resp(p.getContent(), p.getTotal());
    }
}
