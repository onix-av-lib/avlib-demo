package com.onix.streamer.demo.actioncamera;

import android.content.res.Configuration;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onix.avlib.ActionCameraStreamer;
import com.onix.avlib.ActionStreamerState;
import com.onix.avlib.AudioBitrate;
import com.onix.avlib.IActionCamStreamerEvents;
import com.onix.avlib.VideoBitrate;
import com.onix.streamer.demo.DemoActivity;
import com.onix.streamer.demo.R;
import com.onix.streamer.demo.actioncamera.binding.CamStateObservable;
import com.onix.streamer.demo.actioncamera.cam.GoPro3Hero;
import com.onix.streamer.demo.databinding.FragmentActioncameraStreamingBinding;

import static com.onix.avlib.ActionStreamerState.ERROR_CAMERA_PREVIEW;
import static com.onix.avlib.ActionStreamerState.ERROR_INTERNET_CONNECTION;
import static com.onix.avlib.ActionStreamerState.ERROR_NETWORKS;
import static com.onix.avlib.ActionStreamerState.ERROR_SERVER;
import static com.onix.avlib.ActionStreamerState.PREVIEW_PREPARING;
import static com.onix.avlib.ActionStreamerState.PREVIEW_STARTED;
import static com.onix.avlib.ActionStreamerState.PROCESSING_NETWORKS;
import static com.onix.avlib.ActionStreamerState.STREAM_STARTED;
import static com.onix.avlib.ActionStreamerState.STREAM_STOPPED;

public class ActionCamStreamFragment extends Fragment implements IActionCamStreamerEvents {


    private String mServerUrl;
    private FragmentActioncameraStreamingBinding mBinding;
    private ActionCameraStreamer mStreamer;

    // visibility rules for controls
    private CamStateObservable mStartButtonVisibility = new CamStateObservable(
            ERROR_INTERNET_CONNECTION, STREAM_STARTED, STREAM_STOPPED, PREVIEW_STARTED);

    private CamStateObservable mRefreshProgressVisibility = new CamStateObservable(
            PROCESSING_NETWORKS, PREVIEW_PREPARING);

    private CamStateObservable mRefreshPreviewVisibility = new CamStateObservable(
            ERROR_NETWORKS, ERROR_CAMERA_PREVIEW, ERROR_SERVER);


    public static Fragment newInstance(String rtmpUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(DemoActivity.ARG_SERVER_URL, rtmpUrl);
        Fragment fragment = new ActionCamStreamFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServerUrl = getArguments().getString(DemoActivity.ARG_SERVER_URL);

        // need for rotation
        setRetainInstance(true);

        mStreamer = new ActionCameraStreamer(new GoPro3Hero());
        mStreamer.onCreate(getActivity(), savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actioncamera_streaming, container, false);

        mBinding = FragmentActioncameraStreamingBinding.bind(view);
        mBinding.setController(this);

        mStreamer.onCreateView(mBinding.cameraView);

        return view;
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
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    private void updateControlPanel(ActionStreamerState event) {
        switch (event) {
            case ERROR_NETWORKS:
                showError(getString(R.string.error_networks));
                break;
            case ERROR_CAMERA_PREVIEW:
                showError(getString(R.string.error_camera));
                break;
            case ERROR_INTERNET_CONNECTION:
                showError(getString(R.string.error_connection));
                break;
            case ERROR_SERVER:
                showError(getString(R.string.error_server));
                break;
            case STREAM_STARTED:
            case STREAM_STOPPED:
            case PREVIEW_STARTED:
            case PROCESSING_NETWORKS:
            case PREVIEW_PREPARING:
        }

        mBinding.setFps(0);

        mStartButtonVisibility.set(event);
        mRefreshPreviewVisibility.set(event);
        mRefreshProgressVisibility.set(event);

        mBinding.invalidateAll();
    }

    @Override
    public void onStreamingFpsChanged(int fps) {
        mBinding.setFps(fps);
    }

    @Override
    public void onStreamStateChanged(ActionStreamerState event) {
        updateControlPanel(event);
    }

    @Override
    public void onInitCompleted() {
        // customization
        // It make sense when streaming stopped
        mStreamer.setServerUrl(mServerUrl);
        mStreamer.setVideoBitrate(VideoBitrate.B256K);
        mStreamer.setAudioBitrate(AudioBitrate.B32K);
    }

    @Override
    public void onInitStarted() {
    }

    // binding
    public ActionCameraStreamer getStreamer() {
        return mStreamer;
    }

    public ObservableInt startVisibility() {
        return mStartButtonVisibility;
    }

    public ObservableInt progressVisibility() {
        return mRefreshProgressVisibility;
    }

    public ObservableInt refreshVisibility() {
        return mRefreshPreviewVisibility;
    }
}
