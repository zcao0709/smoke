package com.zhongyun.smoke.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhongyun.smoke.model.payload.ImmeApp;
import com.zhongyun.smoke.model.payload.Payload;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by caozhennan on 2017/11/16.
 */
public class Util {

    public static final String SENSOR_NORMAL = "正常";
    public static final String SENSOR_UNKNOWN = "未知";
    public static final String SENSOR_FIRE = "火警";
    public static final String SENSOR_BATTERY = "低电压";
    public static final String SENSOR_FAULT = "故障";
    public static final String SENSOR_MUTE = "静音";
    public static final String SENSOR_DISCONN = "失联";
    public static final String SENSOR_TEST = "测试";

    public static final Set<String> CriticalSensorStatus = new HashSet<String>() {{
        add(SENSOR_FIRE);
        add(SENSOR_BATTERY);
        add(SENSOR_FAULT);
        add(SENSOR_MUTE);
        add(SENSOR_DISCONN);
        add(SENSOR_TEST);
    }};

    public static final String SENSOR_SMOKE = "烟感器";
    public static final String SENSOR_GWRX = "网关";

    public static final String OPTASK_UNSOLVED = "待处理";

    public static final String MONGO_COLLECTION = "app";

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
        String j = "{\"immeAPP\":{\"moteeui\":3747286346749607944,\"dir\":\"up\",\"userdata\":{\"seqno\":2,\"port\":2,\"payload\":\"CgAAIgMJ\",\"motetx\":{\"freq\":470.7,\"modu\":\"LORA\",\"datr\":\"SF12BW125\",\"codr\":\"4/5\",\"adr\":false}},\"gwrx\":[{\"eui\":3603162362285654048,\"time\":\"\",\"timefromgateway\":false,\"chan\":2,\"rfch\":0,\"rssi\":-99,\"lsnr\":-20.299999}]}}";
//        String j = "{\"app\":{\"moteeui\":3747286346749607944,\"dir\":\"up\",\"userdata\":{\"seqno\":2,\"port\":2,\"payload\":\"CgAAAQMJ\",\"motetx\":{\"freq\":470.7,\"modu\":\"LORA\",\"datr\":\"SF12BW125\",\"codr\":\"4/5\",\"adr\":false}},\"gwrx\":[{\"eui\":3603162362285654048,\"time\":\"\",\"timefromgateway\":false,\"chan\":2,\"rfch\":0,\"rssi\":-99,\"lsnr\":-20.299999}]}}";
        ImmeApp ia = Util.json2Object(j, ImmeApp.class);
        System.out.println(ia.getImmeAPP().getUserdata().getPayload());
        byte[] bs = fromBase64(ia.getImmeAPP().getUserdata().getPayload());
        for (byte b : bs) {
            System.out.printf("%02x\n", b);
        }
        System.out.println(Payload.status.get((int) bs[bs.length-1]));
    }
}
