package com.example.tapapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
// import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class Fragment1 extends Fragment implements PopupMenu.OnMenuItemClickListener {

    GalleryAdapter gallery;
    ImageView imageView;
    ExpandableGridView gridView;
    private int pos = -1;
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
        gridView = view.findViewById(R.id.gridView);
        gallery = new GalleryAdapter(getActivity(), mGlideRequestManager);
        gridView.setAdapter(gallery);
        gridView.setExpanded(true);
        imageView = view.findViewById(R.id.imageView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (null != gallery.getImages() && !gallery.getImages().isEmpty()) {
                    Snackbar.make(view, gallery.getImages().get(i), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    File imgFile = new File(gallery.getImages().get(i));
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                    }
                    final ImagePopup imagePopup = new ImagePopup(getContext());
                    imagePopup.setBackgroundColor(Color.argb(128,0,0,0));
                    imagePopup.setFullScreen(true);
                    imagePopup.setHideCloseIcon(true);
                    imagePopup.setImageOnClickClose(true);
                    imagePopup.initiatePopup(imageView.getDrawable());
                    imagePopup.viewPopup();
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (null != gallery.getImages() && !gallery.getImages().isEmpty()) {
                    showPopup(view, i);
                }
                return true;
            }
        });

        return view;
    }

    public void showPopup(View v, int i) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        this.pos = i;
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_popup);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.delete:
                // should implement
                if (this.pos >= 0) {
                    File imgFile = new File(gallery.getImages().get(this.pos));
                    if (imgFile.exists()) {
                        if (!imgFile.delete()) {
                            Snackbar.make(getView(), "Deletion failed.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            Snackbar.make(getView(), "Deleted successfully.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                    this.pos = -1;
                }
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.replace(R.id.fragment, this);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
             default:
                return true;
        }
    }

}
