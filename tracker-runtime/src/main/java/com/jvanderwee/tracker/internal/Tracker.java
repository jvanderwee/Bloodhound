package com.jvanderwee.tracker.internal;

import android.util.Log;

import com.jvanderwee.tracker.TrackEvent;
import com.jvanderwee.tracker.TrackScreen;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class Tracker {
    private static volatile boolean enabled = true;

    @Pointcut("execution(@com.jvanderwee.tracker.TrackEvent * *(..))")
    public void trackEventMethod() {}

    @Pointcut("execution(@com.jvanderwee.tracker.TrackScreen * *(..))")
    public void trackScreenMethod() {}

    public static void setEnabled(boolean enabled) {
        Tracker.enabled = enabled;
    }

    @Around("trackEventMethod() || trackScreenMethod()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);
        return joinPoint.proceed();
    }

    private static void enterMethod(JoinPoint joinPoint) {
        if (!enabled) return;

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType();

        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method m = ms.getMethod();

        TrackEvent trackEvent = m.getAnnotation(TrackEvent.class);

        if (trackEvent != null) {
            Log.v(asTag(cls),
                    String.format("Tracking event with category '%s', action '%s', label '%s'",
                            trackEvent.category(),
                            trackEvent.action(),
                            trackEvent.label()));
        }

        TrackScreen trackScreen = m.getAnnotation(TrackScreen.class);

        if (trackScreen != null) {
            Log.v(asTag(cls),
                    String.format("Tracking screen with name '%s'", trackScreen.value()));

        }
    }

    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }
}