package com.example.tapapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.ALARM_SERVICE;

public class Fragment2 extends Fragment {
    private Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
    private int timeHour =  c.get(Calendar.HOUR_OF_DAY);
    private int timeMinute =  c.get(Calendar.MINUTE);

    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    private String formattedDate = df.format(c.getTime());
    private TextView textView1;
    private EditText editText;
    private Button btn;
    private ArrayAdapter<String> arrayAdapter;
    private ListView lv;

    Context context;

    private ArrayList<String> timestamp_list = new ArrayList<>();
    private AlarmManager alarmManager;
    //private PendingIntent pendingIntent;
    int count = 0;

    public Fragment2() {

    }

    @Override
    public void onCreate (Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        textView1 = view.findViewById(R.id.msg1);

        textView1.setText("0 alarms created");
        editText = view.findViewById(R.id.input);
        btn = view.findViewById(R.id.button);
        lv = view.findViewById(R.id.lv);
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, timestamp_list);
        lv.setAdapter(arrayAdapter);



        
        OnClickListener listener = new OnClickListener() {
            public void onClick(View view) {
//                textView2.setText("");
                Bundle bundle = new Bundle();
                bundle.putInt(MyConstants.HOUR, timeHour);
                bundle.putInt(MyConstants.MINUTE, timeMinute);
                MyDialogFragment fragment = new MyDialogFragment(new MyHandler());
                fragment.setArguments(bundle);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(fragment, "time_picker");
                transaction.commit();
            }
//                c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
//                formattedDate = df.format(c.getTime());
////                Bundle bundle = new Bundle();
////                bundle.putInt(MyConstants.HOUR, timeHour);
////                bundle.putInt(MyConstants.MINUTE, timeMinute);
////                bundle.putInt(MyConstants.SECOND, timeSecond);
////                bundle.putString(MyConstants.NAME, String.valueOf(editText.getText()));
//                timestamp_list.add(formattedDate+ ", " + editText.getText());
//                editText.setText("");
//                arrayAdapter.notifyDataSetChanged();
        };
        btn.setOnClickListener(listener);

        return view;
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage (Message msg){
            Bundle bundle = msg.getData();
            timeHour = bundle.getInt(MyConstants.HOUR);
            timeMinute = bundle.getInt(MyConstants.MINUTE);
            textView1.setText((count + 1) +" alarms created");
            timestamp_list.add(timeHour + ":" + timeMinute + ", " + editText.getText());
            setAlarm();
            arrayAdapter.notifyDataSetChanged();
        }
    }
    private void setAlarm(){
            alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, count, alarmIntent, 0);
    		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
    		calendar.set(Calendar.HOUR_OF_DAY, timeHour);
    		calendar.set(Calendar.MINUTE, timeMinute);
    		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
    		count ++;
    }

}