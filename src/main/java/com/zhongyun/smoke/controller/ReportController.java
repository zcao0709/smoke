package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by caozhennan on 2017/12/10.
 */
@RestController
@RequestMapping("/api/report/v1")
public class ReportController {
    @Autowired
    private ReportService service;

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger("ReportController");

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> findByProjectId(
            @RequestParam(value = "project") long id,
            @RequestParam(value = "start") long start,
            @RequestParam(value = "end") long end) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        Map<String, String> ret = service.findByProjectId(id, start, end);
        return Resp.ok(ret);
    }
}
