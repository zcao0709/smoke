package com.zhongyun.smoke.model.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.OpTask;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.service.OpTaskService;
import com.zhongyun.smoke.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by caozhennan on 2017/11/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class App {
    private long moteeui;
    private String dir;
    private UserData userdata;
    private List<Gwrx> gwrx;
    private int _type; // 0: normal, 1: imme;
    private long _ctime;

    private static final Logger logger = LoggerFactory.getLogger("App");

    public App() {
    }

    public void update(SensorService sensorService, OpTaskService opTaskService, ConcurrentMap<Long, Long> gatewayTs) {
        if (gwrx == null || gwrx.size() == 0) {
            logger.error("sensor " + moteeui + " has no gateway, will not be saved");
            return;
        }
        String payload = payload();
        if (payload == null) {

        }
        long ts = System.currentTimeMillis();
        Gwrx g = gwrx.get(0);
        Sensor sg = sensorService.findByEui(g.eui);

        if (sg == null) {
            sg = new Sensor(g.eui, Util.SENSOR_GWRX, new Timestamp(ts), Util.SENSOR_NORMAL, 0);
            sg = sensorService.add(sg);

            gatewayTs.put(g.eui, ts);
        }
        logger.info("gateways: " + gatewayTs);

        Sensor s = sensorService.findByEui(moteeui);
        if (s == null) {
            s = new Sensor(moteeui, Util.SENSOR_SMOKE, new Timestamp(ts), payload(), sg.getId());
            sensorService.add(s);
        } else {
            s.setStatus(payload());
            s.setGatewayId(sg.getId());
            sensorService.update(s);

            if (Util.OpTaskCause.contains(payload())) {
                OpTask ot = new OpTask(moteeui, 1, new Timestamp(ts), payload(), Util.OPTASK_UNSOLVED, s.getProjectId());
                opTaskService.add(ot);
            }
        }
        return;
    }

    public String payload() {
        if (userdata == null)
            return null;
        return userdata.getPayload();
    }

    public void setPayload(String payload) {
        if (userdata == null)
            return;
        userdata.setPayload(payload);
    }

    public void set_type(int _type) {
        this._type = _type;
    }

    public void set_ctime(long _ctime) {
        this._ctime = _ctime;
    }

    public int get_type() {
        return _type;
    }

    public long get_ctime() {
        return _ctime;
    }

    public void setMoteeui(long moteeui) {
        this.moteeui = moteeui;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setUserdata(UserData userdata) {
        this.userdata = userdata;
    }

    public void setGwrx(List<Gwrx> gwrx) {
        this.gwrx = gwrx;
    }

    public long getMoteeui() {
        return moteeui;
    }

    public String getDir() {
        return dir;
    }

    public UserData getUserdata() {
        return userdata;
    }

    public List<Gwrx> getGwrx() {
        return gwrx;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserData {
        private int seqno;
        private int port;
        private String payload;
        private Motetx motetx;

        public UserData() {
        }

        public void setSeqno(int seqno) {
            this.seqno = seqno;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public void setMotetx(Motetx motetx) {
            this.motetx = motetx;
        }

        public int getSeqno() {
            return seqno;
        }

        public int getPort() {
            return port;
        }

        public String getPayload() {
            return payload;
        }

        public Motetx getMotetx() {
            return motetx;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Motetx {
        private double freq;
        private String modu;
        private String datr;
        private String codr;
        private boolean adr;

        public Motetx() {
        }

        public void setFreq(double freq) {
            this.freq = freq;
        }

        public void setModu(String modu) {
            this.modu = modu;
        }

        public void setDatr(String datr) {
            this.datr = datr;
        }

        public void setCodr(String codr) {
            this.codr = codr;
        }

        public void setAdr(boolean adr) {
            this.adr = adr;
        }

        public double getFreq() {
            return freq;
        }

        public String getModu() {
            return modu;
        }

        public String getDatr() {
            return datr;
        }

        public String getCodr() {
            return codr;
        }

        public boolean isAdr() {
            return adr;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gwrx {
        private long eui;
        private String time;
        private boolean timefromgateway;
        private long chan;
        private int rfch;
        private double rssi;
        private double lsnr;

        public Gwrx() {
        }

        public void setEui(long eui) {
            this.eui = eui;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setTimefromgateway(boolean timefromgateway) {
            this.timefromgateway = timefromgateway;
        }

        public void setChan(long chan) {
            this.chan = chan;
        }

        public void setRfch(int rfch) {
            this.rfch = rfch;
        }

        public void setRssi(double rssi) {
            this.rssi = rssi;
        }

        public void setLsnr(double lsnr) {
            this.lsnr = lsnr;
        }

        public long getEui() {
            return eui;
        }

        public String getTime() {
            return time;
        }

        public boolean isTimefromgateway() {
            return timefromgateway;
        }

        public long getChan() {
            return chan;
        }

        public int getRfch() {
            return rfch;
        }

        public double getRssi() {
            return rssi;
        }

        public double getLsnr() {
            return lsnr;
        }
    }
}
