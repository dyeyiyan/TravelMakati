package com.thatchosenone.travelmakati.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thatchosenone.travelmakati.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InternetConnection extends Fragment {

    private static final String COMMON_TAG = "INTERNET_CONNECTION";


    public InternetConnection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_internet_connection, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(COMMON_TAG,"InternetFragment onSaveInstanceState");
    }

}
