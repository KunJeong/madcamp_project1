package com.example.tapapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private AlarmAdapter alarmAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
        @Override
        public void onRightClicked(int position) {
            // Alarm removing
            deleteItem(position);
            alarmAdapter.notifyItemRemoved(position);
            alarmAdapter.notifyItemRangeChanged(position, alarmAdapter.getItemCount());
            textView1.setText((count) + " alarms created");
        }

        @Override
        public void onLeftClicked(int position) {
            String savedTitle = alarmAdapter.getAlarms().get(position)[2];
            deleteItem(position);
            alarmAdapter.notifyItemRemoved(position);
            alarmAdapter.notifyItemRangeChanged(position, alarmAdapter.getItemCount());
            Bundle bundle = new Bundle();
            bundle.putInt(MyConstants.HOUR, timeHour);
            bundle.putInt(MyConstants.MINUTE, timeMinute);
            MyDialogFragment fragment = new MyDialogFragment(new EditHandler(position, savedTitle));
            fragment.setArguments(bundle);
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(fragment, "time_picker");
            transaction.commit();
        }
    });

    Context context;

    private ArrayList<String[]> timestamp_list = new ArrayList<>();
    private AlarmManager alarmManager;
    private ArrayList<PendingIntent> pendingIntents = new ArrayList<>();
    private Intent alarmIntent;
    int count = 0;
    int code = 0;

    public Fragment2() {

    }

    @Override
    public void onCreate (Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        textView1 = view.findViewById(R.id.msg1);

        textView1.setText("0 alarms created");
        editText = view.findViewById(R.id.input);
        btn = view.findViewById(R.id.button);
        recyclerView = view.findViewById(R.id.rv);
        alarmAdapter = new AlarmAdapter(getContext(), timestamp_list, alarmManager, pendingIntents);
        recyclerView.setAdapter(alarmAdapter);
        layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

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

        editText.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                if(editText.length() == 0)
                    btn.setEnabled(false); //disable send button if no text entered
                else
                    btn.setEnabled(true); //otherwise enable
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });
        if(editText.length() == 0) btn.setEnabled(false); //disable at app start

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
            String[] data = new String[3];
            int fixedHour;
            if(timeHour >= 12){
                data[0] = ("PM");
                fixedHour = timeHour - 12;
            }
            else {
                data[0] = ("AM");
                fixedHour = timeHour;
            }
            if(fixedHour == 0){
                fixedHour = 12;
            }

            if (timeMinute >= 0 && timeMinute < 10) {
                data[1] = (fixedHour + ":0" + timeMinute);
            } else {
                data[1] = (fixedHour + ":" + timeMinute);
            }
            data[2] = (editText.getText() + "");
            timestamp_list.add(data);

            setAlarm();
            alarmAdapter.notifyDataSetChanged();
            editText.setText("");
        }
    }

    class EditHandler extends Handler {
        private int position;
        private String title;

        public EditHandler(int pos, String t) {
            position = pos;
            title = t;
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            timeHour = bundle.getInt(MyConstants.HOUR);
            timeMinute = bundle.getInt(MyConstants.MINUTE);
            textView1.setText((count + 1) +" alarms created");
            String[] data = new String[3];
            int fixedHour;
            if(timeHour >= 12){
                data[0] = ("PM");
                fixedHour = timeHour - 12;
            }
            else {
                data[0] = ("AM");
                fixedHour = timeHour;
            }
            if(fixedHour == 0){
                fixedHour = 12;
            }

            if (timeMinute >= 0 && timeMinute < 10) {
                data[1] = (fixedHour + ":0" + timeMinute);
            } else {
                data[1] = (fixedHour + ":" + timeMinute);
            }
            data[2] = this.title;
            timestamp_list.add(this.position, data);

            setAlarm();
            alarmAdapter.notifyItemChanged(this.position);
            alarmAdapter.notifyDataSetChanged();
        }
    }

    void deleteItem(int position){
        count--;
        timestamp_list.remove(position);
        alarmManager.cancel(pendingIntents.remove(position));
    }

    private void setAlarm(){
            alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, alarmIntent, 0);
    		Calendar schedule = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
            schedule.set(Calendar.HOUR_OF_DAY, timeHour);
            schedule.set(Calendar.MINUTE, timeMinute);
            schedule.set(Calendar.SECOND, 0);
    		Calendar current = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
    		if(schedule.compareTo(current) < 0){
    		    schedule.set(Calendar.DATE, schedule.get(Calendar.DATE) + 1);
            }
            pendingIntents.add(pendingIntent);
    		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, schedule.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, schedule.getTimeInMillis(), pendingIntent);
            }
    		count++;
    		code++;
    }
}