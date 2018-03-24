package com.zhongyun.smoke.service;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.dao.mysql.SensorRepository;
import com.zhongyun.smoke.model.siter.SiterFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by caozhennan on 2018/3/15.
 */
@Service
public class SiterConnector extends Thread {
    @Autowired
    private ApplicationConfig config;

    @Autowired
    private SensorService sensorService;

    private ByteBuffer sendbuffer = ByteBuffer.allocate(SiterFrame.MAX_LEN);

    private Selector selector;
    private ServerSocketChannel server;

    private static final Logger logger = LoggerFactory.getLogger("SiterConnector");

    @PostConstruct
    private void init() {
        logger.info("start SiterConnector");
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                serve(config.getSiterPort());
                listen();
            } catch (SocketException e) {
                e.printStackTrace();
                logger.error("socket broken", e);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("socket broken", e);
            } finally {
                close();
            }
        }
    }

    private void serve(int port) throws IOException {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.socket().bind(new InetSocketAddress(port));
        server.configureBlocking(false);

        selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);

        logger.info("Server Start: " + port);
    }

    private void listen() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                handleKey(key);
            }
        }
    }

    private void handleKey(SelectionKey key) throws IOException {
        ServerSocketChannel server;
        SocketChannel client;
        int count;

        if (key.isAcceptable()) {
            server = (ServerSocketChannel) key.channel();
            client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(SiterFrame.MAX_LEN*2));

        } else if (key.isReadable()) {
            ByteBuffer recv = (ByteBuffer) key.attachment();
            client = (SocketChannel) key.channel();

            count = client.read(recv);
            if (count > 0) {
                SiterFrame f = SiterFrame.parse(recv, sensorService, config);
                if (f != null) {
                    logger.info("recv: " + f.toString());
                    byte[] r = f.response();
                    if (r == null) {
                        logger.error("no response for: " + f.toString());
                    } else {
                        logger.info("response for " + f.toString() + ": " + Util.byteArray(r));
                        sendbuffer.clear();
                        sendbuffer.put(r);
                        sendbuffer.flip();
                        client.write(sendbuffer);
                    }
                } else {
                    logger.info("recv: invalid frame");
                }
                client.register(selector, SelectionKey.OP_READ, recv);
            } else if (count < 0) {
                logger.info("some sensor disconnected");
                client.close();
            }
        }
    }

    private void close() {
        try {
            if (selector != null) {
                selector.close();
            }
            if (server != null) {
                server.socket().close();
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
