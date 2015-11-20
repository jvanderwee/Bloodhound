package com.example.bloodhound;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.jvanderwee.bloodhound.internal.Bloodhound;

public class BloodhoundApplication extends Application {

    private Tracker tracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            tracker = analytics.newTracker("yourTrackingId");
        }
        return tracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Bloodhound.setTracker(getDefaultTracker());
    }
}
