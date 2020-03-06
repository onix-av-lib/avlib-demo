package com.onix.avlib.demo.action_camera.cam

import android.text.TextUtils

import com.onix.avlib.demo.action_camera.cam.base.GoPro4
import com.onix.avlib.demo.util.IoUtil


class GoPro4Silver : GoPro4() {

    override fun getPreviewUrl(): String? {
        return if (!TextUtils.isEmpty(IoUtil.getSyncHttp(CMD_START_PREVIEW))) {
            PREVIEW_URL
        } else {
            null
        }
    }

    companion object {

        private val CMD_START_PREVIEW =
            "http://10.5.5.9/gp/gpControl/execute?p1=gpStream&c1=restart"
        private val PREVIEW_URL = "udp://10.5.5.9:8554"
    }
}
