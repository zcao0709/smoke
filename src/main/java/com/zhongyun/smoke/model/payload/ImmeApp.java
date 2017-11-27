package com.zhongyun.smoke.model.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhongyun.smoke.common.Util;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by caozhennan on 2017/11/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImmeApp implements Payload {

    private App immeApp;

    public ImmeApp() {
    }

    @Override
    public void postRecv() {
        immeApp.set_type(1);
        immeApp.set_ctime(System.currentTimeMillis());
        immeApp.getUserdata().setPayload(Util.fromBase64(immeApp.getUserdata().getPayload()));
    }

    public void setImmeApp(App immeApp) {
        this.immeApp = immeApp;
    }

    public App getImmeApp() {
        return immeApp;
    }
}
