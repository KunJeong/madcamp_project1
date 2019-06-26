package com.example.tapapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    ArrayList<String> images;
    private Context context;

    public GalleryAdapter(Context localContext) {
        images = new ArrayList<String>();
        context = localContext;
    }

    void add(String path) {
        images.add(path);
    }

    ArrayList<String> getImages() {
        return images;
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
            picturesView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            picturesView
                    .setLayoutParams(new ExpandableGridView.LayoutParams(240, 240));
            picturesView.setPadding(4, 4,4,4);
        } else {
            picturesView = (ImageView) convertView;
        }

        Bitmap bm = decodeSampledBitmapFromUri(images.get(position), 240, 240);
        picturesView.setImageBitmap(bm);
        return picturesView;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        Bitmap bm;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);
        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }
}
