package com.example.uzezi.campushero3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by uzezi on 10/2/2017.
 */

public class POIFragment extends Fragment {
    private static final String TAG = "PointsOfInterestFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);
        return view;
    }
}
