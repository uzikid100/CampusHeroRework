package com.example.uzezi.campushero3.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.uzezi.campushero3.ClassAdapter;
import com.example.uzezi.campushero3.DatabaseHelper;
import com.example.uzezi.campushero3.HttpHandler;
import com.example.uzezi.campushero3.PoiAdapter;
import com.example.uzezi.campushero3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by uzezi on 10/2/2017.
 */

public class POIFragment extends Fragment {
    private static final String TAG = "PointsOfInterestFragment";
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);

        context = inflater.getContext();

        DatabaseHelper db = new DatabaseHelper(context);
        ListView poiListView = (ListView) view.findViewById(R.id.Poi_ListView);
        String[] POIs = db.getAllPois();
        //ListAdapter<String> adapter = new ListAdapter<String>(getActivity(), R.layout.drop_down_layout, POIs);
        poiListView.setAdapter(new PoiAdapter(getActivity(), POIs));
        //poiListView.setAdapter(adapter);
        return view;
    }

}
