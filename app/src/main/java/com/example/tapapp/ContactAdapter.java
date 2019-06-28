package com.example.tapapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    ArrayList<String[]> persons;
    String thumbnailUri;
    public ContactAdapter(Context mContext, ArrayList<String[]> data, String uri) {
        context = mContext;
        persons = data;
        thumbnailUri = uri;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView numberView;
        public ImageView profileView;
        public ContactViewHolder(View v) {
            super(v);
            nameView = v.findViewById(R.id.nameText);
            numberView = v.findViewById(R.id.numberText);
            profileView = v.findViewById(R.id.profileView);
        }
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        ContactViewHolder vh = new ContactViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.nameView.setText(persons.get(position)[0]);
        holder.numberView.setText(persons.get(position)[1]);
        if (null != thumbnailUri) {
            holder.profileView.setImageURI(Uri.parse(thumbnailUri));
        }
    }
}
