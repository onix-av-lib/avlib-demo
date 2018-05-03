package com.onix.streamer.demo.custom;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onix.avlib.AudioBitrate;
import com.onix.avlib.CamType;
import com.onix.avlib.CameraStreamer;
import com.onix.avlib.IStreamerEvents;
import com.onix.avlib.PreviewScaleType;
import com.onix.avlib.PreviewSize;
import com.onix.avlib.StreamerState;
import com.onix.avlib.VideoBitrate;
import com.onix.streamer.demo.DemoActivity;
import com.onix.streamer.demo.R;
import com.onix.streamer.demo.databinding.FragmentCustomStreamingBinding;
import com.onix.streamer.demo.util.CollectionUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CustomStreamFragment extends Fragment implements IStreamerEvents {

    private String mServerUrl;
    private FragmentCustomStreamingBinding mBinding;
    private CameraStreamer mStreamer;

    private ObservableBoolean mPreparing = new ObservableBoolean(true);

    private final List<AudioBitrate> mAudioBitrates = Arrays.asList(
            AudioBitrate.B32K, AudioBitrate.B64K, AudioBitrate.B128K, AudioBitrate.B256K);

    private final List<VideoBitrate> mVideoBitrates = Arrays.asList(
            VideoBitrate.B128K, VideoBitrate.B256K, VideoBitrate.B512K, VideoBitrate.B1024K, VideoBitrate.B2048K, VideoBitrate.B3072K, VideoBitrate.B4096K, VideoBitrate.B6144K);

    public static Fragment newInstance(String rtmpUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(DemoActivity.ARG_SERVER_URL, rtmpUrl);
        Fragment fragment = new CustomStreamFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServerUrl = getArguments().getString(DemoActivity.ARG_SERVER_URL);
        // need for rotation
        setRetainInstance(true);

        mStreamer = new CameraStreamer(CamType.MAIN);
        mStreamer.enableLogs();
        mStreamer.onCreate(getActivity(), savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_streaming, container, false);
        mBinding = FragmentCustomStreamingBinding.bind(view);

        mBinding.setController(this);
        mBinding.tuneContainer.setController(this);

        mStreamer.onCreateView(mBinding.cameraView, PreviewScaleType.CENTER_CROP);
        return view;
    }

    public void onPreviewSizeChanged() {
        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(convertSizes(mStreamer.getAvailablePreviewSizes()),
                        mStreamer.getAvailablePreviewSizes().indexOf(mStreamer.getCurrentPreviewSize()),
                        (dialogInterface, index) -> {
                            dialogInterface.dismiss();
                            mStreamer.setPreviewSize(mStreamer.getAvailablePreviewSizes().get(index));
                            mBinding.invalidateAll();
                        }).show();
    }


    public void onVideoBitrateChanged() {
        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(CollectionUtil.convertToList(mVideoBitrates, VideoBitrate::toString).toArray(new String[0]),
                        mVideoBitrates.indexOf(mStreamer.getVideoBitrate()),
                        (dialogInterface, index) -> {
                            dialogInterface.dismiss();
                            mStreamer.setVideoBitrate(mVideoBitrates.get(index));
                            mBinding.invalidateAll();
                        }).show();
    }

    public void onAudioBitrateChanged() {
        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(CollectionUtil.convertToList(mAudioBitrates, AudioBitrate::toString).toArray(new String[0]),
                        mAudioBitrates.indexOf(mStreamer.getAudioBitrate()),
                        (dialogInterface, index) -> {
                            dialogInterface.dismiss();
                            mStreamer.setAudioBitrate(mAudioBitrates.get(index));
                            mBinding.invalidateAll();
                        }).show();
    }

    public void onCameraSwitch() {
        mStreamer.switchCamera();
        mBinding.invalidateAll();
    }

    private String[] convertSizes(List<PreviewSize> items) {
        String[] converted = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            converted[i] = String.format(Locale.getDefault(),
                    getContext().getString(R.string.preview_size_pattern), items.get(i).getWidth(), items.get(i).getHeight());
        }
        return converted;
    }

    @Override
    public void onPause() {
        super.onPause();
        mStreamer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mStreamer.onResume(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStreamer.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mStreamer.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStreamer.onSaveInstanceState(outState);
    }

    private void showError(final String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPreviewSizeSelected(PreviewSize size) {
    }

    @Override
    public void onPreviewSizesAvailable(List<PreviewSize> sizes) {
    }

    @Override
    public void onStreamingFpsChanged(int fps) {
        mBinding.setFps(fps);
    }

    @Override
    public void onStreamStateChanged(StreamerState event) {
        switch (event) {
            case ERROR_CONNECTION:
                showError(getString(R.string.error_connection));
                break;
            case ERROR_CAMERA_BUSY:
                showError(getString(R.string.error_camera));
                break;
            case ERROR_SERVER:
                showError(getString(R.string.error_server));
                break;
            case ERROR_PERMISSIONS:
                showError(getString(R.string.error_permissions));
                break;
            case STREAM_STARTED:
            case STREAM_STOPPED:
                mBinding.setFps(0);
        }

        mBinding.invalidateAll();
    }

    @Override
    public void onInitCompleted() {

        // record stream to file example
        String fileName = getActivity().getExternalFilesDir(null).getAbsolutePath() + "/test.mp4";
        mStreamer.setRecordFile(fileName);

        mStreamer.setServerUrl(mServerUrl);
        mPreparing.set(false);
        mBinding.invalidateAll();
    }

    @Override
    public void onInitStarted() {
        mPreparing.set(true);
    }

    // binding
    public CameraStreamer getStreamer() {
        return mStreamer;
    }

    public ObservableBoolean isPreparing() {
        return mPreparing;
    }
}
