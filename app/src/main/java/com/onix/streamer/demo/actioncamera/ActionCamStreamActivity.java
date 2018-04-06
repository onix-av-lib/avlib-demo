package com.onix.streamer.demo.actioncamera;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.onix.streamer.demo.DemoActivity;
import com.onix.streamer.demo.R;

public class ActionCamStreamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streamer);

        String tag = ActionCamStreamFragment.class.getSimpleName();
        FragmentManager fm = getSupportFragmentManager();

        if (fm.findFragmentByTag(tag) == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    ActionCamStreamFragment.newInstance(getIntent().getStringExtra(DemoActivity.ARG_SERVER_URL)), tag).commitAllowingStateLoss();
        }
    }
}
