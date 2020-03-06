package com.onix.avlib.demo.action_camera.cam

import android.text.TextUtils

import com.onix.avlib.demo.action_camera.cam.base.GoProBase
import com.onix.avlib.demo.util.IoUtil


class GoPro3Hero : GoProBase() {

    override fun getPreviewUrl(): String? {
        return if (!TextUtils.isEmpty(IoUtil.getSyncHttp(CMD_START_PREVIEW))) {
            PREVIEW_URL
        } else {
            null
        }
    }

    companion object {

        private val CMD_START_PREVIEW = "http://10.5.5.9/camera/PV?t=onixgopro&p=%02"
        private val PREVIEW_URL = "http://10.5.5.9:8080/live/amba.m3u8"
    }
}
