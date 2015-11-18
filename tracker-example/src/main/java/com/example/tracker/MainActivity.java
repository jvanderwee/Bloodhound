package com.example.tracker;

import android.app.Activity;
import android.os.Bundle;

import com.jvanderwee.tracker.TrackEvent;
import com.jvanderwee.tracker.TrackScreen;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @TrackScreen("Screen Name")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @TrackEvent(category = "Category", action = "Action", label = "Label")
    @OnClick(R.id.button)
    protected void buttonClicked() {
    }
}