package com.example.hugo;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import com.jvanderwee.tracker.TrackEvent;

public class TrackerActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Check logcat!");
        setContentView(tv);

        printArgs("The", "Quick", "Brown", "Fox");

        Log.i("Fibonacci", "fibonacci's 4th number is " + fibonacci(4));

        Greeter greeter = new Greeter("Jake");
        Log.d("Greeting", greeter.sayHello());

        Charmer charmer = new Charmer("Jake");
        Log.d("Charming", charmer.askHowAreYou());

        startSleepyThread();
    }

    @TrackEvent
    private void printArgs(String... args) {
        for (String arg : args) {
            Log.i("Args", arg);
        }
    }

    @TrackEvent
    private int fibonacci(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Number must be greater than zero.");
        }
        if (number == 1 || number == 2) {
            return 1;
        }
        // NOTE: Don't ever do this. Use the iterative approach!
        return fibonacci(number - 1) + fibonacci(number - 2);
    }

    private void startSleepyThread() {
        new Thread(new Runnable() {
            private static final long SOME_POINTLESS_AMOUNT_OF_TIME = 50;

            @Override public void run() {
                sleepyMethod(SOME_POINTLESS_AMOUNT_OF_TIME);
            }

            @TrackEvent
            private void sleepyMethod(long milliseconds) {
                SystemClock.sleep(milliseconds);
            }
        }, "I'm a lazy thr.. bah! whatever!").start();
    }

    static class Greeter {
        private final String name;

        @TrackEvent
        Greeter(String name) {
            this.name = name;
        }

        @TrackEvent
        public String sayHello() {
            return "Hello, " + name;
        }
    }

    @TrackEvent
    static class Charmer {
        private final String name;

        Charmer(String name) {
            this.name = name;
        }

        public String askHowAreYou() {
            return "How are you " + name + "?";
        }
    }
}