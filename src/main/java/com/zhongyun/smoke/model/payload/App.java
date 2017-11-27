package com.zhongyun.smoke.model.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by caozhennan on 2017/11/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class App {
    private String moteeui;
    private String dir;
    private UserData userdata;
    private List<Gwrx> gwrx;
    private int _type; // 0: normal, 1: imme; -1: downlink
    private long _ctime;

    public App() {
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

    public void setMoteeui(String moteeui) {
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

    public String getMoteeui() {
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
