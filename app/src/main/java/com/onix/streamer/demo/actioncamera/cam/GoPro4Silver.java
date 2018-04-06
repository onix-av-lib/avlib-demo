package com.onix.streamer.demo.actioncamera.cam;

import android.text.TextUtils;

import com.onix.streamer.demo.actioncamera.cam.base.GoPro4;
import com.onix.streamer.demo.util.IoUtil;

public class GoPro4Silver extends GoPro4 {

    private static String CMD_START_PREVIEW = "http://10.5.5.9/gp/gpControl/execute?p1=gpStream&c1=restart";
    private static String PREVIEW_URL = "udp://10.5.5.9:8554";

    @Override
    public String getPreviewUrl() {
        if (!TextUtils.isEmpty(IoUtil.getSyncHttp(CMD_START_PREVIEW))) {
            return PREVIEW_URL;
        } else {
            return null;
        }
    }
}
