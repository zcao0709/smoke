package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by caozhennan on 2017/11/30.
 */
@RestController
@RequestMapping("/api/sensor/v1")
public class SensorController {
    @Autowired
    private SensorService service;

    @RequestMapping(method = RequestMethod.POST)
    public Resp<Sensor> post(@RequestBody Sensor sensor) {
        return new Resp(service.add(sensor));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Resp<String> delete(@PathVariable long id) {
        service.delete(id);
        return new Resp("");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Resp<Sensor> update(@RequestBody Sensor sensor) {
        return new Resp(service.update(sensor));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resp<Sensor> find(@PathVariable long id) {
        return new Resp(service.find(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Resp<List<Sensor>> findByProjectId(
            @RequestParam(value = "project") long projectId) {
        return new Resp(service.findByProjectId(projectId));
    }

    @RequestMapping(value = "/alarm", method = RequestMethod.GET)
    public Resp<List<Sensor>> findAlarmedByProjectId(
            @RequestParam(value = "project") long projectId) {
        return new Resp(service.findAlarmedByProjectId(projectId));
    }
}
