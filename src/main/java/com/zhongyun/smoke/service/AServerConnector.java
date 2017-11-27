package com.zhongyun.smoke.service;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.model.Frame;
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

    private BlockingQueue<Frame> framesToSendout = new LinkedBlockingQueue<>();

    private static final Logger logger = LoggerFactory.getLogger("AServerConnector");

    @PostConstruct
    private void init() {
        logger.info("start AServerConnector");
//        start();
    }

    @Override
    public void run() {
        while (true) {

            try (Socket socket = new Socket(config.getAserverIp(), config.getAserverPort())) {
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                Frame.sendAuth(out, config.getAserverUser(), config.getAserverPwd());

                Auth a = Frame.recvAuth(in);
                if (a.accepted()) {
                    logger.info(config.getAserverUser() + " accepted");
                } else {
                    logger.error(config.getAserverUser() + "/" + config.getAserverPwd() + " rejected");
                }

                ScheduledExecutorService hb = Executors.newSingleThreadScheduledExecutor();
                hb.scheduleAtFixedRate(() -> framesToSendout.offer(Frame.newHB()), 0L, 15L, TimeUnit.MINUTES);

                new Sender(out).start();

                while (true) {
                    Frame f = Frame.recvFrame(in);
                    if (f.isHB()) {
                        continue;
                    } else {
                        Payload p = Payload.parse(f.payload());
                        if (p != null && p.needSave()) {
                            mongo.save(f.payload(), p.collection());
                        }
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
}
