package com.zhongyun.smoke.model.nbiot;

import com.zhongyun.smoke.model.SensorMsg;

/**
 * Created by caozhennan on 2018/6/22.
 */
public interface NbiotMsg extends SensorMsg {
    String getId();
    String state();
}
