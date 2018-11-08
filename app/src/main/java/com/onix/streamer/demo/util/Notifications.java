package com.onix.streamer.demo.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.onix.streamer.demo.R;
import com.onix.streamer.demo.screenactivity.ScreenStreamActivity;

public class Notifications {
    public static Notification getNotification(Context context) {

        Intent mParentIntent = new Intent(context, ScreenStreamActivity.class);
        mParentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                iUniqueId, mParentIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "screen_recording_youtube")
                .setSmallIcon(context.getApplicationInfo().icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), context.getApplicationInfo().icon))
                .setContentTitle(context.getApplicationInfo().loadLabel(context.getPackageManager()))
                .setContentText(context.getString(R.string.recording))
                .setSound(null)
                .setOngoing(true)
                .setGroup("screen_recording_youtube_group")
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL)
                .setContentIntent(pendingIntent);

        return notificationBuilder.build();
    }

}
