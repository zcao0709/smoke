package com.zhongyun.smoke.common;

/**
 * Created by caozhennan on 2018/7/16.
 */

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodMonitor {
}
