package com.example.tapapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private Context context;
    private ArrayList<String[]> alarms;
    private AlarmManager alarmManager;
    private ArrayList<PendingIntent> pendingIntents;
    public AlarmAdapter(Context mContext, ArrayList<String[]> data, AlarmManager am, ArrayList<PendingIntent> pi) {
        context = mContext;
        alarms = data;
        alarmManager = am;
        pendingIntents = pi;
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        public Context context;
        public View totalView;
        public TextView titleView;
        public TextView timeView;
        public TextView ampmView;
        public Switch aSwitch;
        public AlarmViewHolder(View v, Context mContext) {
            super(v);
            context = mContext;
            totalView = v;
            titleView = v.findViewById(R.id.titletext);
            timeView = v.findViewById(R.id.timetext);
            ampmView = v.findViewById(R.id.timetext2);
            aSwitch = v.findViewById(R.id.switch1);
        }
    }

    @Override
    public int getItemCount() { return alarms.size(); }

    public ArrayList<String[]> getAlarms() { return alarms; }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        AlarmAdapter.AlarmViewHolder vh = new AlarmAdapter.AlarmViewHolder(v, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmAdapter.AlarmViewHolder holder, final int position) {
        final int Position = position;
        holder.titleView.setText(alarms.get(Position)[2]);
        holder.timeView.setText(alarms.get(Position)[1]);
        holder.ampmView.setText(alarms.get(Position)[0]);
        holder.aSwitch.setChecked(true);
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Turn Off
            }
        });
    }
}
