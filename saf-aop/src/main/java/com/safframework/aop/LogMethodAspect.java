package com.safframework.aop;

import com.safframework.log.L;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

/**
 * Created by Tony Shen on 16/3/22.
 */
@Aspect
public class LogMethodAspect {

    @Around("execution(!synthetic * *(..)) && onLogMethod()")
    public Object doLogMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint);
    }

    @Pointcut("@within(com.safframework.aop.annotation.LogMethod)||@annotation(com.safframework.aop.annotation.LogMethod)")
    public void onLogMethod() {
    }

    private Object logMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        L.w(joinPoint.getSignature().toShortString() + " Args : " + (joinPoint.getArgs() != null ? Arrays.deepToString(joinPoint.getArgs()) : ""));
        Object result = joinPoint.proceed();
        String type = ((MethodSignature) joinPoint.getSignature()).getReturnType().toString();
        L.w(joinPoint.getSignature().toShortString() + " Result : " + ("void".equalsIgnoreCase(type)?"void":result));
        return result;
    }
}
