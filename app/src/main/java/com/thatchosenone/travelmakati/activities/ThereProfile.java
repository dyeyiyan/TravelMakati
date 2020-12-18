package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.adapters.LeisurePhotosAdapter;
import com.thatchosenone.travelmakati.adapters.RecentLeisureAdapter;
import com.thatchosenone.travelmakati.adapters.YourReviewAdapter;
import com.thatchosenone.travelmakati.models.LeisureModel;
import com.thatchosenone.travelmakati.models.LeisurePhotosModel;
import com.thatchosenone.travelmakati.models.RecentViewModel;
import com.thatchosenone.travelmakati.models.ReviewModel;

import java.util.ArrayList;
import java.util.List;

public class ThereProfile extends AppCompatActivity {

    //View for xml
    ImageView ivUserPic, ivUserCover;
    TextView tvName, tvEmail, tvCity, tvMorePhoto, tvMoreReviews, tvMoreRecent;
    RecyclerView rvPhotos, rvReviews, rvRecent;
    Toolbar toolbar;

    List<LeisurePhotosModel> photoList;
    LeisurePhotosAdapter photoAdapter;

    List<ReviewModel> postList;
    YourReviewAdapter postAdapter;

    List<LeisureModel> leisureList;
    RecentLeisureAdapter recentAdapter;

    List<RecentViewModel> recentList;

    String uid;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, recentRef, leisureRef;
    ;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);

        //Init view from xml
        toolbar = (Toolbar) findViewById(R.id.atp_tb_profile);

        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Init fire base
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Path of table of users
        databaseReference = firebaseDatabase.getReference("Users");
        recentRef = firebaseDatabase.getReference("RecentView");
        leisureRef = firebaseDatabase.getReference("Leisure");

        //Init view from xml
        ivUserPic = findViewById(R.id.fp_iv_profile_pic);
        ivUserCover = findViewById(R.id.fp_iv_cover);
        tvEmail = findViewById(R.id.fp_tv_email);
        tvName = findViewById(R.id.fp_tv_name);
        tvCity = findViewById(R.id.fp_tv_city);
        rvPhotos = findViewById(R.id.fp_rv_photo);
        rvReviews = findViewById(R.id.fp_rv_post);
        rvRecent = findViewById(R.id.fp_rv_recent);
        tvMorePhoto = findViewById(R.id.fp_tv_more_photo);
        tvMoreReviews = findViewById(R.id.fp_tv_more_post);


        //get uid of clicked user to retrieve his posts
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");


        tvMorePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addreview = new Intent(ThereProfile.this, AllPhotos.class);
                addreview.putExtra("myuid", uid);
                addreview.putExtra("ald", "profile_fragment");
                startActivity(addreview);
            }
        });

        tvMoreReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addreview = new Intent(ThereProfile.this, AllReviews.class);
                addreview.putExtra("myuid", uid);
                addreview.putExtra("ald", "profile_fragment");
                startActivity(addreview);
            }
        });


        LinearLayoutManager layoutManagers = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        //show newest post first, for this load from last
        layoutManagers.setStackFromEnd(true);
        layoutManagers.setReverseLayout(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        LinearLayoutManager layoutManagerss = new LinearLayoutManager(this);
        //show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);


        rvPhotos.setLayoutManager(layoutManagers);
        rvReviews.setLayoutManager(layoutManager);
        rvRecent.setLayoutManager(layoutManagerss);

        //init post list
        photoList = new ArrayList<>();
        loadPhotos();

        //init post list
        postList = new ArrayList<>();
        loadReviews();

        //init post list
        recentList = new ArrayList<>();
        loadRecent();


        //Getting the info of user
        Query userData = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(uid);
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String city = "" + ds.child("city").getValue();
                    String profile = "" + ds.child("display_photo").getValue();
                    String cover = "" + ds.child("cover_photo").getValue();

                    //set data
                    tvName.setText(name);
                    tvEmail.setText(email);
                    tvCity.setText(city);

                    //try and catch
                    try {
                        Picasso.get().load(profile).fit().centerCrop().into(ivUserPic);

                        //if image is received the set
                    } catch (Exception e) {
                        //if theres any exception while getting image then set default
//                        Picasso.get().load(R.drawable.ic_default_photo).into(ivUserPic);
                    }

                    try {
                        Picasso.get().load(cover).fit().centerCrop().into(ivUserCover);
                        //if image is received the set
                    } catch (Exception e) {
                        //if theres any exception while getting image then set default

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        checkUserStatus();
    }

    private void loadRecent() {
        recentRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recentList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    RecentViewModel recent = ds.getValue(RecentViewModel.class);
                    recentList.add(recent);
                }
                loadAllRecent();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadAllRecent() {
        leisureList = new ArrayList<>();
        leisureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leisureList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);
//                    for (RecentViewModel recent : recentList) {
//                        if (leisureModel.getLuid() != null && leisureModel.getLuid().equals(recent.getId())) {
//                            leisureList.add(leisureModel);
//                            break;
//                        }
//                    }
                    //adapter
                    recentAdapter = new RecentLeisureAdapter(ThereProfile.this, leisureList);
                    //set adapter
                    rvRecent.setAdapter(recentAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadReviews() {
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ratings");
        //get all data from this ref
        ref.limitToFirst(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ReviewModel reviewModel = ds.getValue(ReviewModel.class);
                    if (reviewModel.getUser_uid().equals(uid)) {
                        postList.add(reviewModel);
                    }
//                    for (DataSnapshot dss : ds.getChildren()) {
//
//                    }
                    //adapter
                    postAdapter = new YourReviewAdapter(getApplicationContext(), postList);
                    //set adapter to recyclerview
                    rvReviews.setAdapter(postAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(ThereProfile.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadPhotos() {
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LeisurePhotos");
        //get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                photoList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisurePhotosModel photoModel = ds.getValue(LeisurePhotosModel.class);
                    if (photoModel.getUser_uid().equals(uid)) {
                        photoList.add(photoModel);
                    }
                    //adapter
                    photoAdapter = new LeisurePhotosAdapter(ThereProfile.this, photoList);
                    //set adapter to recyclerview
                    rvPhotos.setAdapter(photoAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(ThereProfile.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            //user is signed in stay here
            //set email of logged in user
            //myprofile set to user.getEmail
            uid = user.getUid();

        } else {
            //user not signed, goto main activity
            Log.d("TAG", "firebaseUser is null");
            Intent login = new Intent(ThereProfile.this, Login.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
        }
    }

    /*Inflate option menu*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
}
