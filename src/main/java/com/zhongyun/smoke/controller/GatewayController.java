package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.model.Resp;
//import com.zhongyun.smoke.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by caozhennan on 2017/11/30.
 */
/*
@RestController
@RequestMapping("/api/gw/v1")
public class GatewayController {
    @Autowired
    private GatewayService service;

    @RequestMapping(method = RequestMethod.POST)
    public Resp<Gateway> post(@RequestBody Gateway gateway) {
        return new Resp(service.add(gateway));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Resp<String> delete(@PathVariable long id) {
        service.delete(id);
        return new Resp("");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Resp<Gateway> update(@RequestBody Gateway gateway) {
        return new Resp(service.update(gateway));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resp<Gateway> find(@PathVariable long id) {
        return new Resp(service.find(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Resp<List<Gateway>> find() {
        return new Resp(service.findAll());
    }
}
*/
