package com.zhongyun.smoke.model.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.model.SensorMsg;
import com.zhongyun.smoke.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by caozhennan on 2017/12/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gwrx implements SensorMsg {
    @JsonProperty("gateway")
    private Gw gw;

    private static final Logger logger = LoggerFactory.getLogger("Gwrx");

    public Gwrx() {
    }

    public void update(SensorService service, ConcurrentMap<Long, Long> gatewayTs) {
        if (!gatewayTs.containsKey(gw.getEUI())) {
            return;
        }
        long ts = System.currentTimeMillis();

        Sensor sg = service.findBaseByEui(gw.getEUI());
        if (sg == null) {
            gatewayTs.remove(gw.getEUI());
        } else {
            pingGateway(service, sg);
            gatewayTs.put(gw.getEUI(), ts);
//            sg.setStatus(Util.SENSOR_NORMAL);
//
//            try {
//                String lati = gw.getStatus().getLati().length() > 0 ? gw.getStatus().getLati() : sg.getLati();
//                String longi = gw.getStatus().getLongi().length() > 0 ? gw.getStatus().getLongi() : sg.getLongi();
//                if (!sg.getLati().equals(lati) || !sg.getLongi().equals(longi)) {
//                    service.updateLocationAndStatus(lati, longi, sg.getStatus(), sg.getId());
//                } else {
//                    service.update(sg);
//                }
//                gatewayTs.put(gw.getEUI(), ts);
//            } catch (Exception e) {
//                logger.error("update sensor failed for " + sg, e);
//            }
        }
    }

    @Override
    public Sensor toGateway() {
        return null;
    }

    @Override
    public Sensor toSensor() {
        return null;
    }

    @Override
    public String state() {
        return Util.SENSOR_NORMAL;
    }

    @Override
    public void pingGateway(SensorService sensorService, Sensor dbGateway) {
        dbGateway.setStatus(state());
        String lati = gw.getStatus().getLati().length() > 0 ? gw.getStatus().getLati() : dbGateway.getLati();
        String longi = gw.getStatus().getLongi().length() > 0 ? gw.getStatus().getLongi() : dbGateway.getLongi();
        if (!dbGateway.getLati().equals(lati) || !dbGateway.getLongi().equals(longi)) {
            sensorService.updateLocationAndStatus(lati, longi, dbGateway.getStatus(), dbGateway.getId());
        } else {
            sensorService.update(dbGateway);
        }
    }

    public void setGw(Gw gateway) {
        this.gw = gateway;
    }

    public Gw getGw() {
        return gw;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gw {
        @JsonProperty("EUI")
        private long EUI;
        @JsonProperty("Status")
        private Status status;

        public Gw() {
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }

        public void setEUI(long EUI) {
            this.EUI = EUI;
        }

        public long getEUI() {
            return EUI;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {
        private String lati;
        @JsonProperty("Long")
        private String longi;

        public Status() {
        }

        public void setLati(String lati) {
            this.lati = lati;
        }

        public void setLongi(String longi) {
            this.longi = longi;
        }

        public String getLati() {
            return lati;
        }

        public String getLongi() {
            return longi;
        }
    }
}
