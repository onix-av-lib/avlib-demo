package com.onix.avlib.demo.fragment_impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.onix.avlib.*
import com.onix.avlib.demo.ARG_SERVER_URL
import com.onix.avlib.demo.R
import com.onix.avlib.demo.databinding.FragmentCameraStreamingBinding
import com.onix.avlib.demo.util.ext.argument
import com.onix.avlib.demo.util.ext.toast

class StreamFragment : Fragment(), IStreamerEvents {

    private val serverUrl by argument<String>(ARG_SERVER_URL)
    private lateinit var binding: FragmentCameraStreamingBinding
    private val streamer = CameraStreamer()

    companion object {
        fun instance(rtmpUrl: String): Fragment {
            return StreamFragment().apply { arguments = bundleOf(ARG_SERVER_URL to rtmpUrl) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        streamer.onCreate(activity, savedInstanceState)
    }

    override fun onCreateView(inf: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        val view = inf.inflate(R.layout.fragment_camera_streaming, parent, false)
        binding = FragmentCameraStreamingBinding.bind(view)
        binding.streamer = streamer

        streamer.bindView(binding.cameraView, PreviewScaleType.CENTER_CROP)
        streamer.setStreamListener(this)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        streamer.onSaveInstanceState(outState)
    }

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
            StreamerState.ERROR_PERMISSIONS -> toast(R.string.error_permissions)
            StreamerState.STREAM_STARTED, StreamerState.STREAM_STOPPED -> binding.fps = 0
        }

        binding.invalidateAll()
    }

    override fun onInitCompleted() {
        // customization
        streamer.setServerUrl(serverUrl)
    }

    override fun onInitStarted() {}

}
