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
import android.text.Editable;
import android.text.TextWatcher;
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
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, timestamp_list);
        lv.setAdapter(arrayAdapter);
//        lv.setItemsCanFocus(true);
//        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        OnClickListener listener = new OnClickListener() {
            public void onClick(View view) {
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
        };

//        AdapterView.OnItemClickListener listenerOfListView = new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> view, View view1, int pos,
//                                    long arg3) {
//                String value = timestamp_list.get(pos);
//            }
//        };
//        lv.setOnItemClickListener(listenerOfListView);

        editText.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                if(editText.length() == 0)
                    btn.setEnabled(false); //disable send button if no text entered
                else
                    btn.setEnabled(true);  //otherwise enable
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });
        if(editText.length() == 0) btn.setEnabled(false);//disable at app start

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
            editText.setText("");
        }
    }
    private void setAlarm(){
            alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, count, alarmIntent, 0);
    		Calendar schedule = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
            schedule.set(Calendar.HOUR_OF_DAY, timeHour);
            schedule.set(Calendar.MINUTE, timeMinute);
            schedule.set(Calendar.SECOND, 0);
            //if earlier than now, add 1 day
    		Calendar current = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
    		if(schedule.compareTo(current) < 0){
    		    schedule.set(Calendar.DATE, schedule.get(Calendar.DATE) + 1);
            }
//    		textView1.setText(schedule.get(Calendar.DATE) + " " + current.get(Calendar.DATE));
    		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, schedule.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, schedule.getTimeInMillis(), pendingIntent);
            }
    		count ++;
    }

}