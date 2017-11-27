package com.zhongyun.smoke.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static String tobase64 (String rawMessage) {
        return new String(Base64.encodeBase64(rawMessage.getBytes()));
    }

    public static String fromBase64 (String base64String) {
        return new String(Base64.decodeBase64(base64String));
    }

    public static String quoteWithLike(String str) {
        return "%" + str + "%";
    }
}
