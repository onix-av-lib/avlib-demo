package com.onix.avlib.demo.util

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build

import androidx.core.app.NotificationCompat
import com.onix.avlib.demo.MainActivity


object Notifications {
    fun getNotification(context: Context): Notification {

        val mParentIntent = Intent(context, MainActivity::class.java)
        mParentIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val iUniqueId = (System.currentTimeMillis() and 0xfffffff).toInt()

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context,
                iUniqueId,
                mParentIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context,
                iUniqueId,
                mParentIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }

        val notificationBuilder = NotificationCompat.Builder(context, "screen_recording_youtube")
            .setSmallIcon(context.applicationInfo.icon)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    context.applicationInfo.icon
                )
            )
            .setContentTitle(context.applicationInfo.loadLabel(context.packageManager))
            .setContentText("Screen recording...")
            .setSound(null)
            .setOngoing(true)
            .setGroup("screen_recording_youtube_group")
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL)
            .setContentIntent(pendingIntent)

        return notificationBuilder.build()
    }

}
