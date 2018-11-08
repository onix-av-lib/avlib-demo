package com.onix.streamer.demo.screenactivity;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.onix.avlib.AudioBitrate;
import com.onix.avlib.IScreenStreamerEvents;
import com.onix.avlib.PreviewSize;
import com.onix.avlib.ScreenStreamer;
import com.onix.avlib.ScreenStreamerEvent;
import com.onix.avlib.VideoBitrate;
import com.onix.streamer.demo.DemoActivity;
import com.onix.streamer.demo.R;
import com.onix.streamer.demo.databinding.ActivityScreenStreamBinding;
import com.onix.streamer.demo.util.Notifications;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ScreenStreamActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, IScreenStreamerEvents {

    private static final String TAG = "ScreenStreamActivity";

    private static final int CAM_AUDIO_REQUEST_CODE = 668;
    private String mServerUrl;
    private ActivityScreenStreamBinding mBinding;

    private ScreenStreamer mStreamer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStreamer = new ScreenStreamer();
        mStreamer.enableLogs();
        mServerUrl = getIntent().getStringExtra(DemoActivity.ARG_SERVER_URL);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_screen_stream);
        mBinding.setStreamer(mStreamer);

        mStreamer.onCreate(this);
        mStreamer.setNotification(Notifications.getNotification(getApplicationContext()));

        checkPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStreamer.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStreamer.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStreamer.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mStreamer.onActivityResult(requestCode, resultCode, data);
    }


    private void handleStateChanged(ScreenStreamerEvent event) {
        switch (event) {
            case STREAM_STARTED:
                Toast.makeText(this, R.string.recording, Toast.LENGTH_SHORT).show();
            case STREAM_STOPPED:
                mBinding.invalidateAll();
                break;

            case ERROR_DISPLAY_NOT_FOUND: {
                Toast.makeText(this, R.string.error_display_not_found, Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_PERMISSIONS: {
                Toast.makeText(this, R.string.error_permissions, Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_SERVER:
            case ERROR_CONNECTION: {
                Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
                mBinding.invalidateAll();
                break;
            }
        }
    }

    @Override
    public void onStreamStateChanged(ScreenStreamerEvent event) {
        Log.d(TAG, "onStreamStateChanged: " + event.toString());
        handleStateChanged(event);
    }

    @Override
    public void onInited(boolean isStreaming) {
        // setup streaming settings
        // it make sense only when streaming not started

        mStreamer.setServerUrl(mServerUrl);
        mStreamer.setAudioBitrate(AudioBitrate.B64K);
        mStreamer.setVideoBitrate(VideoBitrate.B4096K);
        mStreamer.setVideoBitrate(VideoBitrate.B2048K);
        mStreamer.setSize(new PreviewSize(1578, 960));

        String fileName = getExternalFilesDir(null).getAbsolutePath() + "/test.mp4";
        mStreamer.setRecordFile(fileName);
    }

    // **************
    // handle permissions
    // **************

    @AfterPermissionGranted(CAM_AUDIO_REQUEST_CODE)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET};
            if (!EasyPermissions.hasPermissions(this, perms)) {
                EasyPermissions.requestPermissions(this, getString(R.string.alert_permissions), CAM_AUDIO_REQUEST_CODE, perms);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.alert_permissions_denied_message)
                .setCancelable(false)
                .setPositiveButton(R.string.close, (dialog, which) -> finish())
                .show();
    }

}
