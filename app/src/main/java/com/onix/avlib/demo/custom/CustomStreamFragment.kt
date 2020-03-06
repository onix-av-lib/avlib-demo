package com.onix.avlib.demo.custom

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import com.onix.avlib.*
import com.onix.avlib.demo.ARG_SERVER_URL
import com.onix.avlib.demo.R
import com.onix.avlib.demo.databinding.FragmentCustomStreamingBinding
import com.onix.avlib.demo.util.ext.argument
import com.onix.avlib.demo.util.ext.toast
import java.util.*

class CustomStreamFragment : Fragment(), IStreamerEvents {

    private val serverUrl by argument<String>(ARG_SERVER_URL)
    private lateinit var binding: FragmentCustomStreamingBinding
    val streamer = CameraStreamer(CamType.MAIN).apply { enableLogs() }
    val isPreparing = ObservableBoolean(true)

    companion object {
        fun instance(rtmpUrl: String): Fragment {
            return CustomStreamFragment().apply {
                arguments = bundleOf(ARG_SERVER_URL to rtmpUrl)
            }
        }
    }

    private val mAudioBitrates = listOf(
        AudioBitrate.B32K, AudioBitrate.B64K, AudioBitrate.B128K, AudioBitrate.B256K
    )

    private val mVideoBitrates = listOf(
        VideoBitrate.B128K, VideoBitrate.B256K, VideoBitrate.B512K, VideoBitrate.B1024K,
        VideoBitrate.B2048K, VideoBitrate.B3072K, VideoBitrate.B4096K, VideoBitrate.B6144K,
        VideoBitrate.B12288K, VideoBitrate.B24576K
    )

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        retainInstance = true
        streamer.onCreate(activity, state)
    }

    override fun onCreateView(inf: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        val view = inf.inflate(R.layout.fragment_custom_streaming, parent, false)
        binding = FragmentCustomStreamingBinding.bind(view)
        binding.controller = this

        streamer.bindView(binding.cameraView, PreviewScaleType.CENTER_CROP)
        streamer.setStreamListener(this)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        streamer.onSaveInstanceState(outState)
    }

    fun onPreviewSizeChanged() {
        activity?.let { activity ->

            val sizes = streamer.availablePreviewSizes.map {
                String.format(
                    Locale.getDefault(),
                    activity.getString(R.string.preview_size_pattern),
                    it.width, it.height
                )
            }.toTypedArray()

            val checkedItem = streamer.availablePreviewSizes.indexOf(streamer.currentPreviewSize)

            AlertDialog.Builder(activity)
                .setSingleChoiceItems(sizes, checkedItem) { dialog, index ->
                    dialog.dismiss()
                    streamer.setPreviewSize(streamer.availablePreviewSizes[index])
                    binding.invalidateAll()
                }.show()
        }
    }


    fun onVideoBitrateChanged() {
        activity?.let { activity ->
            val bitrates = mVideoBitrates.map { it.toString() }.toTypedArray()
            val checkedItem = mVideoBitrates.indexOf(streamer.videoBitrate)

            AlertDialog.Builder(activity)
                .setSingleChoiceItems(bitrates, checkedItem) { dialogInterface, index ->
                    dialogInterface.dismiss()
                    streamer.videoBitrate = mVideoBitrates[index]
                    binding.invalidateAll()
                }.show()
        }
    }

    fun onAudioBitrateChanged() {

        activity?.let { activity ->
            val bitrates = mAudioBitrates.map { it.toString() }.toTypedArray()
            val checkedItem = mAudioBitrates.indexOf(streamer.audioBitrate)

            AlertDialog.Builder(activity)
                .setSingleChoiceItems(bitrates, checkedItem) { dialogInterface, index ->
                    dialogInterface.dismiss()
                    streamer.audioBitrate = mAudioBitrates[index]
                    binding.invalidateAll()
                }.show()
        }
    }

    fun onCameraSwitch() {
        streamer.switchCamera()
        binding.invalidateAll()
    }

    override fun onPreviewSizeSelected(size: PreviewSize) {
        binding.invalidateAll()
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
        streamer.setServerUrl(serverUrl)
        // uncomment for record stream to file
        //val fileName = activity!!.getExternalFilesDir(null)!!.absolutePath + "/test.mp4"
        //streamer.setRecordFile(fileName)
        isPreparing.set(false)
        binding.invalidateAll()
    }

    override fun onInitStarted() {
        isPreparing.set(true)
    }

}
