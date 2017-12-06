package com.zhongyun.smoke.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhongyun.smoke.model.payload.ImmeApp;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;

/**
 * Created by caozhennan on 2017/11/16.
 */
public class Util {

    public static <T> T json2Object(String s, Class<T> targetClass) {
        if (s == null || s.length() == 0) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(s, targetClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T json2Object(byte[] s, Class<T> targetClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(s, targetClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String object2Json(T object) {
        if (object == null) {
            return "null";
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toBase64 (String rawMessage) {
        return new String(Base64.encodeBase64(rawMessage.getBytes()));
    }

    public static byte[] fromBase64 (String base64String) {
        return Base64.decodeBase64(base64String);
    }

    public static String like(String str) {
        if (str == null || str.length() == 0) {
            return "%";
        }
        return "%" + str + "%";
    }

    public static void main(String[] args) {
        String j = "{\"immeAPP\":{\"moteeui\":3747286346749607944,\"dir\":\"up\",\"userdata\":{\"seqno\":2,\"port\":2,\"payload\":\"CgAAAQMJ\",\"motetx\":{\"freq\":470.7,\"modu\":\"LORA\",\"datr\":\"SF12BW125\",\"codr\":\"4/5\",\"adr\":false}},\"gwrx\":[{\"eui\":3603162362285654048,\"time\":\"\",\"timefromgateway\":false,\"chan\":2,\"rfch\":0,\"rssi\":-99,\"lsnr\":-20.299999}]}}";
//        String j = "{\"app\":{\"moteeui\":3747286346749607944,\"dir\":\"up\",\"userdata\":{\"seqno\":2,\"port\":2,\"payload\":\"CgAAAQMJ\",\"motetx\":{\"freq\":470.7,\"modu\":\"LORA\",\"datr\":\"SF12BW125\",\"codr\":\"4/5\",\"adr\":false}},\"gwrx\":[{\"eui\":3603162362285654048,\"time\":\"\",\"timefromgateway\":false,\"chan\":2,\"rfch\":0,\"rssi\":-99,\"lsnr\":-20.299999}]}}";
        ImmeApp ia = Util.json2Object(j, ImmeApp.class);
        ia.postRecv();
        System.out.println(ia.getImmeAPP().getUserdata().getPayload());
    }
}
