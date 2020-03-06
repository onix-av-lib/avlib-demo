package com.onix.avlib.demo.action_camera

import android.view.View
import androidx.databinding.ObservableInt
import com.onix.avlib.ActionStreamerState

class CamStateObservable(vararg events: ActionStreamerState) : ObservableInt() {
    private val mEvents: List<ActionStreamerState> = listOf(*events)

    init {
        set(View.INVISIBLE)
    }

    fun set(event: ActionStreamerState) {
        set(if (mEvents.contains(event)) View.VISIBLE else View.INVISIBLE)
    }
}