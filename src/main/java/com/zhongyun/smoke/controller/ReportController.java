package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.common.Util;
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
    public ResponseEntity<Map<String, Map<String, String>>> findBy(
            @RequestParam(value = "project", defaultValue = "-1") long projectId,
            @RequestParam(value = "district", defaultValue = "0") int district,
            @RequestParam(value = "city", defaultValue = "0") int city,
            @RequestParam(value = "province", defaultValue = "0") int province,
            @RequestParam(value = "start", defaultValue = "0") long start,
            @RequestParam(value = "end", defaultValue = "0") long end) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        if (start == 0) {
            start = Util.DEF_START_TS;
        }
        if (end == 0) {
            end = Util.DEF_END_TS;
        }
        Map<String, Map<String, String>> ret = service.findBy(projectId, district, city, province, start, end);
        return Resp.ok(ret);
    }
}
