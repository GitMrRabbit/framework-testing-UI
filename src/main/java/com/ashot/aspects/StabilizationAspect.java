package com.ashot.aspects;

import com.ashot.annotations.Stabilize;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class StabilizationAspect {

    @Around("@annotation(stabilize)")
    public Object stabilize(ProceedingJoinPoint joinPoint, Stabilize stabilize) throws Throwable {
        long millis = stabilize.millis();
        log.debug("Stabilizing for {} ms before: {}", millis, joinPoint.getSignature().getName());
        Thread.sleep(millis);
        return joinPoint.proceed();
    }
}
