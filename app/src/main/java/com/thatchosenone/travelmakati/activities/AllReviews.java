package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.adapters.ReviewAdapter;
import com.thatchosenone.travelmakati.adapters.YourReviewAdapter;
import com.thatchosenone.travelmakati.models.ReviewModel;

import java.util.ArrayList;
import java.util.List;

public class AllReviews extends AppCompatActivity {

    Toolbar tbReviews;
    RecyclerView rvAllReviews;

    Spinner sSort;
    String sortItem;

    List<ReviewModel> postList;
    ReviewAdapter postAdapter;
    YourReviewAdapter postAdapters;

    String lUID, lNAME, myuid, acts;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef, tRatingRef, ratingRef;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews);

        tbReviews = findViewById(R.id.tb_all_reviews);
        rvAllReviews = findViewById(R.id.aar_rv_reviews);
        sSort = findViewById(R.id.aar_s_reviews);

        Intent intent = getIntent();
        lUID = intent.getStringExtra("luid");
        lNAME = intent.getStringExtra("lname");
        myuid = intent.getStringExtra("myuid");
        acts = intent.getStringExtra("ald");


        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDbRef = firebaseDatabase.getReference("Leisure");
        tRatingRef = firebaseDatabase.getReference("RateTotal");
        ratingRef = firebaseDatabase.getReference("Ratings");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recycler view
        rvAllReviews.setLayoutManager(layoutManager);

        //init post list
        postList = new ArrayList<>();

        tbReviews.setTitle("All Reviews");
        setSupportActionBar(tbReviews);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
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
                loadAllReviews(sortItem);
                // Show selected item
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void loadAllReviews(String sortItem) {
        if (acts.equals("all_details")) {
            if (sortItem.equals("Most Recent")) {
                //path of all posts
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ratings");
                //get all data from this ref
                ref.orderByChild("date_time").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ReviewModel reviewModel = ds.getValue(ReviewModel.class);
                            if (reviewModel.getLuid().equals(lUID)) {
                                postList.add(reviewModel);
                            }
                            //adapter
                            postAdapter = new ReviewAdapter(AllReviews.this, postList);
                            //set adapter to recyclerview
                            rvAllReviews.setAdapter(postAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //in case of error
                        Toast.makeText(AllReviews.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                final DatabaseReference nRates = FirebaseDatabase.getInstance().getReference("Ratings");
                nRates.orderByChild("plikes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ReviewModel reviewModel = ds.getValue(ReviewModel.class);
                            if (reviewModel.getLuid().equals(lUID)) {
                                postList.add(reviewModel);
                            }
                            postAdapter = new ReviewAdapter(AllReviews.this, postList);
                            //set adapter to recyclerview
                            rvAllReviews.setAdapter(postAdapter);

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
                final DatabaseReference nRates = FirebaseDatabase.getInstance().getReference("Ratings");
                nRates.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ReviewModel reviewModel = ds.getValue(ReviewModel.class);
                            if (reviewModel.getUser_uid().equals(firebaseUser.getUid())) {
                                postList.add(reviewModel);
                            }

//                            ReviewModel reviewModel = ds.getValue(ReviewModel.class);
//                            if (reviewModel.getUser_uid().equals(myuid)) {
//                                postList.add(reviewModel);
//                            }
                            //adapter
                            postAdapters = new YourReviewAdapter(AllReviews.this, postList);
                            //set adapter to recyclerview
                            rvAllReviews.setAdapter(postAdapters);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            } else {
                final DatabaseReference nRates = FirebaseDatabase.getInstance().getReference("Ratings");
                nRates.orderByChild("plikes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ReviewModel reviewModel = ds.getValue(ReviewModel.class);
                            if (reviewModel.getUser_uid().equals(firebaseUser.getUid())) {
                                postList.add(reviewModel);
                            }

//                            ReviewModel reviewModel = ds.getValue(ReviewModel.class);
//                            if (reviewModel.getUser_uid().equals(myuid)) {
//                                postList.add(reviewModel);
//                            }
                            //adapter
                            postAdapters = new YourReviewAdapter(AllReviews.this, postList);
                            //set adapter to recyclerview
                            rvAllReviews.setAdapter(postAdapters);

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
