package com.safframework.aop;

import android.annotation.TargetApi;
import android.util.Log;

import com.safframework.aop.annotation.Cacheable;
import com.safframework.cache.Cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by Tony Shen on 16/3/23.
 */
@TargetApi(14)
@Aspect
public class CacheAspect {

    @Around("execution(!synthetic * *(..)) && onCacheMethod()")
    public Object doCacheMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        return cacheMethod(joinPoint);
    }

    @Pointcut("@within(com.safframework.aop.annotation.Cacheable)||@annotation(com.safframework.aop.annotation.Cacheable)")
    public void onCacheMethod() {
    }

    private Object cacheMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("LM" , "LM___cacheMethod");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        Object result = null;
        Log.e("LM" , "LM___cacheable " + cacheable);

        if (cacheable!=null) {
            String key = cacheable.key();
            int expiry = cacheable.expiry();
            Log.e("LM" , "LM___key " + key);
            result = joinPoint.proceed();
            Cache cache = Cache.get(Utils.getContext());
            if (expiry>0) {
                cache.put(key,(Serializable)result,expiry);
            } else {
                cache.put(key,(Serializable)result);
            }
        } else {
            // 不影响原来的流程
            result = joinPoint.proceed();
        }

        return result;
    }
}
