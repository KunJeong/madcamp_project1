package com.example.tapapp;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification_channel_id")
//                .setSmallIcon(R.drawable.ic_alarm_64px)
//                .setContentTitle("Alarm")
//                .setContentText("Text")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(222, builder.build());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ringtone.stop();
            }
        }, 2000);
    }
}
