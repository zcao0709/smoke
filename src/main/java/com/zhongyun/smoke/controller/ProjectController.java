package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.model.Project;
import com.zhongyun.smoke.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.PortUnreachableException;
import java.util.List;

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
        return new Resp(service.find(id).beforeReturn());
    }

    @RequestMapping(value = "/like", method = RequestMethod.GET)
    public ResponseEntity<Resp<List<Project>>> find(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {
        Page<Project> pages = service.find(name, province, city, district, address, new PageRequest(page - 1, limit, Sort.Direction.DESC, "mtime"));
        List<Project> ps = pages.getContent();

        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(pages.getTotalElements()));
        return new ResponseEntity<Resp<List<Project>>>(new Resp(ps), hs, HttpStatus.OK);
    }
}
