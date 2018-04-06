package com.onix.streamer.demo.activityimpl;

import android.Manifest;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.onix.avlib.CameraStreamer;
import com.onix.avlib.IStreamerEvents;
import com.onix.avlib.PreviewScaleType;
import com.onix.avlib.PreviewSize;
import com.onix.avlib.StreamerState;
import com.onix.streamer.demo.DemoActivity;
import com.onix.streamer.demo.R;
import com.onix.streamer.demo.databinding.ActivityCameraStreamBinding;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SimpleStreamActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, IStreamerEvents {


    private static final int CAM_AUDIO_INTERNET_REQUEST_CODE = 668;

    private String mServerUrl;
    private ActivityCameraStreamBinding mBinding;
    private CameraStreamer mStreamer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServerUrl = getIntent().getStringExtra(DemoActivity.ARG_SERVER_URL);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera_stream);
        mStreamer = new CameraStreamer();
        mBinding.setStreamer(mStreamer);

        mStreamer.onCreate(this, savedInstanceState);
        mStreamer.onCreateView(mBinding.cameraView, PreviewScaleType.CENTER_CROP);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStreamer.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mStreamer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mStreamer.onResume(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStreamer.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mStreamer.onConfigurationChanged(newConfig);
    }

    private void showError(final String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPreviewSizeSelected(PreviewSize size) {
        mBinding.setPreviewSize(size);
    }

    @Override
    public void onPreviewSizesAvailable(List<PreviewSize> sizes) {
    }

    @Override
    public void onStreamingFpsChanged(int fps) {
        mBinding.setFps(fps);
    }

    @Override
    public void onStreamStateChanged(StreamerState event) {

        switch (event) {
            case ERROR_CONNECTION:
                showError(getString(R.string.error_connection));
                break;
            case ERROR_CAMERA_BUSY:
                showError(getString(R.string.error_camera));
                break;
            case ERROR_SERVER:
                showError(getString(R.string.error_server));
                break;
            case ERROR_PERMISSIONS:
                checkPermissions();
                break;
            case STREAM_STARTED:
            case STREAM_STOPPED:
                mBinding.setFps(0);
        }

        mBinding.invalidateAll();
    }

    @Override
    public void onInitCompleted() {
        // customization
        // It make sense when streaming stopped
        mStreamer.setServerUrl(mServerUrl);
    }

    @Override
    public void onInitStarted() {
    }


    // **************
    // handle permissions
    // **************

    @AfterPermissionGranted(CAM_AUDIO_INTERNET_REQUEST_CODE)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET};
            if (!EasyPermissions.hasPermissions(this, perms)) {
                EasyPermissions.requestPermissions(this, getString(R.string.alert_permissions), CAM_AUDIO_INTERNET_REQUEST_CODE, perms);
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
