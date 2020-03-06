package com.onix.avlib.demo.screen_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.onix.avlib.*
import com.onix.avlib.demo.ARG_SERVER_URL
import com.onix.avlib.demo.R
import com.onix.avlib.demo.databinding.FragmentScreenStreamBinding
import com.onix.avlib.demo.util.ext.argument
import com.onix.avlib.demo.util.ext.toast

class ScreenStreamFragment : Fragment(), IScreenStreamerEvents {

    private val serverUrl by argument<String>(ARG_SERVER_URL)
    private lateinit var binding: FragmentScreenStreamBinding
    private var streamer = ScreenStreamer()

    companion object {
        fun instance(rtmpUrl: String): Fragment {
            return ScreenStreamFragment().apply { arguments = bundleOf(ARG_SERVER_URL to rtmpUrl) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        streamer.onCreate(activity!!)
        streamer.setStreamListener(this)
    }

    override fun onCreateView(inf: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        val view = inf.inflate(R.layout.fragment_screen_stream, parent, false)
        binding = FragmentScreenStreamBinding.bind(view)
        binding.streamer = streamer
        return view
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

    override fun onStreamStateChanged(event: ScreenStreamerEvent) = handleStateChanged(event)

    override fun onInited(isStreaming: Boolean) {
        // setup streaming settings
        // it make sense only before stream not started

        //mStreamer.enableAudio(false);
        streamer.setServerUrl(serverUrl)
        streamer.audioBitrate = AudioBitrate.B64K
        streamer.videoBitrate = VideoBitrate.B6144K
        streamer.size = PreviewSize(1280, 960)
    }
}
