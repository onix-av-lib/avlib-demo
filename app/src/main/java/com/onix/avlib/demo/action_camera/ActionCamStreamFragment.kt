package com.onix.avlib.demo.action_camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.onix.avlib.*
import com.onix.avlib.ActionStreamerState.*
import com.onix.avlib.demo.ARG_SERVER_URL
import com.onix.avlib.demo.R
import com.onix.avlib.demo.action_camera.cam.GoPro3Hero
import com.onix.avlib.demo.databinding.FragmentActioncameraStreamingBinding
import com.onix.avlib.demo.util.ext.alert
import com.onix.avlib.demo.util.ext.argument
import com.onix.avlib.demo.util.ext.toast

class ActionCamStreamFragment : Fragment(), IActionCamStreamerEvents {

    private val serverUrl by argument<String>(ARG_SERVER_URL)
    private lateinit var binding: FragmentActioncameraStreamingBinding
    val streamer = ActionCameraStreamer(GoPro3Hero())
    // GoPro4Session()
    // GoPro4Plus()
    // GoPro4Silver()

    // visibility rules for controls
    val startVisibility = CamStateObservable(
        ERROR_INTERNET_CONNECTION, STREAM_STARTED, STREAM_STOPPED, PREVIEW_STARTED
    )

    val progressVisibility = CamStateObservable(
        PROCESSING_NETWORKS, PREVIEW_PREPARING
    )

    val refreshVisibility = CamStateObservable(
        ERROR_NETWORKS, ERROR_CAMERA_PREVIEW, ERROR_SERVER
    )

    companion object {
        fun instance(rtmpUrl: String): Fragment {
            return ActionCamStreamFragment().apply {
                arguments = bundleOf(ARG_SERVER_URL to rtmpUrl)
            }
        }
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        retainInstance = true
        streamer.onCreate(activity, state)
    }

    override fun onCreateView(inf: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        val view = inf.inflate(R.layout.fragment_actioncamera_streaming, parent, false)
        binding = FragmentActioncameraStreamingBinding.bind(view)
        binding.controller = this

        streamer.bindView(binding.cameraView)
        streamer.setStreamListener(this)
        return view
    }

    private fun updateControlPanel(event: ActionStreamerState) {
        when (event) {
            ERROR_NETWORKS -> alert(R.string.error_networks)
            ERROR_CAMERA_PREVIEW -> toast(R.string.error_camera)
            ERROR_INTERNET_CONNECTION -> toast(R.string.error_connection)
            ERROR_SERVER -> toast(R.string.error_server)
            else -> {}
        }

        binding.fps = 0

        startVisibility.set(event)
        refreshVisibility.set(event)
        progressVisibility.set(event)

        binding.invalidateAll()
    }

    override fun onStreamingFpsChanged(fps: Int) {
        binding.fps = fps
    }

    override fun onStreamStateChanged(event: ActionStreamerState) {
        updateControlPanel(event)
    }

    override fun onInitCompleted() {
        // customization
        // It make sense when streaming stopped
        streamer.setServerUrl(serverUrl)
        streamer.setVideoBitrate(VideoBitrate.B256K)
        streamer.setAudioBitrate(AudioBitrate.B32K)
    }

    override fun onInitStarted() {}

}
