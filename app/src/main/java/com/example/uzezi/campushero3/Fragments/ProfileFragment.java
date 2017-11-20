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

    public DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = inflater.getContext();

        //Resources res = getResources();
        //classNames = res.getStringArray(R.array.classNames);
        //classNames = db.getUserClassNames(db.getStudentId());

        ArrayList<Classes> listContact = GetlistContact();
        ListView lv = (ListView)view.findViewById(R.id.List1);
        lv.setAdapter(new ClassAdapter(getActivity(), listContact));

        /*
        ClassAdapter itemAdapter = new ClassAdapter(getActivity(), R.layout.class_list_layout);
        myListView = (ListView) view.findViewById(R.id.List1);
        myListView.setAdapter(itemAdapter);
        */

        return view;
    }
    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        //db = new DatabaseHelper(getActivity());
    }
    */

    private ArrayList<Classes> GetlistContact(){
        db = new DatabaseHelper(context);
        ArrayList<Classes> contactlist = new ArrayList<>();
        String studentId = db.getFirstStudent().getId();
        contactlist = db.getUserClasses(studentId);
/*
        Classes contact = new Classes();

        contact.setMsimpleName("Topher");
        contact.setId("dszgfdzgfdzg");
        contactlist.add(contact);
/*
        contact = new Classes();
        contact.setMsimpleName("Jean");
        contact.setId("01213869102");
        contactlist.add(contact);

        contact = new Classes();
        contact.setMsimpleName("Andrew");
        contact.setId("01213123985");
        contactlist.add(contact);
*/

        return contactlist;
    }

}
