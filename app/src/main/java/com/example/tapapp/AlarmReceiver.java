package com.example.tapapp;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.Edits;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Iterator;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();
//        Bundle data = intent.getExtras();
//        ArrayList<Integer> index2code = data.getIntegerArrayList("index2code");
//        int code = data.getInt("code");
//        int i = 0;
//        for(; i < index2code.size(); i++){
//            if(index2code.get(i) == code){
//                break;
//            }
//        }



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
