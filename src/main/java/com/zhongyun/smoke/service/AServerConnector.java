package com.zhongyun.smoke.service;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Frame;
import com.zhongyun.smoke.model.Sensor;
import com.zhongyun.smoke.model.payload.Auth;
import com.zhongyun.smoke.model.payload.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by caozhennan on 2017/11/16.
 */
@Service
public class AServerConnector extends Thread {
    @Autowired
    private ApplicationConfig config;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private SensorService sensorService;

    private BlockingQueue<Frame> framesToSendout = new LinkedBlockingQueue<>();

    private ConcurrentMap<Long, Long> gatewayTs = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger("AServerConnector");

    @PostConstruct
    private void init() {
        logger.info("start AServerConnector");
        start();
    }

    @Override
    public void run() {
        try {
            initSensors();
        } catch (Exception e) {
            logger.error("init sensors failed", e);
        }
        while (true) {

            try (Socket socket = new Socket(config.getAserverIp(), config.getAserverPort())) {
                logger.info("socket connected");

                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                Frame.sendAuth(out, config.getAserverUser(), config.getAserverPwd());

                Auth a = Frame.recvAuth(in);
                if (a.accepted()) {
                    logger.info(config.getAserverUser() + " accepted");
                } else {
                    logger.error(config.getAserverUser() + "/" + config.getAserverPwd() + " rejected");
                    return;
                }

                ScheduledExecutorService hb = Executors.newSingleThreadScheduledExecutor();
                hb.scheduleAtFixedRate(() -> framesToSendout.offer(Frame.newHB()), 0L, 15L, TimeUnit.MINUTES);

                ScheduledExecutorService mt = Executors.newSingleThreadScheduledExecutor();
                SensorMonitor sm = new SensorMonitor();
                long rate = config.getGatewayTimeout() / 2;
                mt.scheduleAtFixedRate(sm, rate, rate, TimeUnit.MILLISECONDS);

                new Sender(out).start();

                while (true) {
                    Frame f = Frame.recvFrame(in);
                    if (f.getSize() == 0) {
                        logger.error("got empty frame");
                        break;
                    }
                    if (f.isHB()) {
                        continue;
                    } else {
                        Payload.parse(f.payload(), mongo, sensorService, gatewayTs, config);
                    }
//                    Frame s = framesToSendout.poll();
//                    if (s != null) {
//                        s.sendFrame(out);
//                    }
                }
            } catch (IOException e) {
                logger.error("", e);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    private class Sender extends Thread {
        private OutputStream out;

        public Sender(OutputStream out) {
            this.out = out;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Frame f = framesToSendout.take();
                    f.sendFrame(out);
                } catch (InterruptedException e) {
                    logger.info("", e);
                } catch (IOException e) {
                    logger.error("", e);
                    return;
                }
            }
        }
    }

    private class SensorMonitor implements Runnable {

        public SensorMonitor() {
        }

        @Override
        public void run() {
            long ts = System.currentTimeMillis();
            logger.debug("smoke monitor at " + ts);

            List<Long> timeoutGateways = new LinkedList<>();
            gatewayTs.entrySet().forEach(v -> {
                if (v.getValue() > 0 && ts - v.getValue() > config.getGatewayTimeout()) {
                    logger.warn("gateway " + String.format("%X", v.getKey()) + " lost connection");
                    timeoutGateways.add(v.getKey());
                }
            });
            timeoutGateways.forEach(eui -> {
                Sensor g = sensorService.findBaseByEui(eui);
                long c = sensorService.countByGatewayId(g.getId());
                if (c == 0) {
                    gatewayTs.remove(eui);
                    sensorService.delete(g.getId());
                    logger.warn("gateway " + String.format("%X", eui) + " removed from DB since no sensor");
                } else {
                    gatewayTs.put(eui, 0L);
                    sensorService.updateStatusAndGateway(Util.SENSOR_DISCONN, g, ts);
                }
            });
            List<Sensor> ss = sensorService.findByMtimeBefore(new Date(ts - config.getSensorTimeout()));
            ss.forEach(s -> {
                sensorService.updateStatusAndGateway(Util.SENSOR_DISCONN, s, ts);
                logger.warn("sensor " + s.getEui16() + " lost connection");
            });
        }
    }

    private void initSensors() {
        sensorService.updateMtime();
        List<Sensor> sensors = sensorService.findBaseByType(Util.SENSOR_GWRX);
        sensors.forEach(v -> gatewayTs.put(v.getEui(), v.getMtime().getTime()));
    }
}
