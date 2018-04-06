package com.onix.streamer.demo.actioncamera.cam;

import android.text.TextUtils;

import com.onix.streamer.demo.actioncamera.cam.base.GoPro4;
import com.onix.streamer.demo.util.IoUtil;

import static com.onix.streamer.demo.util.IoUtil.getSyncHttp;

public class GoPro4Session extends GoPro4 {

    private static String CMD_START_PREVIEW = "http://10.5.5.9/gp/gpControl/execute?p1=gpStream&c1=restart";
    private static String CMD_GET_STATUS = "http://10.5.5.9/gp/gpControl/status";
    private static String PREVIEW_URL = "udp://10.5.5.9:8554";

    @Override
    public String getPreviewUrl() {

        // for test only
        String startPreviewResponse = IoUtil.getSyncHttp(CMD_START_PREVIEW);
        for (int i = 0; i < 3; i++) {
            getSyncHttp(CMD_GET_STATUS);
        }
        // ****

        if (!TextUtils.isEmpty(startPreviewResponse)) {
            return PREVIEW_URL;
        } else {
            return null;
        }
    }
}
