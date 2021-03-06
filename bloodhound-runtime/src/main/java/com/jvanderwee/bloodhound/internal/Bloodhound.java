package com.jvanderwee.bloodhound.internal;

import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jvanderwee.bloodhound.TrackEvent;
import com.jvanderwee.bloodhound.TrackScreen;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class Bloodhound {
    private static volatile boolean enabled = true;
    private static volatile Tracker tracker;

    @Pointcut("execution(@com.jvanderwee.bloodhound.TrackEvent * *(..))")
    public void trackEventMethod() {}

    @Pointcut("execution(@com.jvanderwee.bloodhound.TrackScreen * *(..))")
    public void trackScreenMethod() {}

    public static void setEnabled(boolean enabled) {
        Bloodhound.enabled = enabled;
    }

    public static void setTracker(Tracker tracker) {
        Bloodhound.tracker = tracker;
    }

    @Around("trackEventMethod() || trackScreenMethod()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);
        return joinPoint.proceed();
    }

    private static void enterMethod(JoinPoint joinPoint) {
        if (!enabled || tracker == null) return;

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
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(trackEvent.category())
                    .setAction(trackEvent.action())
                    .setLabel(trackEvent.label())
                    .build());
        }

        TrackScreen trackScreen = m.getAnnotation(TrackScreen.class);

        if (trackScreen != null) {
            Log.v(asTag(cls),
                    String.format("Tracking screen with name '%s'", trackScreen.value()));
            tracker.setScreenName(trackScreen.value());
            tracker.send(new HitBuilders.ScreenViewBuilder().build());

        }
    }

    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }
}