package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.thatchosenone.travelmakati.models.LeisureModel;

import java.util.ArrayList;
import java.util.List;

public class YourBusiness extends AppCompatActivity {

    private Toolbar toolbar;
    Button btnJoin;

    RecyclerView rvYBusiness;
    Spinner sList;
    String listItem;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference totalRef, leisureRef;
    FirebaseUser firebaseUser;

    List<LeisureModel> leisureList;
    //YourBusinessAdapter ybAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_business);

        toolbar = findViewById(R.id.tb_y_business);
        btnJoin = findViewById(R.id.ayb_btn_busi);
        rvYBusiness = findViewById(R.id.ayb_rv_yours);
        sList = findViewById(R.id.ayb_s_busi);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        leisureRef = firebaseDatabase.getReference("Leisure");


        toolbar.setTitle("Your Business");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ybusi = new Intent(YourBusiness.this, LeisureAdvertisement.class);
                startActivity(ybusi);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recycler view
        rvYBusiness.setLayoutManager(layoutManager);

        //init post list
        leisureList = new ArrayList<>();

        list();
    }

    private void list() {
        List<String> categories = new ArrayList<>();
        categories.add(0, "All");
        categories.add("Hotels");
        categories.add("Malls");
        categories.add("Parks");
        categories.add("Nightlife");
        categories.add("Restaurant");
        categories.add("Museum");
        categories.add("Art Galleries");
        categories.add("Cafe / Tea Shop");

        //Style and populate the spinner
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        //Dropdown layout stylea
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Attaching array adapter to spinner
        sList.setAdapter(arrayAdapter);

        sList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("All")) {
                    //get all data from thi3t6rs ref
                    //path of all posts
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Leisure");
                    //get all data from this ref
                    ref.orderByChild("date_time").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            leisureList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                LeisureModel leisureModel = ds.getValue(LeisureModel.class);
//                                if (leisureModel.getUser_uid().equals(firebaseUser.getUid())) {
//                                    leisureList.add(leisureModel);
//                                }
//                                //adapter
//                                ybAdapter = new YourBusinessAdapter(YourBusiness.this, leisureList);
//                                ybAdapter.notifyDataSetChanged();
//                                //set adapter to recyclerview
//                                //rvPopular.setAdapter(postAdapter);
//                                rvYBusiness.setAdapter(ybAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //in case of error
                            Toast.makeText(YourBusiness.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    //path of all posts
                } else {
//                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
//                    startActivity(intent);
                    listItem = parent.getItemAtPosition(position).toString();
                    loadYourBusiness(listItem);
                    // Show selected item
                    //Toast.makeText(parent.getContext(), "Selected: " + listItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


    }

    private void loadYourBusiness(String listItem) {
//        //path of all posts
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Leisure");
//        //get all data from this ref
//        ref.orderByChild("date_time").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                leisureList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);
////                    if (leisureModel.getUser_uid().equals(firebaseUser.getUid())) {
////                        if (leisureModel.getCategory().equals(listItem)) {
////                            leisureList.add(leisureModel);
////                        }
////
////                    }
//                    //adapter
//                    ybAdapter = new YourBusinessAdapter(YourBusiness.this, leisureList);
//                    ybAdapter.notifyDataSetChanged();
//                    //set adapter to recyclerview
//                    //rvPopular.setAdapter(postAdapter);
//                    rvYBusiness.setAdapter(ybAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //in case of error
//                Toast.makeText(YourBusiness.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // goto previous activity
        return super.onSupportNavigateUp();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//
//        //searchview to search posts by post title description
//        MenuItem item = menu.findItem(R.id.search_all_mm);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        //hide addpost icon from this fragment
//        menu.findItem(R.id.add_search_mm).setVisible(false);
//        menu.findItem(R.id.add_post_mm).setVisible(false);
//        menu.findItem(R.id.sign_out_mm).setVisible(false);
//        menu.findItem(R.id.your_business_mm).setVisible(false);
//        //menu.findItem(R.id.setting_mm).setVisible(false);
//        menu.findItem(R.id.about_us_mm).setVisible(false);
//
//        return super.onCreateOptionsMenu(menu);
//    }

}
