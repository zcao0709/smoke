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

import javax.xml.ws.Response;
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
    public ResponseEntity<Project> post(@RequestBody Project project) {
        return Resp.ok(service.add(project));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable long id) {
        service.delete(id);
        return Resp.ok(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Project> update(@RequestBody Project project) {
        return Resp.ok(service.update(project));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Project> find(@PathVariable long id) {
        Project p = service.find(id);
        if (p == null) {
            return Resp.not(id);
        }
        return Resp.ok(p.beforeReturn());
    }

    @RequestMapping(value = "/like", method = RequestMethod.GET)
    public ResponseEntity<List<Project>> find(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) {
        Page<Project> pages = service.find(name, province, city, district, address, new PageRequest(page - 1, limit, Sort.Direction.DESC, "mtime"));
        List<Project> ps = pages.getContent();
        ps.forEach(v -> v.beforeReturn());

        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(pages.getTotalElements()));
        return new ResponseEntity<>(ps, hs, HttpStatus.OK);
    }
}
