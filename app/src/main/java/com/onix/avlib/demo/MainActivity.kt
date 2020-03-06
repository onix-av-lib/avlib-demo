package com.onix.avlib.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.onix.avlib.demo.action_camera.ActionCamStreamActivity
import com.onix.avlib.demo.activity_impl.SimpleStreamActivity
import com.onix.avlib.demo.custom.CustomStreamActivity
import com.onix.avlib.demo.fragment_impl.StreamActivity
import com.onix.avlib.demo.screen_activity.ScreenStreamActivity
import com.onix.avlib.demo.screen_fragment.SimpleScreenStreamActivity
import com.onix.avlib.demo.util.ext.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.activity_main)

        activityExample.setOnClickListener { launchExample(SimpleStreamActivity::class.java) }
        fragmentExample.setOnClickListener { launchExample(StreamActivity::class.java) }
        screenActivityExample.setOnClickListener { launchExample(ScreenStreamActivity::class.java) }
        screenFragmentExample.setOnClickListener { launchExample(SimpleScreenStreamActivity::class.java) }
        customExample.setOnClickListener { launchExample(CustomStreamActivity::class.java) }
        goproExample.setOnClickListener { launchExample(ActionCamStreamActivity::class.java) }
    }

    private fun launchExample(activityClass: Class<*>) {
        val url = serverUrl.text.toString()
        if (url.isEmpty()) {
            toast(R.string.alert_empty_url)
        } else {
            startActivity(Intent(this, activityClass).apply { putExtra(ARG_SERVER_URL, url) })
        }
    }
}
