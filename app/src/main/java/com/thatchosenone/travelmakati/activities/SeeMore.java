package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.LeisureModel;

import java.util.ArrayList;
import java.util.List;

public class SeeMore extends AppCompatActivity {

    String see;
    Toolbar toolbar;
    RecyclerView rvSeeMore;

    List<LeisureModel> whatsnewList, popularLists;
//    FullWhatsNewAdapter whatAdapter;
//    FullPopularPlacesAdapter popularAdapter;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef, tRatingRef, ratingRef;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);

        toolbar = findViewById(R.id.tb_see_more);
        rvSeeMore = findViewById(R.id.asm_rv_more);

        Intent intent = getIntent();
        see = intent.getStringExtra("more");

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDbRef = firebaseDatabase.getReference("Leisure");
        tRatingRef = firebaseDatabase.getReference("RateTotal");
        ratingRef = firebaseDatabase.getReference("Ratings");

        toolbar.setTitle(see);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recycler view
        rvSeeMore.setLayoutManager(layoutManager);

        //init post list
        whatsnewList = new ArrayList<>();
        popularLists = new ArrayList<>();

        if (see.equals("What's New")) {
            loadWhatsNew();
        }

        if (see.equals("Popular Places")) {
            loadPopular();
        }
    }

    private void loadPopular() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Leisure");
        //get all data from this ref
        ref.orderByChild("total_rate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                popularLists.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);
//                    String total = leisureModel.getTotal_rate();
//                    float ntotal = Float.parseFloat(total);
//                    if (ntotal >= 3.75) {
//                        popularLists.add(leisureModel);
//                    }
                    //adapter
//                    popularAdapter = new FullPopularPlacesAdapter(SeeMore.this, popularLists);
//                    popularAdapter.notifyDataSetChanged();
//                    //set adapter to recyclerview
//                    rvSeeMore.setAdapter(popularAdapter);
//                    //rvWhats.setAdapter(postAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(SeeMore.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadWhatsNew() {
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Leisure");
        //get all data from this ref
        ref.orderByChild("date_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                whatsnewList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);
//                    String leisuretime = leisureModel.getDate_time();
//                    long lt = Long.parseLong(leisuretime);
//                    long lta = lt + 604800000;
//                    long timestamp = System.currentTimeMillis();
//                    if (leisureModel.getStatus().equals("approved")) {
//                        if (lta >= timestamp) {
//                            whatsnewList.add(leisureModel);
//                        }
//                    }
                    //adapter
//                    whatAdapter = new FullWhatsNewAdapter(SeeMore.this, whatsnewList);
//                    whatAdapter.notifyDataSetChanged();
//                    //set adapter to recyclerview
//                    //rvPopular.setAdapter(postAdapter);
//                    rvSeeMore.setAdapter(whatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(SeeMore.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //searchview to search posts by post title description
//        MenuItem item = menu.findItem(R.id.search_all_mm);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        //hide addpost icon from this fragment
////        menu.findItem(R.id.add_search_mm).setVisible(false);
////        menu.findItem(R.id.add_post_mm).setVisible(false);
////        menu.findItem(R.id.sign_out_mm).setVisible(false);
////        menu.findItem(R.id.your_business_mm).setVisible(false);
////        //menu.findItem(R.id.setting_mm).setVisible(false);
////        menu.findItem(R.id.about_us_mm).setVisible(false);
//        //search listener
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                //called when user press search button
//                if (!TextUtils.isEmpty(s)) {
//                    searchLeisure(s);
//                } else {
//                    if (see.equals("What's New")) {
//                        loadWhatsNew();
//                    }
//
//                    if (see.equals("Popular Places")) {
//                        loadPopular();
//                    }
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                //called as and when user press any letter
//                if (!TextUtils.isEmpty(s)) {
//                    searchLeisure(s);
//                } else {
//                    if (see.equals("What's New")) {
//                        loadWhatsNew();
//                    }
//
//                    if (see.equals("Popular Places")) {
//                        loadPopular();
//                    }
//                }
//                return false;
//            }
//        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchLeisure(String searchQuery) {
        //path of all posts
        if (see.equals("What's New")) {
            //path of all posts
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Leisure");
            //get all data from this ref
            ref.orderByChild("date_time").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    whatsnewList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        LeisureModel leisureModel = ds.getValue(LeisureModel.class);
//                        String leisuretime = leisureModel.getDate_time();
//                        long lt = Long.parseLong(leisuretime);
//                        long lta = lt + 604800000;
//                        long timestamp = System.currentTimeMillis();
//                        if (leisureModel.getStatus().equals("approved")) {
//                            if (lta >= timestamp) {
//                                if (leisureModel.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
//                                    whatsnewList.add(leisureModel);
//                                }
//                            }
//                        }
                        //adapter
//                        whatAdapter = new FullWhatsNewAdapter(SeeMore.this, whatsnewList);
//                        whatAdapter.notifyDataSetChanged();
//                        //set adapter to recyclerview
//                        //rvPopular.setAdapter(postAdapter);
//                        rvSeeMore.setAdapter(whatAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //in case of error
                    Toast.makeText(SeeMore.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (see.equals("Popular Places")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Leisure");
            //get all data from this ref
            ref.orderByChild("total_rate").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    popularLists.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        LeisureModel leisureModel = ds.getValue(LeisureModel.class);
//                        String total = leisureModel.getTotal_rate();
//                        float ntotal = Float.parseFloat(total);
//                        if (ntotal >= 3.75) {
//                            if (leisureModel.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
//                                popularLists.add(leisureModel);
//                            }
//
//                        }
                        //adapter
//                        popularAdapter = new FullPopularPlacesAdapter(SeeMore.this, popularLists);
//                        popularAdapter.notifyDataSetChanged();
//                        //set adapter to recyclerview
//                        rvSeeMore.setAdapter(popularAdapter);
//                        //rvWhats.setAdapter(postAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //in case of error
                    Toast.makeText(SeeMore.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
