package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thatchosenone.travelmakati.R;

import java.util.HashMap;

public class AboutUs extends AppCompatActivity {


    private Toolbar tbABout;
    private TextInputLayout tilFeed;
    private Button btnSend;
    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference feedRef;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        tbABout = findViewById(R.id.aau_tb_about);
        tilFeed = findViewById(R.id.aau_til_feedback);
        btnSend = findViewById(R.id.aau_btn_send);

        tbABout.setTitle("About us");
        setSupportActionBar(tbABout);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        feedRef = firebaseDatabase.getReference("Feedback");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String feedback = tilFeed.getEditText().getText().toString().trim();
                final ProgressDialog progressDialog = new ProgressDialog(AboutUs.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                if (!validateFeed()) {
                    progressDialog.dismiss();
                    return;
                } else {
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    HashMap<Object, String> feedMap = new HashMap<>();
                    //put post info
                    feedMap.put("feedback", feedback);
                    feedMap.put("timestamp", timestamp);
                    feedMap.put("user_uid", firebaseUser.getUid());
                    //path to store post data
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feedback");
                    //put data in this ref
                    ref.child(timestamp).setValue(feedMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //added in database
                                    progressDialog.dismiss();
                                    //reset views
                                    Toast.makeText(AboutUs.this, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed adding post in database
                                    progressDialog.dismiss();
                                    Toast.makeText(AboutUs.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }

            }
        });

    }

    //Validate First Name
    private boolean validateFeed() {
        final String feedback = tilFeed.getEditText().getText().toString().trim();
        if (feedback.isEmpty()) {
            tilFeed.setError("Error: Field can't be empty");
            return false;
        } else {
            tilFeed.setError(null);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
