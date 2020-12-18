package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.adapters.PreviewPhotoAdapter;
import com.thatchosenone.travelmakati.models.LeisurePhotosModel;

import java.util.ArrayList;
import java.util.List;

public class PreviewPhoto extends AppCompatActivity {

    RecyclerView rvPreview;

    List<LeisurePhotosModel> photosList;
    PreviewPhotoAdapter previewAdapter;

    String ldate, lNAME;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference photoRef;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);

        rvPreview = findViewById(R.id.app_rv_preview);

        Intent intent = getIntent();
        ldate = intent.getStringExtra("ldate");

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        photoRef = firebaseDatabase.getReference("LeisurePhotos");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        //show newest post first, for this load from last
        //set layout to recycler view
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(true);
        rvPreview.setLayoutManager(layoutManager);
        //init post list
        photosList = new ArrayList<>();
        loadPreviewPhoto();
    }

    private void loadPreviewPhoto() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LeisurePhotos");
        //get all data from this ref
        ref.orderByChild("date_time").equalTo(ldate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                photosList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisurePhotosModel photoModel = ds.getValue(LeisurePhotosModel.class);

                    photosList.add(photoModel);
                    //adapter
                    previewAdapter = new PreviewPhotoAdapter(PreviewPhoto.this, photosList);
                    previewAdapter.notifyDataSetChanged();
                    //set adapter to recyclerview
                    rvPreview.setAdapter(previewAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(PreviewPhoto.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
