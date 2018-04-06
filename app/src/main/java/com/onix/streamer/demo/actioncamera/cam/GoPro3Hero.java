package com.onix.streamer.demo.actioncamera.cam;

import android.text.TextUtils;

import com.onix.streamer.demo.actioncamera.cam.base.GoProBase;
import com.onix.streamer.demo.util.IoUtil;

public class GoPro3Hero extends GoProBase {

    private static String CMD_START_PREVIEW = "http://10.5.5.9/camera/PV?t=onixgopro&p=%02";
    private static String PREVIEW_URL = "http://10.5.5.9:8080/live/amba.m3u8";

    @Override
    public String getPreviewUrl() {
        if (!TextUtils.isEmpty(IoUtil.getSyncHttp(CMD_START_PREVIEW))) {
            return PREVIEW_URL;
        } else {
            return null;
        }
    }
}
