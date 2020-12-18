package com.thatchosenone.travelmakati.activities;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.thatchosenone.travelmakati.R;

public class LoadingBar {

    Activity activity;
    AlertDialog alertDialog;

    LoadingBar(Activity thisActivity){
        activity = thisActivity;
    }

    void showDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissBar(){
        alertDialog.dismiss();
    }
}
