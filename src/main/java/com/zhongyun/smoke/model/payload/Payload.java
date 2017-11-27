package com.zhongyun.smoke.model.payload;

import com.zhongyun.smoke.common.Util;

/**
 * Created by caozhennan on 2017/11/25.
 */
public interface Payload {
    void postRecv();

    default boolean needSave() {
        return true;
    }

    default String collection() {
        return "app";
    }

    static Payload parse(String payload) {
        Payload p;
        if (payload.startsWith("{\"immeAPP")) {
            p = Util.json2Object(payload, ImmeApp.class);
        } else if (payload.startsWith("{\"APP")) {
            p = Util.json2Object(payload, UpApp.class);
        } else {
            return null;
        }
        p.postRecv();
        return p;
    }
}
