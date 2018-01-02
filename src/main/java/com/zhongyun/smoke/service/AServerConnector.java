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

    @Autowired
    private SmsService smsService;

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
        while (true) {

            try (Socket socket = new Socket(config.getAserverIp(), config.getAserverPort())) {
                cleanupGateway();

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

                new Sender(out).start();

                while (true) {
                    Frame f = Frame.recvFrame(in);
                    if (f.getSize() == 0) {
                        smsService.send(new ArrayList<String>(){{add("17777791290");}}, "", "", "");
                        break;
                    }
                    if (f.isHB()) {
                        continue;
                    } else {
                        Payload.parse(f.payload(), mongo, sensorService, gatewayTs);
                    }
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
        private SensorService service;
        private ConcurrentMap<Long, Long> gatewayTs;

        private static final long GATEWAY_TIMEOUT = 3600 * 1000;
        private static final long SENSOR_TIMEOUT = 3600 * 26 * 1000;

        public SensorMonitor(SensorService service, ConcurrentMap<Long, Long> gatewayTs) {
            this.service = service;
            this.gatewayTs = gatewayTs;
        }

        @Override
        public void run() {
            long ts = System.currentTimeMillis();

            List<Long> keys = new LinkedList<>();
            gatewayTs.entrySet().forEach(v -> {
                if (v.getValue() - ts > GATEWAY_TIMEOUT) {
                    logger.warn("gateway " + String.format("%X", v.getKey()) + "time out");
                    keys.add(v.getKey());
                }
            });
            keys.forEach(v -> gatewayTs.remove(v));

//            sensorService.findOutOfDate();
        }
    }

    private void cleanupGateway() {

        sensorService.deleteUselessGateway();
        List<Sensor> sensors = sensorService.findBaseByType(Util.SENSOR_GWRX);
        sensors.forEach(v -> gatewayTs.put(v.getEui(), v.getMtime().getTime()));
    }
}
