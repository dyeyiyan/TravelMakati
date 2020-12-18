package com.thatchosenone.travelmakati.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.thatchosenone.travelmakati.R;
//import com.thatchosenone.travelmakati.fragments.FavFragment;
import com.thatchosenone.travelmakati.fragments.ChatFragment;
import com.thatchosenone.travelmakati.fragments.FavFragment;
import com.thatchosenone.travelmakati.fragments.HomeFragment;
import com.thatchosenone.travelmakati.fragments.InternetConnection;
import com.thatchosenone.travelmakati.fragments.MapFragment;
import com.thatchosenone.travelmakati.notification.Token;
import com.thatchosenone.travelmakati.fragments.ProfileFragment;

public class ActivityMain extends AppCompatActivity {

    private static final String COMMON_TAG = "OrientationChange";

    GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;

    private BottomNavigationView mMainNav;
    private FrameLayout frameLayout;

    private HomeFragment homeFragment;
    private FavFragment favFragment;
    private MapFragment mapFragment;
    private ProfileFragment profileFragment;
    private ChatFragment chatFragment;
    private InternetConnection internetFragment;


    DatabaseReference categoryRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

//        int startIndex = (int)(Math.random() * 13);
//        String my = String.valueOf(startIndex);

        Toolbar toolbar = findViewById(R.id.tb_main);
        toolbar.setTitle("Travel Makati");
        setSupportActionBar(toolbar);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mMainNav = (BottomNavigationView) findViewById(R.id.bnv_main);
        frameLayout = (FrameLayout) findViewById(R.id.main_frame);

        homeFragment = new HomeFragment();
        //favFragment = new FavFragment();
        //mapFragment = new MapFragment();
        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();
        internetFragment = new InternetConnection();


        if (savedInstanceState == null) {
            setFragment(homeFragment);
        }
        // checkConnection();
        navigationMenu();
        checkUserStatus();

    }

    public void updateToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tokens");
        Token mToken = new Token(token);
        ref.child(firebaseUser.getUid()).setValue(mToken);
    }

    private void navigationMenu() {
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                mMainNav.getMenu().getItem(0).setIcon(R.drawable.home);
                mMainNav.getMenu().getItem(1).setIcon(R.drawable.event);
                mMainNav.getMenu().getItem(2).setIcon(R.drawable.discount);
                mMainNav.getMenu().getItem(3).setIcon(R.drawable.chat);
                mMainNav.getMenu().getItem(4).setIcon(R.drawable.account);

                switch (menuItem.getItemId()) {

                    case R.id.bnm_home:

                        menuItem.setIcon(R.drawable.home_r);
                        setFragment(homeFragment);
                        return true;

                    case R.id.bnm_event:

                        menuItem.setIcon(R.drawable.event_r);
                        setFragment(favFragment);
                        return true;

                    case R.id.bnm_deals:
                        menuItem.setIcon(R.drawable.discount_r);
                        setFragment(mapFragment);
                        return true;

                    case R.id.bnm_chat:
                        setFragment(chatFragment);
                        menuItem.setIcon(R.drawable.message_r);
                        return true;


                    case R.id.bnm_profile:
                        setFragment(profileFragment);

                        menuItem.setIcon(R.drawable.account_circle_r);
                        return true;

//                    case R.id.bnm_user_chat:
//                        setFragment(chatFragment);
//                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
//                        return true;

//                    case R.id.bnm_business:
//                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
//                        setFragment(businessFragment);
//                        return true;

//                    case R.id.bnm_news_feed:
//                        setFragment(newsfeedFragment);
//                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
//                        return true;


//                    case R.id.bnm_user_chat:
//                        setFragment(messageFragment);
//                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
//                        return true;

//                    case R.id.bnm_business:
//                        setFragment(businessFragment);
//                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
//                        return true;

                    default:
                        return false;

                }
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            //user is signed in stay here
            //set email of logged in user
            //myprofile set to user.getEmail

            //save uid of currently signed in user in shared preference
            myUID = user.getUid(); // currently signed in user's uid

            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID", myUID);
            editor.apply();

            //update Token
            updateToken(FirebaseInstanceId.getInstance().getToken());

        } else {
            //user not signed, goto main activity
            startActivity(new Intent(this, Login.class));
            finish();
        }

    }

//        @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//
////        MenuItem item = menu.findItem(R.id.search_all_mm);
////        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String s) {
////                firebaseSearch(s);
////                return false;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String s) {
////
////                firebaseSearch(s);
////                return false;
////            }
////        });
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.search_mm:
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(ActivityMain.this, AddRate.class);
//                startActivity(intent);
                break;

            case R.id.notif_mm:
                  Toast.makeText(this, "Notification clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(ActivityMain.this, AddRate.class);
//                startActivity(intent);
                break;

//            case R.id.your_business_mm:
//                Intent business = new Intent(ActivityMain.this, YourBusiness.class);
//                business.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                business.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(business);
//                break;
//            case R.id.about_us_mm:
//                Intent aboutus = new Intent(ActivityMain.this, AboutUs.class);
//                aboutus.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                aboutus.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(aboutus);
//                break;
//            case R.id.sign_out_mm:
//                final ProgressDialog progressDialog = new ProgressDialog(ActivityMain.this);
//                progressDialog.setMessage("Signing out...");
//                progressDialog.show();
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                Query query = reference.child("users").orderByChild("u_id").equalTo(firebaseUser.getUid());
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        progressDialog.dismiss();
//                        if (dataSnapshot.exists()) {
//                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                                String type = "" + issue.child("account_type").getValue();
//                                if (type.equalsIgnoreCase("1")) {
//                                    firebaseAuth.signOut();
//                                    Intent signOut = new Intent(ActivityMain.this, Login.class);
//                                    signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(signOut);
//                                    finish();
//                                }
//
//                                if (type.equalsIgnoreCase("3")) {
//                                    firebaseAuth.signOut();
//                                    LoginManager.getInstance().logOut();
//                                    Intent signOut = new Intent(ActivityMain.this, Login.class);
//                                    signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(signOut);
//                                    finish();
//                                }
//
//                                if (type.equalsIgnoreCase("2")) {
//                                    // Configure Google Sign In
//                                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                                            .requestIdToken(getString(R.string.default_web_client_id))
//                                            .requestEmail()
//                                            .build();
//                                    mGoogleSignInClient = GoogleSignIn.getClient(ActivityMain.this, gso);
//                                    mGoogleSignInClient.revokeAccess()
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    firebaseAuth.signOut();
//                                                    Intent signOut = new Intent(ActivityMain.this, Login.class);
//                                                    signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                    signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                    startActivity(signOut);
//                                                    finish();
//                                                }
//
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Toast.makeText(ActivityMain.this, "Unable to logout: " + e.getMessage(),
//                                                            Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//
//                                }
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//                break;
//            case R.id.add_post_mm:
//                Intent intent = new Intent(ActivityMain.this, AddRate.class);
//                startActivity(intent);
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(COMMON_TAG, "MainActivity onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(COMMON_TAG, "MainActivity onSaveInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i(COMMON_TAG, "Landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i(COMMON_TAG, "Portrait");
        }

    }

}
