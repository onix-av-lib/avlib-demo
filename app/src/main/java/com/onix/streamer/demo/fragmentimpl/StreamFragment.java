package com.onix.streamer.demo.fragmentimpl;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onix.avlib.CameraStreamer;
import com.onix.avlib.IStreamerEvents;
import com.onix.avlib.PreviewScaleType;
import com.onix.avlib.PreviewSize;
import com.onix.avlib.StreamerState;
import com.onix.streamer.demo.DemoActivity;
import com.onix.streamer.demo.R;
import com.onix.streamer.demo.databinding.FragmentCameraStreamingBinding;

import java.util.List;

public class StreamFragment extends Fragment implements IStreamerEvents {

    private String mServerUrl;
    private FragmentCameraStreamingBinding mBinding;
    private CameraStreamer mStreamer;

    public static Fragment newInstance(String rtmpUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(DemoActivity.ARG_SERVER_URL, rtmpUrl);
        Fragment fragment = new StreamFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServerUrl = getArguments().getString(DemoActivity.ARG_SERVER_URL);

        // need for rotation
        setRetainInstance(true);

        mStreamer = new CameraStreamer();
        mStreamer.onCreate(getActivity(), savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_streaming, container, false);
        mBinding = FragmentCameraStreamingBinding.bind(view);
        mBinding.setStreamer(mStreamer);

        mStreamer.onCreateView(mBinding.cameraView, PreviewScaleType.CENTER_CROP);
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
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPreviewSizeSelected(PreviewSize size) {
        mBinding.setPreviewSize(size);
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
        // customization
        // It make sense when streaming stopped
        mStreamer.setServerUrl(mServerUrl);
    }

    @Override
    public void onInitStarted() {
    }
}
