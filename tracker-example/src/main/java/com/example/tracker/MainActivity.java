package com.example.tracker;

import android.app.Activity;
import android.os.Bundle;

import com.jvanderwee.tracker.TrackEvent;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @TrackEvent
    @OnClick(R.id.button)
    void buttonClicked() {
    }
}