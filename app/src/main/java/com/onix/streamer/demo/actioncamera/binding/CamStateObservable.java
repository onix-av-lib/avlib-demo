package com.onix.streamer.demo.actioncamera.binding;

import android.databinding.ObservableInt;
import android.view.View;

import com.onix.avlib.ActionStreamerState;

import java.util.Arrays;
import java.util.List;

public class CamStateObservable extends ObservableInt {
    private List<ActionStreamerState> mEvents;

    public CamStateObservable(ActionStreamerState... events) {
        mEvents = Arrays.asList(events);
        set(View.INVISIBLE);
    }

    public void set(ActionStreamerState event) {
        set(mEvents.contains(event) ? View.VISIBLE : View.INVISIBLE);
    }
}