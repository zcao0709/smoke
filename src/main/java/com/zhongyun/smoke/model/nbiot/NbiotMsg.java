package com.zhongyun.smoke.model.nbiot;

import com.zhongyun.smoke.model.Sensor;

/**
 * Created by caozhennan on 2018/6/22.
 */
public interface NbiotMsg {
    String getId();
    String state();
    Sensor toSensor();
}
