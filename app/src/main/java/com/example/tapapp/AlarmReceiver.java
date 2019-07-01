package com.example.tapapp;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.Edits;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
<<<<<<< HEAD
import android.os.Bundle;
=======
import android.os.Build;
>>>>>>> e5257fa065e966a711266f6a3a43aa0751612573
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Iterator;
=======
import com.bumptech.glide.util.Util;
>>>>>>> e5257fa065e966a711266f6a3a43aa0751612573

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);

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
        }, 3000);

        Intent alarmIntent = new Intent("com.example.tapapp.ALARM_ACTION");
        alarmIntent.setClass(context, AlarmActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}
