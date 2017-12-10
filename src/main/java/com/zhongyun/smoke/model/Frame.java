package com.zhongyun.smoke.model;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.payload.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * Created by caozhennan on 2017/11/15.
 */
public class Frame {

    public static final int COMMAND_HB = 0;
    public static final int COMMAND_JS = 1;

    private byte[] content;
    private int size;
    private int payloadLen;

    private static final byte START_ID = (byte)0xB0;
    private static final int START_LEN = 4;

    private static final byte CMD_HB = (byte) 0xE0;
    private static final byte CMD_JS = (byte) 0xE1;
    private static final int CMD_LEN  = 1;

    private static final int LEN_LEN  = 2;

    private static final byte END = (byte) 0xF0;

    private static final int COMMAND_OFFSET = START_LEN;
    private static final int PAYLOAD_LEN_OFFSET = START_LEN + CMD_LEN;
    private static final int PAYLOAD_OFFSET = START_LEN + CMD_LEN + LEN_LEN;

    private static final int FIX_LEN = START_LEN + CMD_LEN + LEN_LEN + 1;
    private static final int MAX_LEN = START_LEN + CMD_LEN + LEN_LEN + 1 + 65536;

    private static final Logger logger = LoggerFactory.getLogger("Frame");

    public Frame(InputStream in) throws IOException {
        content = new byte[MAX_LEN];
        size = 0;
        for (int i = in.read(); i != -1; i = in.read()) {
            byte b = (byte) i;
//            System.out.printf("%02x ", b);

            // read start id
            if (size < COMMAND_OFFSET) {
                if (b != START_ID) {
                    clear();
                    continue;
                } else {
                    content[size++] = b;
                }
            // read command
            } else if (size < PAYLOAD_LEN_OFFSET) {
                if (b != CMD_HB && b != CMD_JS) {
                    clear();
                    continue;
                } else {
                    content[size++] = b;
                }
//            } else if (size < PAYLOAD_OFFSET) {
            } else {
                content[size++] = b;
                if (b == END) {
                    payloadLen = getPayloadLen();
                    return;
                }
//                if (size == START_LEN + CMD_LEN + LEN_LEN) {
//                    payloadLen = getPayloadLen();
//                }
//            } else if (size < PAYLOAD_OFFSET + payloadLen) {
//                content[size++] = b;
//            } else {
//                if (b != END) {
//                    clear();
//                    continue;
//                } else {
//                    content[size++] = END;
//                    break;
//                }
            }
        }
    }

    public Frame(int command, String payload) {
        payloadLen = payload.length();
        size = payloadLen + FIX_LEN;
        content = new byte[size];

        int i = 0;
        for (; i < START_LEN; i++) {
            content[i] = START_ID;
        }
        content[i++] = (byte) (CMD_HB + command);
        content[i++] = (byte) (payloadLen & 0xFF);
        content[i++] = (byte) (payloadLen & 0xFF00);

        byte[] bs = payload.getBytes();
        for (int j = 0; j < bs.length; j++) {
            content[i++] = bs[j];
        }
        content[i++] = END;
    }

    private int getPayloadLen() {
        if (size >= PAYLOAD_OFFSET) {
            return (content[PAYLOAD_LEN_OFFSET+1] << 8) + (content[PAYLOAD_LEN_OFFSET] & 0xFF);
        } else {
            return 0;
        }
    }

    private void clear() {
        size = 0;
        payloadLen = 0;
    }

    public String payload() {
        return new String(content, PAYLOAD_OFFSET, payloadLen);
    }

    public boolean isHB() {
        return content[COMMAND_OFFSET] == CMD_HB;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(String.format("%02x ", content[i]));
        }
//        return payload() + ":" + sb.toString() + size + "/" + getPayloadLen();
        return payload();
    }

    public static void sendAuth(OutputStream out, String user, String pwd) throws IOException {
//        String auth = "{\"account\":{\"user\":\"manthinktest\",\"APPEUI\":null,\"customer\":null,\"accept\":false,\"pwd\":\"2f6c2f10405da982236653090d943753\",\"DwnData\":false,\"serverAccept\":false,\"serverUser\":null,\"serverPwd\":null,\"serverDB\":null}}";

        Auth auth = new Auth(user, pwd);
        Frame f = new Frame(COMMAND_JS, Util.object2Json(auth));
//        Frame f = new Frame(COMMAND_JS, auth);
        out.write(f.content);
        logger.info("sent auth: " + f.toString());
    }

    public static Auth recvAuth(InputStream in) throws IOException {
        Frame f = recvFrame(in);
        Auth auth = Util.json2Object(f.payload(), Auth.class);
        return auth;
    }

    public static Frame recvFrame(InputStream in) throws IOException {
        Frame f = new Frame(in);
        logger.info("recv: " + f.toString());
        return f;
    }

    public void sendFrame(OutputStream out) throws IOException {
        out.write(content);
        logger.info("sent: " + toString());
    }

    public static Frame newHB() {
        return new Frame(COMMAND_HB, "CSHeart");
    }

    public static void main(String[] args) {

        try (Socket socket = new Socket("iotcn02.manthink.cn", 6001)) {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            Frame.sendAuth(out, "manthinktest", "2f6c2f10405da982236653090d943753");

            Auth a = Frame.recvAuth(in);
            if (a.accepted()) {
                System.out.println("accepted");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
