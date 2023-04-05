package com.onix.avlib.demo.screen_activity

import android.Manifest
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
import com.onix.avlib.demo.databinding.ActivityScreenStreamBinding
import com.onix.avlib.demo.util.Notifications
import com.onix.avlib.demo.util.ext.argument
import com.onix.avlib.demo.util.ext.toast


class ScreenStreamActivity : AppCompatActivity(), IScreenStreamerEvents {

    private val serverUrl by argument<String>(ARG_SERVER_URL)
    private lateinit var binding: ActivityScreenStreamBinding
    private val streamer = ScreenStreamer().apply { enableLogs() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_screen_stream)
        binding.streamer = streamer

        binding.start.setOnClickListener {
            //mStreamer.setPreparing(true, "Preparing...");
            if (streamer.isStreaming) {
                streamer.stopStream()
            } else {
                streamer.startStream()
            }
        }

        streamer.onCreate(this)
        streamer.setNotification(Notifications.getNotification(applicationContext))
        streamer.setStreamListener(this)

        checkPermissions()
    }

    private fun handleStateChanged(event: ScreenStreamerEvent) {
        when (event) {
            ScreenStreamerEvent.STREAM_STARTED -> {
                toast(R.string.alert_recording)
                binding.invalidateAll()
            }
            ScreenStreamerEvent.STREAM_STOPPED -> binding.invalidateAll()
            ScreenStreamerEvent.ERROR_DISPLAY_NOT_FOUND -> toast(R.string.error_display_not_found)
            ScreenStreamerEvent.ERROR_PERMISSIONS -> toast(R.string.error_permissions)
            ScreenStreamerEvent.ERROR_SERVER, ScreenStreamerEvent.ERROR_CONNECTION -> {
                toast(R.string.error_connection)
                binding.invalidateAll()
            }
        }
    }

    override fun onStreamStateChanged(event: ScreenStreamerEvent) {
        handleStateChanged(event)
    }

    override fun onInited(isStreaming: Boolean) {
        // setup streaming settings
        // it make sense only when streaming not started

        streamer.setServerUrl(serverUrl)
        streamer.audioBitrate = AudioBitrate.B64K
        streamer.videoBitrate = VideoBitrate.B4096K
        streamer.size = PreviewSize(1440, 720)

        val fileName = getExternalFilesDir(null)!!.absolutePath + "/test_screen.mp4"
        streamer.setRecordFile(fileName)
    }

    // **************
    // handle permissions
    // *************

    private fun checkPermissions() {
        val permissionHandleOptions = QuickPermissionsOptions(
            handleRationale = false,
            rationaleMessage = getString(R.string.alert_permissions),
            permanentlyDeniedMessage = getString(R.string.alert_permissions_denied_message),
            permissionsDeniedMethod = ::onPermissionDenied,
            permanentDeniedMethod = ::onPermissionDenied
        )
        runWithPermissions(
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
            options = permissionHandleOptions
        ) {}
    }

    private fun onPermissionDenied(request: QuickPermissionsRequest) {
        AlertDialog.Builder(this)
            .setMessage(R.string.alert_permissions_denied_message)
            .setCancelable(false)
            .setPositiveButton(R.string.close) { _, _ -> finish() }
            .show()
    }

}
