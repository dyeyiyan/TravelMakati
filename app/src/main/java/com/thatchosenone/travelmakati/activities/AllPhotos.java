package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.adapters.LeisurePhotosAdapter;
import com.thatchosenone.travelmakati.models.LeisurePhotosModel;

import java.util.ArrayList;
import java.util.List;

public class AllPhotos extends AppCompatActivity {


    Toolbar tbPhotos;

    RecyclerView rvAllPhotos;

    Spinner sSort;
    String sortItem;

    List<LeisurePhotosModel> photosList;
    LeisurePhotosAdapter photosAdapter;

    String lUID, lNAME, myuid, acts;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference photoRef;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_photos);

        tbPhotos = findViewById(R.id.tb_all_photos);
        rvAllPhotos = findViewById(R.id.aap_rv_photos);
        sSort = findViewById(R.id.aap_s_photos);

        Intent intent = getIntent();
        lUID = intent.getStringExtra("luid");
        lNAME = intent.getStringExtra("lname");
        myuid = intent.getStringExtra("myuid");
        acts = intent.getStringExtra("ald");

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        photoRef = firebaseDatabase.getReference("LeisurePhotos");

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        //show newest post first, for this load from last
        //layoutManager.setReverseLayout(true);
        //set layout to recycler view
        rvAllPhotos.setLayoutManager(layoutManager);

        //init post list
        photosList = new ArrayList<>();

        tbPhotos.setTitle("All Photos");
        setSupportActionBar(tbPhotos);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        sortby();
    }

    private void sortby() {
        List<String> sort = new ArrayList<>();
        sort.add(0, "Most Recent");
        sort.add("Most Popular");
        //Style and populate the spinner
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sort);

        //Dropdown layout stylea
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching array adapter to spinner
        sSort.setAdapter(arrayAdapter);

        sSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortItem = parent.getItemAtPosition(position).toString();
                loadAllPhotos(sortItem);
                // Show selected item
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void loadAllPhotos(String sortItem) {
        if (acts.equals("all_details")) {
            if (sortItem.equals("Most Recent")) {
                final DatabaseReference nRates = FirebaseDatabase.getInstance().getReference("LeisurePhotos");
                nRates.orderByChild("date_time").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        photosList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            LeisurePhotosModel photoModel = ds.getValue(LeisurePhotosModel.class);
                            if (photoModel.getLuid().equals(lUID)) {
                                photosList.add(photoModel);

                            }
                            //adapter
                            photosAdapter = new LeisurePhotosAdapter(AllPhotos.this, photosList);
                            //set adapter to recyclerview
                            rvAllPhotos.setAdapter(photosAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

        }
        if (acts.equals("profile_fragment")) {
            if (sortItem.equals("Most Recent")) {
                final DatabaseReference nRates = FirebaseDatabase.getInstance().getReference("LeisurePhotos");
                nRates.orderByChild("date_time").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        photosList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            LeisurePhotosModel photoModel = ds.getValue(LeisurePhotosModel.class);
                            if (photoModel.getUser_uid().equals(myuid)) {
                                photosList.add(photoModel);
                            }
                            //adapter
                            photosAdapter = new LeisurePhotosAdapter(AllPhotos.this, photosList);
                            //set adapter to recyclerview
                            rvAllPhotos.setAdapter(photosAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
