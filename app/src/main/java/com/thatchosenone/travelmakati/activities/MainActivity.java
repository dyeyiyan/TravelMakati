package com.thatchosenone.travelmakati.activities;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thatchosenone.travelmakati.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private long backPressedTime;
    private Toast backToast;
    private String TAG;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;


    private ArrayList<String> cNames = new ArrayList<>();
    private ArrayList<Integer> cImageUrls = new ArrayList<Integer>();

    private ArrayList<String> tNames = new ArrayList<>();
    private ArrayList<Integer> tImageUrls = new ArrayList<Integer>();
    private ArrayList<String> tDesc = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //categoryMain();
        Toolbar toolbar = findViewById(R.id.toolbar_am);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_am);
//        collapsingToolbarLayout.setTitle("");

        String uid;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
//        uid = firebaseAuth.getCurrentUser().getUid();


        drawerLayout = findViewById(R.id.drawer_layout_am);
        navigationView = findViewById(R.id.navigation_view_am);

        //final CircleImageView circleImageView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.civ_userPic);
//        final TextView textViewName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvName_nv);
//        final TextView textViewEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvEmail_nv);


        if (firebaseUser != null) {
            StorageReference storageReference = firebaseStorage.getReference();
            storageReference.child("UserPicture").child(firebaseUser.getUid()).child("DisplayPhoto").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                   // Picasso.get().load(uri).fit().centerCrop().into(circleImageView);
                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
//                        AccountData accountData = dataSnapshot.getValue(AccountData.class);
//                        String myFirstname = dataSnapshot.child("firstname").getValue().toString();
//                        String myLastname = dataSnapshot.child("lastname").getValue().toString();
//                    //String myEmail = dataSnapshot.child("email").getValue().toString();
                        String firstname = String.valueOf(dataSnapshot.child("firstname").getValue());
                        String lastname = String.valueOf(dataSnapshot.child("lastname").getValue());
//                        textViewName.setText("Name: " + firstname + " " + lastname);
//                        //textViewEmail.setText("Email: " + myEmail);
//                        textViewEmail.setText("Email: " + firebaseUser.getEmail());


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            Log.d("TAG", "firebaseUser is null");
        }


//
//        final String uid;


        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new AboutTramakatiFragment()).commit();
//            navigationView.setCheckedItem(R.id.about_tra_mak_nm);
//        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_am, new BestPlacesFragment()).commit();
     //   navigationView.setCheckedItem(R.id.home_nm);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
//            case R.id.setting_mm:
//                Toast.makeText(MainActivity.this, "Setting is Clicked", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.about_us_mm:
//                Toast.makeText(MainActivity.this, "About us is Clicked", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.sign_out_mm:
//                firebaseAuth.signOut();
//                Intent signOut = new Intent(MainActivity.this, Login.class);
//                signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(signOut);
//                finish();
//                break;
//            case R.id.search_all_mm:
//                Toast.makeText(MainActivity.this, "Search is Clicked", Toast.LENGTH_SHORT).show();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        //Handles navigation view item clicked
        switch (menuItem.getItemId()) {
//            case R.id.home_nm:
////                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                fragmentTransaction.replace(R.id.fragment_container_am, new BestPlacesFragment());
////                fragmentTransaction.commit();
//                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_am, new BestPlacesFragment()).commit();
//                break;
//            case R.id.best_places_nm:
//                startActivity(new Intent(MainActivity.this, BestPlaces.class));
//                finish();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_am, new DiscoverFragment()).commit();
                //break;
//            case R.id.events_nm:
//                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_am, new DiscoverFragment()).commit();
//                break;
////            case R.id.maps_nm:
//                startActivity(new Intent(MainActivity.this, MakatiMap.class));
//                finish();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_am, new MakatiMapFragment()).commit();
               // break;
//            case R.id.advertisement_nm:
//                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_am, new AdvertisementFragment()).commit();
//                break;
//            case R.id.officials_nm:

                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_am, new OfficialsFragment()).commit();
                //break;
//            case R.id.em_hot_line_nm:
//                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_am, new EmergencyHotLineFragment()).commit();
//                break;

//            case R.id.account_info_nm:
//                startActivity(new Intent(MainActivity.this, Profile.class));
//                finish();
//                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_am, new EmergencyHotLineFragment()).commit();
//                break;
//            case R.id.sign_out_nm:
//                firebaseAuth.signOut();
//                Intent signOut = new Intent(MainActivity.this, Login.class);
//                signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(signOut);
//                finish();
//                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                return;
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            Intent homePage = new Intent(MainActivity.this, MainActivity.class);
            homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homePage);
            finish();
        }
    }


}
