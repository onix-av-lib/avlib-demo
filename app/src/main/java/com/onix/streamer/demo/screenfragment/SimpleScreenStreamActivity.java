package com.onix.streamer.demo.screenfragment;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.onix.streamer.demo.DemoActivity;
import com.onix.streamer.demo.R;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SimpleScreenStreamActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "ScreenStreamActivity";
    private static final int CAM_AUDIO_REQUEST_CODE = 668;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_screen_stream);
        checkPermissions();
    }

    protected void openScreenStreamFragment() {
        String tag = ScreenStreamFragment.class.getSimpleName();
        FragmentManager fm = getSupportFragmentManager();
        Fragment streamFragment = fm.findFragmentByTag(tag);

        if (streamFragment == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    ScreenStreamFragment.newInstance(getIntent().getStringExtra(DemoActivity.ARG_SERVER_URL)), tag).commit();
        }
    }

    @AfterPermissionGranted(CAM_AUDIO_REQUEST_CODE)
    protected void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET};
            if (EasyPermissions.hasPermissions(this, perms)) {
                openScreenStreamFragment();
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.alert_permissions), CAM_AUDIO_REQUEST_CODE, perms);
            }
        } else {
            openScreenStreamFragment();
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
