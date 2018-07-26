package com.zhongyun.smoke.service;

import com.zhongyun.smoke.common.MethodMonitor;
import com.zhongyun.smoke.model.Project;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.model.monitor.Resp;
import com.zhongyun.smoke.model.monitor.Upload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.zhongyun.smoke.common.Util.*;

/**
 * Created by caozhennan on 2018/7/25.
 */
@Service
public class MonitorService {
    private static final String url = "http://120.27.12.97:5000/xfjc/v1/alarm";
    private static final String token = "C9D531D6-4A45-4E7A-82D5-A62ADE045D5A";
    private static final Map<String, String> headers = new HashMap<>();
    static {
        headers.put("access_token", token);
    }
    private static final String owner = "zy1234567800";
    private static final String device = SENSOR_SMOKE;
    private static final int alarm = 1;
    private static final int timeout = 5000;

    private static final Logger logger = LoggerFactory.getLogger("MonitorService");

    @MethodMonitor
    public void upload(Sensor sensor, Project project, String status, String time) {
        if (sensor == null) {
            return;
        }
        String address = SENSOR_UNKNOWN;
        if (project != null) {
            address = project.fullAddress();
        }
        int isTest = SENSOR_FIRE.equals(status) ? 0 : 1;

        Upload data = new Upload(owner, isTest, time, alarm, sensor.getLocation(), device,
                                 address, sensor.getEui16(), time);
        String req = object2Json(data);
        logger.debug("upload request: " + req);
        String resp = httpRequest(url, null, headers, null, req, true, timeout, timeout);
        Resp r = json2Object(resp, Resp.class);
        if (r == null || r.getCode() != 200) {
            logger.error("upload " + data + "failed: " + r != null ? r.getMsg() : "null resp");
        }
    }

    public static void main(String[] args) {
        Sensor s = new Sensor(52L, 3747286346749607963L, "3401091411AE801B", null, null, "副楼二层值班室（212）", null, null, null,
                              null, null, 0, 0, null, null, null, 0);
        Project p = new Project(8L, "青岛市立医院一期", "山东省", "青岛市", "市南区", "安徽路21号", 0, null, null, null, null);

        MonitorService service = new MonitorService();
        service.upload(s, p, SENSOR_TEST, FORMAT.format(new Date()));
    }
}
