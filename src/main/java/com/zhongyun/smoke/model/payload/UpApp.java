package com.zhongyun.smoke.model.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhongyun.smoke.common.Util;

/**
 * Created by caozhennan on 2017/11/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpApp implements Payload {
    private App app;

    public UpApp() {
    }

    @Override
    public void postRecv() {
        app.set_type(0);
        app.set_ctime(System.currentTimeMillis());
        app.getUserdata().setPayload(Payload.decode(app.getUserdata().getPayload()));
    }

    public void setApp(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }
}
