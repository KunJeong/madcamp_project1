package com.example.tapapp;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
// import android.widget.GridView;

import androidx.fragment.app.Fragment;

import java.io.File;

public class Fragment1 extends Fragment {

    GalleryAdapter gallery;
    public Fragment1() {
        // Empty Public Constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        ExpandableGridView gridView = view.findViewById(R.id.galleryGridView);
        gallery = new GalleryAdapter(getActivity());
        gridView.setAdapter(gallery);
        gridView.setExpanded(true);

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/test/";
        File targetDirectory = new File(targetPath);

        File[] files = targetDirectory.listFiles();
        for (File file : files) {
            gallery.add(file.getAbsolutePath());
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (null != gallery.getImages() && !gallery.getImages().isEmpty())
                    Toast.makeText(
                            getActivity(), "Position " + i + " " + gallery.getImages().get(i),
                            Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
