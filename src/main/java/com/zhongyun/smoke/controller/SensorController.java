package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.service.SensorService;
import static com.zhongyun.smoke.common.Util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by caozhennan on 2017/11/30.
 */
@RestController
@RequestMapping("/api/sensor/v1")
public class SensorController {
    @Autowired
    private SensorService service;

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger("SensorController");

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable long id) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        service.delete(id);
        return Resp.ok(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Sensor> update(@RequestBody Sensor sensor) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

//        return Resp.ok(Sensor.valueOf(service.update(sensor.toSensor())));
        return Resp.ok(service.update(sensor));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Sensor> findOne(@PathVariable long id) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        Sensor s = service.find(id);
        if (s == null) {
            return Resp.not();
        }
//        return Resp.ok(Sensor.valueOf(service.findOne(id)));
        return Resp.ok(service.find(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Sensor>> findLike(
            @RequestParam(value = "project", defaultValue = "-1") long projectId,
            @RequestParam(value = "eui", required = false) String eui,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "type", defaultValue = SENSOR_SMOKE) String type,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "ctime_start", defaultValue = "0") long ctimeStart,
            @RequestParam(value = "ctime_end", defaultValue = "0") long ctimeEnd,
            @RequestParam(value = "guarantee", required = false) String guarantee,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit) throws ParseException {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        if (ctimeStart == 0) {
            ctimeStart = DEF_START_TS;
        }
        if (ctimeEnd == 0) {
            ctimeEnd = DEF_END_TS;
        }
        Date start = new Date(ctimeStart);
        Date end = new Date(ctimeEnd);
        Page<Sensor> p = service.findLike(projectId, eui, model, type, location, guarantee, status, phone, start, end, page, limit);
        return resp(p.getContent(), p.getTotal());
    }

    @RequestMapping(value = "/alarm", method = RequestMethod.GET)
    public ResponseEntity<List<Sensor>> findAlarmedByProjectId(
            @RequestParam(value = "project") long projectId) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

//        return Resp.ok(Sensor.valueOf(service.findAlarmedByProjectId(projectId)));
        List<Sensor> sensors = service.findAlarmedByProjectId(projectId);
//        sensors.forEach(v -> v.beforeReturn());
        return Resp.ok(sensors);
    }
}
