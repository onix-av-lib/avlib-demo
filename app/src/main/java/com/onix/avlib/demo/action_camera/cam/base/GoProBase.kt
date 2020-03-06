package com.onix.avlib.demo.action_camera.cam.base

import com.onix.avlib.IActionCamera

abstract class GoProBase : IActionCamera {

    override fun onResume() {}

    override fun onPause() {}

    abstract override fun getPreviewUrl(): String?
}
