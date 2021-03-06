package com.zhongyun.smoke.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.StringUtils;
import com.zhongyun.smoke.model.payload.ImmeApp;
import com.zhongyun.smoke.model.payload.Payload;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by caozhennan on 2017/11/16.
 */
public class Util {
    public static final String ERR_NOT_ENOUGH = "信息不足，请补全";
    public static final String ERR_INVALID_ID = "无效的ID";
    public static final String ERR_DEL_ADMIN = "第一管理员不可删除";

    public static final long DEF_START_TS = 1510818350000L; // in ms 2017/11/16 3:45:00
    public static final long DEF_END_TS = 33067727150000L; // in ms 3017/11/16 3:45:00

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateFormat FORMAT = new SimpleDateFormat(TIME_FORMAT);

    public static final String USER_ADMIN = "管理员";
    public static final String USER_OP = "运维用户";
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
        if (testEnv()) {
            OpTaskAlarmCause.add(SENSOR_TEST);
            OpTaskSmsCause.add(SENSOR_TEST);
        }
    }

    public static final String SENSOR_SMOKE = "烟感器";
    public static final String SENSOR_GWRX = "网关";

    // vendor of sensors
    public static final int VENDOR_MENSI = 1;
    public static final int VENDOR_SITER = 2;
    public static final int VENDOR_ORENA = 3;
    public static final int VENDOR_SANJI = 4;

    public static final int GATEWAY_UNSET = 0;
    public static final int PROJECT_UNSET = 0;

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

    public static String postLike(String str) {
        if (str == null || str.length() == 0) {
            return "%";
        }
        return "%" + str;
    }

    public static boolean validatePhone(String num) {
        return num != null && num.length() > 0;
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

    public static <T> ResponseEntity<List<T>> resp(List<T> content, long total) {
        HttpHeaders hs = new HttpHeaders();
        hs.add("x-total-count", String.valueOf(total));
        return new ResponseEntity<>(content, hs, HttpStatus.OK);
    }

    public static String order(String column) {
        return "ORDER BY " + column + " ASC ";
    }

    public static String orderDesc(String column) {
        return "ORDER BY " + column + " DESC ";
    }

    public static String page(int page, int limit) {
        int offset = (page - 1) * limit;
        return "LIMIT " + offset + "," + limit + " ";
    }

    public static String byteArray(byte[] b) {
        return byteArray(b, 0, b.length);
    }

    public static String byteArray(byte[] b, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(String.format("%02X ", b[i]));
        }
        return sb.toString().trim();
    }

    public static boolean testEnv() {
        return System.getProperty("spring.profiles.active").equals("dev");
    }

    public static String httpRequest(String url, Map<String, String> params,
                                     Map<String, String> headers,
                                     String postJsonBody,
                                     String postFormBody,
                                     boolean isPost, int connectTimeout, int readTimeout) {
        String response = null;
        try {
            URL testUrl = new URL(buildUrl(url, params));
            HttpURLConnection connection = (HttpURLConnection)testUrl.openConnection();
            connection.setDoInput(true);
            setupHeader(connection, headers);
            if (isPost) {
                connection.setRequestMethod("POST");
                sendPostData(connection, postJsonBody, postFormBody);
            }

            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            int retCode = connection.getResponseCode();
            if (retCode == 200) {
                InputStream is = connection.getInputStream();
                response = getStringFromInputStream(is);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } finally {

        }
        return response;
    }

    private static String buildUrl(String url, Map<String, String> params) {
        if (StringUtils.isNullOrEmpty(url) || params == null) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url).append("?");
        params.entrySet().stream().forEach(entry -> sb.append(entry.getKey()).append("=").append(entry.getValue()).append('&'));
        return sb.deleteCharAt(sb.length()-1).toString();
    }

    private static void setupHeader(URLConnection connection, Map<String, String> headers) {
        if (connection == null  || headers == null) {
            return;
        }

        headers.entrySet().stream().forEach(entry -> connection.setRequestProperty(entry.getKey(), entry.getValue()));
    }

    private static int sendPostData(URLConnection connection, String body) {
        if (connection == null || StringUtils.isNullOrEmpty(body)) {
            return 0;
        }
        int count = 0;
        connection.setDoOutput(true);
        try {
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();
            count = body.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private static int sendPostData(URLConnection connection, String jsonBody, String formBody) {
        if (jsonBody != null) {
            connection.setRequestProperty("Content-Type", "application/json");
            return sendPostData(connection, jsonBody);
        } else {
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            return sendPostData(connection, formBody);
        }
    }

    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();
        os.close();
        return state;
    }
}
