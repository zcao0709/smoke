package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.model.nbiot.NbiotMsg;
import com.zhongyun.smoke.model.nbiot.SJStatusMsg;
import com.zhongyun.smoke.model.nbiot.StatusMsg;
import com.zhongyun.smoke.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by caozhennan on 2018/6/2.
 */
@RestController
@RequestMapping("/api/nb/v1")
public class NbiotController {
    @Autowired
    private SensorService sensorService;

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger("NbiotController");

    /*
    @RequestMapping(value = "activate", method = RequestMethod.POST)
    public void activate(@RequestBody ActivateMsg msg) {
        if (msg.getResultCode() != 1) {
            logger.warn("Nbiot activation failure: " + msg.getDeviceId());
            return;
        }
        long ts = System.currentTimeMillis();
        Sensor s = new Sensor(msg.getDeviceId(), Util.SENSOR_SMOKE, Util.VENDOR_NBIOT, new Timestamp(ts), Util.SENSOR_NORMAL,
                              Util.GATEWAY_UNSET, Util.PROJECT_UNSET);
        s.setPhone(config.getAdminPhone());
        sensorService.add(s);
    }
    */

    @RequestMapping(value = "status", method = RequestMethod.POST)
    public void status(@RequestBody StatusMsg msg) {
        logger.info(request.getRequestURL().append("?").append(msg.toString()).toString());
        handleMsg(msg);
    }

    @RequestMapping(value = "sj", method = RequestMethod.POST)
    public void sjStatus(@RequestBody SJStatusMsg msg) {

        logger.info(request.getRequestURL().append("?").append(msg.toString()).toString());
        handleMsg(msg);
    }

    private void handleMsg(NbiotMsg msg) {
        String state = msg.state();
        if (state == null || state.length() == 0) {
            return;
        }
        Sensor s = sensorService.findBaseByEui16(msg.getId());
        if (s == null) {
            s = msg.toSensor();
            s.setPhone(config.getAdminPhone());
            sensorService.add(s);

        } else {
            sensorService.updateStatusAndGateway(msg.state(), s, System.currentTimeMillis());
        }
    }
}
