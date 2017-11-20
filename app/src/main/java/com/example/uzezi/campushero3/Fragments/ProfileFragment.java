package com.example.uzezi.campushero3.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uzezi.campushero3.ClassAdapter;
import com.example.uzezi.campushero3.Classes;
import com.example.uzezi.campushero3.DatabaseHelper;
import com.example.uzezi.campushero3.R;

import java.util.ArrayList;

/**
 * Created by uzezi on 10/2/2017.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private ClassAdapter mAdapter;
    ListView myListView;
    String[] classNames;
    Context context;
    TextView noItems;

    public DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = inflater.getContext();

        noItems = (TextView) view.findViewById(R.id.noItems_tv);
        noItems.setVisibility(noItems.GONE);

        ArrayList<Classes> listContact = GetlistContact();
        if(listContact.isEmpty())
            noItems.setVisibility(noItems.VISIBLE);
        ListView lv = (ListView)view.findViewById(R.id.List1);
        lv.setAdapter(new ClassAdapter(getActivity(), listContact));


        return view;
    }

    private ArrayList<Classes> GetlistContact(){
        db = new DatabaseHelper(context);
        ArrayList<Classes> contactlist = new ArrayList<>();
        String studentId = db.getFirstStudent().getId();
        contactlist = db.getUserClasses(studentId);

        return contactlist;
    }

}
