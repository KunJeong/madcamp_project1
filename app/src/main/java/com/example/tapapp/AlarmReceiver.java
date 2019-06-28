package com.example.tapapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ringtone.stop();
            }
        }, 2000);
    }
}
