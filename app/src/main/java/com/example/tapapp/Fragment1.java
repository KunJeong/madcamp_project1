package com.example.tapapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
// import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.material.snackbar.Snackbar;

public class Fragment1 extends Fragment {

    GalleryAdapter gallery;
    public RequestManager mGlideRequestManager;
    public Fragment1() {
        // Empty Public Constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlideRequestManager = Glide.with(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        ExpandableGridView gridView = view.findViewById(R.id.gridView);
        gallery = new GalleryAdapter(getActivity(), mGlideRequestManager);
        gridView.setAdapter(gallery);
        gridView.setExpanded(true);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (null != gallery.getImages() && !gallery.getImages().isEmpty())
                    Snackbar.make(view, gallery.getImages().get(i), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
        return view;
    }
}
