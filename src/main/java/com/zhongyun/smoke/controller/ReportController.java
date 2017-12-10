package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by caozhennan on 2017/12/10.
 */
@RestController
@RequestMapping("/api/report/v1")
public class ReportController {
    @Autowired
    private ReportService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> findByProjectId(@PathVariable long id) {
        Map<String, String> ret = service.findByProjectId(id);
        if (ret == null || ret.size() == 0) {
            return Resp.not(id);
        }
        return Resp.ok(ret);
    }
}
