package com.example.tapapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {
    @Override

    public void onReceive(final Context context, Intent intent) {
        Intent startIntent = new Intent(context, RingtoneService.class);
        context.startService(startIntent);

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);
    }
}
