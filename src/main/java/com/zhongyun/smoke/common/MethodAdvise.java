package com.zhongyun.smoke.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by caozhennan on 2018/7/16.
 */
@Component
@Aspect
public class MethodAdvise {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.zhongyun.smoke.common.MethodMonitor)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void logMethodInvokeParam(JoinPoint joinPoint) {
        logger.info("calling method {} with args: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void logMethodThrowing(JoinPoint joinPoint, Exception ex) {
        logger.info("method {} throwing: {}", joinPoint.getSignature().toShortString(), ex);
    }
}
