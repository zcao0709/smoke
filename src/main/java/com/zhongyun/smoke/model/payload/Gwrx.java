package com.zhongyun.smoke.model.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.service.SensorService;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by caozhennan on 2017/12/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gwrx {
    @JsonProperty("gateway")
    private Gw gw;

    public Gwrx() {
    }

    public void update(SensorService service, ConcurrentMap<Long, Long> gatewayTs) {
        if (!gatewayTs.containsKey(gw.getEUI())) {
            return;
        }
        long ts = System.currentTimeMillis();

        Sensor s = service.findBaseByEui(gw.getEUI());
        if (s == null) {
            gatewayTs.remove(gw.getEUI());
        } else {
            if (!s.getLati().equals(gw.getStatus().getLati()) || !s.getLongi().equals(gw.getStatus().getLongi())) {
                service.updateLatiAndLongiById(gw.getStatus().getLati(), gw.getStatus().getLongi(), s.getId());
                service.updateLatiAndLongiByGatewayId(s.getId());
            } else {
                service.update(s);
            }

            gatewayTs.put(gw.getEUI(), ts);
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
