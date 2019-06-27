package com.example.tapapp;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    ArrayList<String> images;
    RequestOptions reqOpt;
    private Context context;
    private final RequestManager glide;

    public GalleryAdapter(Context localContext, RequestManager mGlideRequestManager) {
        context = localContext;
        glide = mGlideRequestManager;
        images = getAllShownImagesPath(context);
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public RequestManager getGlide() { return glide; }

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
                    .setLayoutParams(new ExpandableGridView.LayoutParams(420, 420));

        } else {
            picturesView = (ImageView) convertView;
        }

        reqOpt = RequestOptions
                .fitCenterTransform()
                .transform(new RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(picturesView.getWidth(), picturesView.getHeight());

        glide.load(images.get(position))
                .thumbnail(0.25f)
                .apply(reqOpt)
                .placeholder(R.mipmap.ic_launcher).centerCrop()
                .into(picturesView);

        return picturesView;
    }

    private ArrayList<String> getAllShownImagesPath(Context context) {
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        try {
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                    null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                File imgFile = new File(absolutePathOfImage);
                if (imgFile.exists()) {
                    listOfAllImages.add(absolutePathOfImage);
                }
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception ex) {
            Log.e("Content.Cursor", ex.getMessage());
        }

        return listOfAllImages;
    }
}
