package com.example.tapapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Fragment0 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final int PICK_CONTACT_REQUEST = 2;
    private final int PICK_EDIT_REQUEST = 3;
    private int profileIndex = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public Integer[] images = new Integer[] {
            R.drawable.adam, R.drawable.anjali,
            R.drawable.arjun, R.drawable.jorge,
            R.drawable.maya, R.drawable.rahul,
            R.drawable.sadona, R.drawable.sandy,
            R.drawable.sid, R.drawable.steve
    };
    private SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
        @Override
        public void onRightClicked(int position) {
            if (null != mAdapter.getData().get(position)[3]) {
                getContext().getContentResolver().delete(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                        mAdapter.getData().get(position)[3]), null, null);
                Snackbar.make(getView(), "Deleted successfully.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            mAdapter.getData().remove(position);
            mAdapter.notifyItemRemoved(position);
            mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
        }

        @Override
        public void onLeftClicked(int position) {
            String currentLookupKey = mAdapter.getData().get(position)[3];
            String [] projection = new String [] { ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
            Cursor cur = getContext().getContentResolver().query
                    (ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    do {
                        if (cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY)).equalsIgnoreCase(currentLookupKey)) {
                            Uri selectedContactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, currentLookupKey);
                            Intent editIntent = new Intent(Intent.ACTION_EDIT);
                            editIntent.setDataAndType(selectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                            if (Integer.valueOf(Build.VERSION.SDK_INT) > 14)
                                editIntent.putExtra("finishActivityOnSaveCompleted", true);
                            startActivityForResult(editIntent, PICK_EDIT_REQUEST);
                            mAdapter.notifyItemChanged(position);
                            break;
                        }
                    } while (cur.moveToNext());
                }
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                Snackbar.make(getView(), "Editing failed.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } finally {
                cur.close();
            }
        }
    });

    public Fragment0() {
        // Empty Public Constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_0, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
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
        getList();
        mAdapter.setItemClick(new ContactAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                String currentLookupKey = mAdapter.getData().get(position)[3];
                String [] projection = new String [] { ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
                Cursor cur = getContext().getContentResolver().query
                        (ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
                try {
                    if (cur.moveToFirst()) {
                        do {
                            if (cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY)).equalsIgnoreCase(currentLookupKey)) {
                                Uri selectedContactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, currentLookupKey);
                                Intent editIntent = new Intent(Intent.ACTION_VIEW, selectedContactUri);
                                startActivity(editIntent);
                                break;
                            }
                        } while (cur.moveToNext());
                    }
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                    Snackbar.make(getView(), "Editing failed.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } finally {
                    cur.close();
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = view.findViewById(R.id.contactFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_INSERT);
                i.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (Integer.valueOf(Build.VERSION.SDK_INT) > 14)
                    i.putExtra("finishActivityOnSaveCompleted", true);
                startActivityForResult(i, PICK_CONTACT_REQUEST);
            }
        });
        return view;
    }

    public void getList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        };
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor contactCursor = getActivity().getContentResolver().query(uri, projection, null, null, sortOrder);

        ArrayList<String[]> persons = new ArrayList<>();
        if (contactCursor.moveToFirst()) {
            do {
                String[] data = new String[5];
                data[0] = contactCursor.getString(1);
                data[1] = contactCursor.getString(0).replace("-", "").replace("?","");
                if (data[1].startsWith("//")) {
                    data[1] = data[1].substring(2);
                } else if (data[1].startsWith("+82")) {
                    data[1] = "0" + data[1].substring(3);
                }
                data[2] = contactCursor.getString(2);
                data[3] = contactCursor.getString(4);
                data[4] = images[profileIndex].toString();
                if (profileIndex == 9) {
                    profileIndex = 0;
                } else {
                    profileIndex++;
                }
                persons.add(data);
            } while (contactCursor.moveToNext());
        }
        mAdapter = new ContactAdapter(getContext(), persons);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CONTACT_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Snackbar.make(getView(), "Added successfully.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case PICK_EDIT_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Snackbar.make(getView(), "Edited successfully.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mAdapter);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, new Fragment0());
                transaction.commit();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}
