package com.zhongyun.smoke.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhongyun.smoke.model.payload.ImmeApp;
import com.zhongyun.smoke.model.payload.Payload;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by caozhennan on 2017/11/16.
 */
public class Util {

    public static final long DEF_START_TS = 1510818350000L; // in ms
    public static final long DEF_END_TS = 33067727150000L; // in ms

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateFormat FORMAT = new SimpleDateFormat(TIME_FORMAT);

    public static final String USER_ADMIN = "管理员";
    public static final String USER_OP = "运维";
    public static final String USER_USER = "普通用户";

    public static final PasswordEncoder ENCODER = new StandardPasswordEncoder("zhy_sm");

    public static final String SENSOR_NORMAL = "正常";
    public static final String SENSOR_UNKNOWN = "未知";
    public static final String SENSOR_FIRE = "火警";
    public static final String SENSOR_FIRED = "已报警";
    public static final String SENSOR_BATTERY = "低电压";
    public static final String SENSOR_FAULT = "故障";
    public static final String SENSOR_MUTE = "静音";
    public static final String SENSOR_DISCONN = "失联";
    public static final String SENSOR_TEST = "测试";

    public static final Set<String> CriticalSensorStatus = new HashSet<String>() {{
        add(SENSOR_FIRE);
        add(SENSOR_FIRED);
        add(SENSOR_BATTERY);
        add(SENSOR_FAULT);
        add(SENSOR_MUTE);
        add(SENSOR_DISCONN);
        add(SENSOR_UNKNOWN);
    }};

    public static final Set<String> OpTaskAlarmCause = new HashSet<String>() {{
        add(SENSOR_FIRE);
        add(SENSOR_BATTERY);
        add(SENSOR_FAULT);
        add(SENSOR_MUTE);
        add(SENSOR_DISCONN);
    }};

    public static final Set<String> OpTaskSmsCause = new HashSet<String>() {{
        add(SENSOR_FIRE);
    }};

    static {
        if (System.getProperty("spring.profiles.active").equals("dev")) {
            OpTaskAlarmCause.add(SENSOR_TEST);
            OpTaskSmsCause.add(SENSOR_TEST);
        }
    }

    public static final String SENSOR_SMOKE = "烟感器";
    public static final String SENSOR_GWRX = "网关";

    public static final String OPTASK_UNSOLVED = "待处理";
    public static final String OPTASK_COMPLETE = "完成";
    public static final String OPTASK_OWNGOAL = "误报";
    public static final long OPTASK_EXPIRED = 2 * 24 * 3600 * 1000; // 2 days

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

    public static boolean validatePhone(String num) {
        return num.length() > 0;
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
