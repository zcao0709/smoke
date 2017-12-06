package com.zhongyun.smoke.model.payload;

import com.zhongyun.smoke.common.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caozhennan on 2017/11/25.
 */
public interface Payload {
    Map<Integer, String> status = new HashMap<Integer, String>() {{
        put(1, "火灾");
        put(2, "低电压");
        put(3, "故障");
        put(7, "静音");
        put(9, "测试");
//        put(-1, "心跳");
    }};

    void postRecv();

    default boolean needSave() {
        return true;
    }

    default String collection() {
        return "app";
    }

    static Payload parse(String payload) {
        Payload p;
        if (payload.startsWith("{\"immeAPP\"")) {
            p = Util.json2Object(payload, ImmeApp.class);
        } else if (payload.startsWith("{\"app\"")) {
            p = Util.json2Object(payload, UpApp.class);
        } else {
            return null;
        }
        p.postRecv();
        return p;
    }

    static String decode(String raw) {
        byte[] bs = Util.fromBase64(raw);
        return status.get((int) bs[bs.length - 1]);
    }
}
