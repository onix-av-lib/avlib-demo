package com.onix.streamer.demo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.onix.streamer.demo.actioncamera.ActionCamStreamActivity;
import com.onix.streamer.demo.activityimpl.SimpleStreamActivity;
import com.onix.streamer.demo.custom.CustomStreamActivity;
import com.onix.streamer.demo.databinding.ActivityDemoBinding;
import com.onix.streamer.demo.fragmentimpl.StreamActivity;

public class DemoActivity extends AppCompatActivity {

    public static final String ARG_SERVER_URL = "ARG_SERVER_URL";
    private ActivityDemoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_demo);
        mBinding.setEventHandler(this);
    }

    public void onActivityExample() {
        startExample(SimpleStreamActivity.class, getString(R.string.alert_network_with_internet_connection));
    }

    public void onFragmentExample() {
        startExample(StreamActivity.class, getString(R.string.alert_network_with_internet_connection));
    }

    public void onCustomExample() {
        startExample(CustomStreamActivity.class, getString(R.string.alert_network_with_internet_connection));
    }

    public void onCamExample() {
        startExample(ActionCamStreamActivity.class, getString(R.string.alert_multiple_networks));
    }


    public void startExample(final Class activityClass, String message) {

        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.continue_button, (dialogInterface, i) -> {

                    String serverUrl = mBinding.serverUrl.getText().toString();
                    if (TextUtils.isEmpty(serverUrl)) {
                        Toast.makeText(DemoActivity.this, R.string.alert_empty_url, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(DemoActivity.this, activityClass);
                    intent.putExtra(ARG_SERVER_URL, serverUrl);
                    startActivity(intent);
                })
                .show();
    }

}
