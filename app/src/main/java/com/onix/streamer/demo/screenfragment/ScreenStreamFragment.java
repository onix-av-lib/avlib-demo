package com.onix.streamer.demo.screenfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onix.avlib.AudioBitrate;
import com.onix.avlib.IScreenStreamerEvents;
import com.onix.avlib.PreviewSize;
import com.onix.avlib.ScreenStreamer;
import com.onix.avlib.ScreenStreamerEvent;
import com.onix.avlib.VideoBitrate;
import com.onix.streamer.demo.DemoActivity;
import com.onix.streamer.demo.R;
import com.onix.streamer.demo.databinding.FragmentScreenStreamBinding;

public class ScreenStreamFragment extends Fragment implements IScreenStreamerEvents {

    private static final String TAG = "ScreenStreamFragment";

    private String mServerUrl;
    private FragmentScreenStreamBinding mBinding;

    private ScreenStreamer mStreamer;

    public static Fragment newInstance(String rtmpUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(DemoActivity.ARG_SERVER_URL, rtmpUrl);
        Fragment fragment = new ScreenStreamFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServerUrl = getArguments().getString(DemoActivity.ARG_SERVER_URL);

        // need for rotation
        setRetainInstance(true);

        mStreamer = new ScreenStreamer();
        mStreamer.onCreate(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_stream, container, false);
        mBinding = FragmentScreenStreamBinding.bind(view);
        mBinding.setStreamer(mStreamer);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mStreamer.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mStreamer.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStreamer.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mStreamer.onActivityResult(requestCode, resultCode, data);
    }


    private void handleStateChanged(ScreenStreamerEvent event) {
        switch (event) {
            case STREAM_STARTED:
                Toast.makeText(getActivity(), R.string.recording, Toast.LENGTH_SHORT).show();
            case STREAM_STOPPED:
                mBinding.invalidateAll();
                break;

            case ERROR_DISPLAY_NOT_FOUND: {
                Toast.makeText(getActivity(), R.string.error_display_not_found, Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_PERMISSIONS: {
                Toast.makeText(getActivity(), R.string.error_permissions, Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_SERVER:
            case ERROR_CONNECTION: {
                Toast.makeText(getActivity(), R.string.error_connection, Toast.LENGTH_SHORT).show();
                mBinding.invalidateAll();
                break;
            }
        }
    }

    @Override
    public void onStreamStateChanged(ScreenStreamerEvent event) {
        Log.d(TAG, "onStreamStateChanged: " + event.toString());
        handleStateChanged(event);
    }

    @Override
    public void onInited(boolean isStreaming) {
        // setup streaming settings
        // it make sense only when streaming not started
        mStreamer.setServerUrl(mServerUrl);
        mStreamer.setAudioBitrate(AudioBitrate.B64K);
        mStreamer.setVideoBitrate(VideoBitrate.B4096K);
        mStreamer.setSize(new PreviewSize(1280, 960));
    }

}
