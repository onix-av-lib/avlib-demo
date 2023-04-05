package com.onix.avlib.demo.activity_impl

import android.Manifest
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.onix.avlib.demo.util.permissions.runWithPermissions
import com.onix.avlib.demo.util.permissions.util.QuickPermissionsOptions
import com.onix.avlib.demo.util.permissions.util.QuickPermissionsRequest
import com.onix.avlib.*
import com.onix.avlib.demo.ARG_SERVER_URL
import com.onix.avlib.demo.R
import com.onix.avlib.demo.databinding.ActivityCameraStreamBinding
import com.onix.avlib.demo.util.ext.argument
import com.onix.avlib.demo.util.ext.toast

class SimpleStreamActivity : AppCompatActivity(), IStreamerEvents {

    private val serverUrl by argument<String>(ARG_SERVER_URL)
    private lateinit var binding: ActivityCameraStreamBinding
    private val streamer = CameraStreamer().apply { enableLogs() }
    private var permissionsAlert: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_stream)

        binding.streamer = streamer

        streamer.onCreate(this, savedInstanceState)
        streamer.bindView(binding.cameraView, PreviewScaleType.CENTER_CROP)
        streamer.setStreamListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        streamer.onSaveInstanceState(outState)
    }

    // streamer callbacks
    // ******************
    override fun onPreviewSizeSelected(size: PreviewSize) {
        binding.previewSize = size
    }

    override fun onPreviewSizesAvailable(sizes: List<PreviewSize>) {}

    override fun onStreamingFpsChanged(fps: Int) {
        binding.fps = fps
    }

    override fun onStreamStateChanged(event: StreamerState) {
        when (event) {
            StreamerState.ERROR_CONNECTION -> toast(R.string.error_connection)
            StreamerState.ERROR_CAMERA_BUSY -> toast(R.string.error_camera)
            StreamerState.ERROR_SERVER -> toast(R.string.error_server)
            StreamerState.ERROR_PERMISSIONS -> checkPermissions()
            StreamerState.STREAM_STARTED, StreamerState.STREAM_STOPPED -> binding.fps = 0
        }

        binding.invalidateAll()
    }

    override fun onInitStarted() {}

    override fun onInitCompleted() {
        // customization
        // It make sense before streaming started
        streamer.setServerUrl(serverUrl)
    }

    // allow permissions
    // *****************
    private fun checkPermissions() {
        if (permissionsAlert?.isShowing != true) {
            val options = QuickPermissionsOptions(
                handleRationale = false,
                rationaleMessage = getString(R.string.alert_permissions),
                permanentlyDeniedMessage = getString(R.string.alert_permissions_denied_message),
                permissionsDeniedMethod = ::onPermissionDenied,
                permanentDeniedMethod = ::onPermissionDenied
            )

            runWithPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                options = options,
                callback = {}
            )
        }
    }

    private fun onPermissionDenied(request: QuickPermissionsRequest) {
        permissionsAlert = AlertDialog.Builder(this)
            .setMessage(R.string.alert_permissions_denied_message)
            .setCancelable(false)
            .setPositiveButton(R.string.close) { _, _ -> finish() }
            .show()
    }

}
