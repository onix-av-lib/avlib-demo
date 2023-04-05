package com.onix.avlib.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.onix.avlib.demo.action_camera.ActionCamStreamActivity
import com.onix.avlib.demo.activity_impl.SimpleStreamActivity
import com.onix.avlib.demo.custom.CustomStreamActivity
import com.onix.avlib.demo.databinding.ActivityMainBinding
import com.onix.avlib.demo.fragment_impl.StreamActivity
import com.onix.avlib.demo.screen_activity.ScreenStreamActivity
import com.onix.avlib.demo.screen_fragment.SimpleScreenStreamActivity
import com.onix.avlib.demo.util.ext.toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.activityExample.setOnClickListener { launchExample(SimpleStreamActivity::class.java) }
        binding.fragmentExample.setOnClickListener { launchExample(StreamActivity::class.java) }
        binding.screenActivityExample.setOnClickListener { launchExample(ScreenStreamActivity::class.java) }
        binding.screenFragmentExample.setOnClickListener { launchExample(SimpleScreenStreamActivity::class.java) }
        binding.customExample.setOnClickListener { launchExample(CustomStreamActivity::class.java) }
        binding.goproExample.setOnClickListener { launchExample(ActionCamStreamActivity::class.java) }
    }

    private fun launchExample(activityClass: Class<*>) {
        val url = binding.serverUrl.text.toString()
        if (url.isEmpty()) {
            toast(R.string.alert_empty_url)
        } else {
            startActivity(Intent(this, activityClass).apply { putExtra(ARG_SERVER_URL, url) })
        }
    }
}
