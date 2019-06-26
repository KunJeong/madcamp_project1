package com.example.tapapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
// import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class Fragment1 extends Fragment {

    private ArrayList<String> images;
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
        ExpandableGridView gridView = view.findViewById(R.id.galleryGridView);
        gridView.setAdapter(new GalleryAdapter(getActivity(), mGlideRequestManager));
        gridView.setExpanded(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (null != images && !images.isEmpty())
                    Toast.makeText(
                            getActivity(), "Position " + i + " " + images.get(i),
                            Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private class GalleryAdapter extends BaseAdapter {

        private Context context;
        private final RequestManager glide;

        public GalleryAdapter(Context localContext, RequestManager mGlideRequestManager) {
            context = localContext;
            glide = mGlideRequestManager;
            images = getAllShownImagesPath(context);
        }

        public GalleryAdapter(Context localContext, RequestManager mGlideRequestManager, ArrayList<String> localImages) {
            context = localContext;
            glide = mGlideRequestManager;
            images = localImages;
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new ExpandableGridView.LayoutParams(240, 240));

            } else {
                picturesView = (ImageView) convertView;
            }

            glide.load(images.get(position))
                    .placeholder(R.mipmap.ic_launcher).centerCrop()
                    .into(picturesView);

            return picturesView;
        }

        private ArrayList<String> getAllShownImagesPath(Context context) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<>();
            String absolutePathOfImage;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = context.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                listOfAllImages.add(absolutePathOfImage);
            }

            if (cursor != null) {
                cursor.close();
            }

            return listOfAllImages;
        }
    }
}
