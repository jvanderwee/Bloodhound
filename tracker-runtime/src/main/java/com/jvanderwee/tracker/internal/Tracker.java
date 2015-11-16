package com.jvanderwee.tracker.internal;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

@Aspect
public class Tracker {
    private static volatile boolean enabled = true;

    @Pointcut("within(@com.jvanderwee.tracker.TrackEvent *)")
    public void withinAnnotatedClass() {}

    @Pointcut("execution(* *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {}

    @Pointcut("execution(*.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {}

    @Pointcut("execution(@com.jvanderwee.tracker.TrackEvent * *(..)) || methodInsideAnnotatedType()")
    public void method() {}

    @Pointcut("execution(@com.jvanderwee.tracker.TrackEvent *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {}

    public static void setEnabled(boolean enabled) {
        Tracker.enabled = enabled;
    }

    @Around("method() || constructor()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);
        return joinPoint.proceed();
    }

    private static void enterMethod(JoinPoint joinPoint) {
        if (!enabled) return;

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType();

        Log.v(asTag(cls), "Tracking");
    }

    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }
}