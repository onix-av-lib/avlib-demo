package com.onix.avlib.demo.action_camera.cam.base

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*

abstract class GoPro4 : GoProBase() {

    private var mTimer: Timer? = null
    private var mTask: TimerTask? = null

    private val isAlive: Boolean
        get() {

            return try {
                val messageStr = "_GPHD_:0:0:2:0.000000\n"
                val server_port = 8554
                val s = DatagramSocket()
                val local = InetAddress.getByName("10.5.5.9")
                val message = messageStr.toByteArray()
                val p = DatagramPacket(message, message.size, local, server_port)
                s.soTimeout = 300
                s.send(p)
                s.close()
                true
            } catch (e: Exception) {
                false
            }

        }

    override fun onResume() {
        startStatusChecking()
    }

    override fun onPause() {
        clearTimer()
    }

    private fun clearTimer() {
        if (mTimer != null) {
            mTask!!.cancel()
            mTimer!!.cancel()
            mTimer = null
            mTask = null
        }
    }

    private fun startStatusChecking() {
        clearTimer()
        mTimer = Timer()
        mTask = object : TimerTask() {
            override fun run() {
                isAlive
            }
        }
        mTimer!!.schedule(mTask, 10000, 10000)
    }
}
