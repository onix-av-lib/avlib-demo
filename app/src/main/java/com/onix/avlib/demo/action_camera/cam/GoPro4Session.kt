package com.onix.avlib.demo.action_camera.cam

import android.text.TextUtils

import com.onix.avlib.demo.action_camera.cam.base.GoPro4
import com.onix.avlib.demo.util.IoUtil


open class GoPro4Session : GoPro4() {

    override fun getPreviewUrl(): String? {

        // for test only
        val startPreviewResponse = IoUtil.getSyncHttp(CMD_START_PREVIEW)
        for (i in 0..2) {
            IoUtil.getSyncHttp(CMD_GET_STATUS)
        }
        // ****

        return if (!TextUtils.isEmpty(startPreviewResponse)) {
            PREVIEW_URL
        } else {
            null
        }
    }

    companion object {

        private val CMD_START_PREVIEW =
            "http://10.5.5.9/gp/gpControl/execute?p1=gpStream&c1=restart"
        private val CMD_GET_STATUS = "http://10.5.5.9/gp/gpControl/status"
        private val PREVIEW_URL = "udp://10.5.5.9:8554"
    }
}
