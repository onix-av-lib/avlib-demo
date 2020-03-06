package com.onix.avlib.demo.action_camera

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onix.avlib.demo.ARG_SERVER_URL
import com.onix.avlib.demo.R
import com.onix.avlib.demo.util.ext.argument

class ActionCamStreamActivity : AppCompatActivity() {

    private val serverUrl by argument<String>(ARG_SERVER_URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streamer)

        val tag = ActionCamStreamFragment::class.java.simpleName

        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragmentContainer, ActionCamStreamFragment.instance(serverUrl), tag
            ).commitAllowingStateLoss()
        }
    }

}
