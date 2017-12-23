package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<Sensor> find(@PathVariable long id) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        Sensor s = service.find(id);
        if (s == null) {
            return Resp.not(id);
        }
//        return Resp.ok(Sensor.valueOf(service.find(id)));
        return Resp.ok(service.find(id).beforeReturn());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Sensor>> findByProjectId(
            @RequestParam(value = "project") long projectId) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

//        return Resp.ok(Sensor.valueOf(service.findByProjectId(projectId)));
        List<Sensor> sensors = service.findByProjectId(projectId);
        sensors.forEach(v -> v.beforeReturn());
        return Resp.ok(sensors);
    }

    @RequestMapping(value = "/alarm", method = RequestMethod.GET)
    public ResponseEntity<List<Sensor>> findAlarmedByProjectId(
            @RequestParam(value = "project") long projectId) {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

//        return Resp.ok(Sensor.valueOf(service.findAlarmedByProjectId(projectId)));
        List<Sensor> sensors = service.findAlarmedByProjectId(projectId);
        sensors.forEach(v -> v.beforeReturn());
        return Resp.ok(sensors);
    }
}
