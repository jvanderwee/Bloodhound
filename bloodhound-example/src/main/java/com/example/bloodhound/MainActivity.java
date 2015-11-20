package com.example.bloodhound;

import android.app.Activity;
import android.os.Bundle;

import com.example.tracker.R;
import com.jvanderwee.bloodhound.TrackEvent;
import com.jvanderwee.bloodhound.TrackScreen;

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