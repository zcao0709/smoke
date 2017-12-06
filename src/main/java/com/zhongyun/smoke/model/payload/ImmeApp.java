package com.zhongyun.smoke.model.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by caozhennan on 2017/11/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImmeApp implements Payload {

    private App immeAPP;

    public ImmeApp() {
    }

    @Override
    public void postRecv() {
        immeAPP.set_type(1);
        immeAPP.set_ctime(System.currentTimeMillis());
        immeAPP.getUserdata().setPayload(Payload.decode(immeAPP.getUserdata().getPayload()));
    }

    public void setImmeAPP(App immeAPP) {
        this.immeAPP = immeAPP;
    }

    public App getImmeAPP() {
        return immeAPP;
    }
}
