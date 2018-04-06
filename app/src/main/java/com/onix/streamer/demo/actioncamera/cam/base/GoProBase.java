package com.onix.streamer.demo.actioncamera.cam.base;

import com.onix.avlib.IActionCamera;

public abstract class GoProBase implements IActionCamera {

    public void onResume() {
    }

    public void onPause() {
    }

    public abstract String getPreviewUrl();
}
