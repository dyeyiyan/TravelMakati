package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.adapters.LeisureAdapter;
import com.thatchosenone.travelmakati.models.LeisureModel;

import java.util.ArrayList;
import java.util.List;

public class SearchAllLeisure extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rvLeisureCategory;
    // private RatingBar ratingBar;

    List<LeisureModel> postList;
    LeisureAdapter postAdapter;

    String approved = "approved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all_leisure);

        toolbar = findViewById(R.id.tb_leisure_category);


        //recyclerviewand its properties
        rvLeisureCategory = findViewById(R.id.asll_rv_lcategory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recycler view
        rvLeisureCategory.setLayoutManager(layoutManager);

        //init post list
        postList = new ArrayList<>();
        loadLeisure();

        toolbar.setTitle("Search All");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
//
//        category = FirebaseDatabase.getInstance().getReference().child("Leisure");
    }

    private void loadLeisure() {
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("leisures");
        //get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);
                    postList.add(leisureModel);
                    //adapter
                    postAdapter = new LeisureAdapter(SearchAllLeisure.this, postList);
                    postAdapter.notifyDataSetChanged();
                    //set adapter to recyclerview
                    rvLeisureCategory.setAdapter(postAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(SearchAllLeisure.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // goto previous activity
        return super.onSupportNavigateUp();
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
////        menu.findItem(R.id.about_us_mm).setVisible(false);
//        //search listener
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                //called when user press search button
//                if (!TextUtils.isEmpty(s)) {
//                    searchLeisure(s);
//                } else {
//                    loadLeisure();
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
//                    loadLeisure();
//                }
//                return false;
//            }
//        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchLeisure(String searchQuery) {
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Leisure");
        //get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);
                    if (leisureModel.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                        postList.add(leisureModel);
                    }
                    //adapter
                    postAdapter = new LeisureAdapter(SearchAllLeisure.this, postList);
                    //set adapter to recyclerview
                    rvLeisureCategory.setAdapter(postAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(SearchAllLeisure.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
