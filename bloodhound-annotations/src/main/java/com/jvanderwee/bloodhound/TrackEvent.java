package com.jvanderwee.bloodhound;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface TrackEvent {
    String category();
    String action();
    String label();
}
