package com.onix.streamer.demo.actioncamera.cam.base;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

public abstract class GoPro4 extends GoProBase {

    private Timer mTimer;
    private TimerTask mTask;

    public void onResume() {
        startStatusChecking();
    }

    public void onPause() {
        clearTimer();
    }

    private void clearTimer() {
        if (mTimer != null) {
            mTask.cancel();
            mTimer.cancel();
            mTimer = null;
            mTask = null;
        }
    }

    private void startStatusChecking() {
        clearTimer();
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                isAlive();
            }
        };
        mTimer.schedule(mTask, 10000, 10000);
    }

    private boolean isAlive() {

        try {
            String messageStr = "_GPHD_:0:0:2:0.000000\n";
            int server_port = 8554;
            DatagramSocket s = new DatagramSocket();
            InetAddress local = InetAddress.getByName("10.5.5.9");
            byte[] message = messageStr.getBytes();
            DatagramPacket p = new DatagramPacket(message, message.length, local, server_port);
            s.setSoTimeout(300);
            s.send(p);
            s.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
