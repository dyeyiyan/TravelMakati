package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.adapters.LeisurePhotosAdapter;
import com.thatchosenone.travelmakati.adapters.ReviewAdapter;
import com.thatchosenone.travelmakati.models.LeisurePhotosModel;
import com.thatchosenone.travelmakati.models.ReviewModel;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AllLeisureDetails extends AppCompatActivity {

    Toolbar tbLDetails;
    CollapsingToolbarLayout ctlDetails;
    AppBarLayout ablDetails;
    ImageView ivLPic;
    TextView tvAddress, tvName, tvCateg, tvPrice;
    //MaterialButton btnFav, btnMessage;
    LinearLayout llMyRates, llDRates;

//    TextView tvsmPhotos, tvsmAnnounce, tvsmReviews;
//    TextView tvAddReview, tvMTime, tvMComment, tvName;
//    Button btnAddReview, btnDirection;

    RecyclerView rvPhotos, rvReviews;
    MaterialRatingBar ratingBar, mrtMRate, mrtARate;


    List<ReviewModel> postList;
    List<LeisurePhotosModel> photoList;

    ReviewAdapter postAdapter;
    LeisurePhotosAdapter photoAdapter;

    //List<AnnouncementModel> postLists;
    //AnnouncementAdapter postAdapters;


    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference leisureRef;
    //tRatingRef, ratingRef, favRef, aveRateRef;
    FirebaseUser firebaseUser;

    String lUID, lname, lcategory, laddress, lprice, lstreet, lbarangay, lcity, lpic, ltotal, lhours, ldesc, lcontacts, luserid, lat, lon, lAveRates;

    boolean addFav = false;
    boolean addRev = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leisure_details);

        tbLDetails = findViewById(R.id.ald_tb_leisure);
        ctlDetails = findViewById(R.id.adc_ctl_image);
        ablDetails = findViewById(R.id.adc_appbar);
        ivLPic = findViewById(R.id.ald_iv_leisure_pic);
        tvCateg = findViewById(R.id.ald_tv_categ);
        tvAddress = findViewById(R.id.ald_tv_address);
        tvName = findViewById(R.id.ald_tv_name);
        tvPrice = findViewById(R.id.ald_tv_price);
//        tvTReviews = findViewById(R.id.ald_tv_reviews);
//        tvContacts = findViewById(R.id.ald_tv_conta);
//        tvDesc = findViewById(R.id.ald_tv_description);
//        tvHours = findViewById(R.id.ald_tv_hours);
//        tvAveRates = findViewById(R.id.ald_tv_ave_rate);
//        tvTotalRates = findViewById(R.id.ald_tv_total);
        llDRates = findViewById(R.id.ll_add_rates);
        //llMyRates = findViewById(R.id.ll_my_rates);

        //fabMessage = findViewById(R.id.aald_fab_message);

//        tvsmPhotos = findViewById(R.id.ald_tv_more_photos);
//        tvsmAnnounce = findViewById(R.id.ald_tv_more_announce);
//        tvsmReviews = findViewById(R.id.ald_tv_more_highlights);
//
//        tvAddReview = findViewById(R.id.ald_tv_add_rate);
//        tvMComment = findViewById(R.id.ald_tv_comment);
//        tvMTime = findViewById(R.id.ald_tv_mtime);
//
//        btnFav = findViewById(R.id.ald_mbtn_fav);
//        btnMessage = findViewById(R.id.ald_mbtn_message);
//        //btnFav = findViewById(R.id.aald_btn_fav);
//        btnAddReview = findViewById(R.id.aald_btn_review);
//        btnDirection = findViewById(R.id.aald_btn_dir);

        ratingBar = findViewById(R.id.aald_ratings_view);
        //mrtMRate = findViewById(R.id.ald_mrb_mrate);

        rvPhotos = findViewById(R.id.ald_rv_photo);
        //rvAnnounce = findViewById(R.id.aald_rv_leisure_announce);
        rvReviews = findViewById(R.id.ald_rv_reviews);
        //rvMyReviews = findViewById(R.id.aald_rv_your_reviews);


        LinearLayoutManager reviewsManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        //show newest post first, for this load from last
        reviewsManager.setStackFromEnd(true);
        reviewsManager.setReverseLayout(true);

//        LinearLayoutManager layoutManagerss = new LinearLayoutManager(this);
//        //show newest post first, for this load from last
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(true);
//
//
//        LinearLayoutManager layoutManagers = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
//        //show newest post first, for this load from last
//        layoutManagers.setStackFromEnd(true);
//        layoutManagers.setReverseLayout(true);

        //set layout to recycler view
        rvReviews.setLayoutManager(reviewsManager);
        //rvMyReviews.setLayoutManager(layoutManagerss);
        //rvPhotos.setLayoutManager(layoutManagers);

        //init post list
        postList = new ArrayList<>();
        loadAllReviews();
        //loadMyReviews();

        //init post list
        photoList = new ArrayList<>();
        loadPhotos();

        //tbLDetails.setTitle("Details");
        setSupportActionBar(tbLDetails);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //ctlDetails.setTitle("Title");
        ablDetails.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    ctlDetails.setTitle("Details"); // Careful! There should be a space between double quote. Otherwise it won't work.
                    isShow = false;
                } else if (!isShow) {
                    ctlDetails.setTitle(" ");
                    isShow = true;
                }
            }
        });

        Intent intent = getIntent();
        lUID = intent.getStringExtra("luid");

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        leisureRef = firebaseDatabase.getReference("leisures");
//        aveRateRef = firebaseDatabase.getReference("leisureAveRates");
//        ratingRef = firebaseDatabase.getReference("ratings");
//        favRef = firebaseDatabase.getReference("favorites");

//        btnAddReview.setOnClickListener(v -> {
//            Intent addreview = new Intent(AllLeisureDetails.this, AddReview.class);
//            addreview.putExtra("luid", lUID);
//            addreview.putExtra("lname", lname);
//            startActivity(addreview);
//        });
//
//        btnFav.setOnClickListener(v -> {
//            addFav();
//        });
//
//        btnMessage.setOnClickListener(v -> {
//            addMessage();
//        });
//
//        btnDirection.setOnClickListener(v -> {
//            Intent intent1 = new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lon));
//            startActivity(intent1);
//        });
//
//        tvsmReviews.setOnClickListener(v -> {
//            Intent addreview = new Intent(AllLeisureDetails.this, AllReviews.class);
//            addreview.putExtra("luid", lUID);
//            addreview.putExtra("lname", lname);
//            addreview.putExtra("ald", "all_details");
//            startActivity(addreview);
//        });
//
//        tvsmPhotos.setOnClickListener(v -> {
//            Intent addreview = new Intent(AllLeisureDetails.this, AllPhotos.class);
//            addreview.putExtra("luid", lUID);
//            addreview.putExtra("lname", lname);
//            addreview.putExtra("ald", "all_details");
//            startActivity(addreview);
//        });

        leisureRef.orderByChild("leisureID").equalTo(lUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    lname = "" + ds.child("name").getValue();
                    laddress = "" + ds.child("address").getValue();
                    lcategory = "" + ds.child("category").getValue();
                    lprice = "" + ds.child("price").getValue();
                    lpic = "" + ds.child("leisurePhoto").getValue();

                    //set data
                    tvName.setText(lname);
                    tvAddress.setText(laddress);
                    tvCateg.setText(lcategory);
                    tvPrice.setText(lprice);

                    //ratingBar.setRating(nltotal);
                    //try and catch
                    try {
                        Picasso.get().load(lpic).fit().centerCrop().into(ivLPic);
                        //if image is received the set
                    } catch (Exception e) {
                        //if theres any exception while getting image then set default
                        //Picasso.get().load(R.drawable.ic_default_photo).into(ivUserPic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AllLeisureDetails.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        aveRateRef.orderByChild("luid").equalTo(lUID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String totalRate = "" + ds.child("total_rate").getValue();
//                    tvAveRates.setText(totalRate);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(AllLeisureDetails.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        ratingRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                float total = 0;
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    for (DataSnapshot dss : ds.getChildren()) {
//                        String luid = "" + dss.child("luid").getValue();
//                        String pnt = "" + dss.child("point").getValue();
//                        String userid = "" + dss.child("user_uid").getValue();
//                        String timestapmp = "" + dss.child("date_time").getValue();
//                        String rate = "" + dss.child("rate_value").getValue();
//                        String comment = "" + dss.child("comment").getValue();
//
//                        //conver t timestamp to dd/mm/yyyy hh:mm:pm
//                        Calendar calendar = Calendar.getInstance(Locale.getDefault());
//                        calendar.setTimeInMillis(Long.parseLong(timestapmp));
//                        String pTime = DateFormat.format("dd MMM yyyy 'at' hh:mm aa", calendar).toString();
//                        float nratings = Float.parseFloat(rate);
//
//                        float npnt = Float.parseFloat(pnt);
//                        if (luid.equals(lUID)) {
//                            total = total + npnt;
//                            if (total >= 1) {
//                                tvTotalRates.setText("Out of " + total);
//                            } else {
//                                tvTotalRates.setText("Out of 0");
//                            }
//                        }
//
//                        if (firebaseUser.getUid().equals(userid) && luid.equals(lUID)) {
//                            llMyRates.setVisibility(View.VISIBLE);
//                            llDRates.setVisibility(View.GONE);
//                            mrtMRate.setRating(nratings);
//                            tvMTime.setText(pTime);
//                            tvMComment.setText(comment);
//                        } else {
//                            llDRates.setVisibility(View.VISIBLE);
//                            llMyRates.setVisibility(View.GONE);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(AllLeisureDetails.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
////        ratingRef.orderByChild("user_uid").equalTo(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                for (DataSnapshot ds : dataSnapshot.getChildren()) {
////                    for (DataSnapshot dss : ds.getChildren()) {
////                        if (dss.getValue() == null) {
////
////                            llDRates.setVisibility(View.VISIBLE);
////                        } else {
////                            llMyRates.setVisibility(View.GONE);
////                            llMyRates.setVisibility(View.VISIBLE);
////                        }
////                    }
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////                Toast.makeText(AllLeisureDetails.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
////            }
////        });
//        favRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(firebaseUser.getUid()).hasChild(lUID)) {
//                    //user has liked this post
//                    /*To indicate that the post if like by this (Signed IN) user
//                     * Change drawable left icon of like button
//                     * change text of like button from "Like" to "Like"*/
//                    btnFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_red, 0, 0, 0);
//                    //btnFav.setText("Favorites");
//                } else {
//                    /*To indicate that the post if like by this (Signed IN) user
//                     * Change drawable left icon of like button
//                     * change text of like button from "Like" to "Like"*/
//                    btnFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.default_heart, 0, 0, 0);
//                    //btnFav.setText("Add to favorites");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
    }

    private void addMessage() {

        Intent chat = new Intent(AllLeisureDetails.this, Chat.class);
        chat.putExtra("hisUID", lUID);
        chat.putExtra("chats", "leisure");
        startActivity(chat);

    }

    private void sortby() {
//
//        List<String> sort = new ArrayList<>();
//        sort.add(0, "All");
//        sort.add("Promos");
//        sort.add("Offers");
//        //Style and populate the spinner
//        ArrayAdapter<String> arrayAdapter;
//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sort);
//
//        //Dropdown layout stylea
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        //Attaching array adapter to spinner
//        sSort.setAdapter(arrayAdapter);
//
//        sSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//                sortItem = parent.getItemAtPosition(position).toString();
//                loadAllAnnounce(sortItem);
//                // Show selected item
//                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });


    }

    private void loadAllAnnounce(String sortItem) {
//        if (sortItem.equals("All")) {
//            //path of all posts
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Announcement");
//            //get all data from this ref
//            ref.orderByChild("ptime").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    postLists.clear();
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        AnnouncementModel announcementModel = ds.getValue(AnnouncementModel.class);
//                        if (announcementModel.getLuid().equals(lUID)) {
//                            postLists.add(announcementModel);
//                        }
//                        //adapter
////                        postAdapters = new AnnouncementAdapter(AllLeisureDetails.this, postLists);
////                        //set adapter to recyclerview
////                        rvAnnounce.setAdapter(postAdapters);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    //in case of error
//                    Toast.makeText(AllLeisureDetails.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else if (sortItem.equals("Promos")) {
//            final DatabaseReference nRates = FirebaseDatabase.getInstance().getReference("Announcement");
//            nRates.orderByChild("ptime").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    postLists.clear();
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        AnnouncementModel announcementModel = ds.getValue(AnnouncementModel.class);
//                        if (announcementModel.getLuid().equals(lUID)) {
//                            if (announcementModel.getType().equals(sortItem)) {
//                                postLists.add(announcementModel);
//                            }
//                        }
//                        //adapter
////                        postAdapters = new AnnouncementAdapter(AllLeisureDetails.this, postLists);
////                        //set adapter to recyclerview
////                        rvAnnounce.setAdapter(postAdapters);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                }
//            });
//        } else {
//            final DatabaseReference nRates = FirebaseDatabase.getInstance().getReference("Announcement");
//            nRates.orderByChild("ptime").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    postList.clear();
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        AnnouncementModel announcementModel = ds.getValue(AnnouncementModel.class);
//                        if (announcementModel.getLuid().equals(lUID)) {
//                            if (announcementModel.getType().equals(sortItem)) {
//                                postLists.add(announcementModel);
//                            }
//                        }
//                        //adapter
////                        postAdapters = new AnnouncementAdapter(AllLeisureDetails.this, postLists);
////                        //set adapter to recyclerview
////                        rvAnnounce.setAdapter(postAdapters);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                }
//            });
//        }
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
                    if (photoModel.getLuid().equals(lUID)) {
                        photoList.add(photoModel);
                    }
                    //adapter
                    photoAdapter = new LeisurePhotosAdapter(AllLeisureDetails.this, photoList);
                    //set adapter to recyclerview
                    rvPhotos.setAdapter(photoAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(AllLeisureDetails.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFav() {

//        addFav = true;
//        //get id of the post clicked
//        //final String postIde = postList.get(position).getDate_time();
//        favRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (addFav) {
//                    if (dataSnapshot.child(firebaseUser.getUid()).hasChild(lUID)) {
//                        //already liked, so remove like
//                        favRef.child(firebaseUser.getUid()).child(lUID).removeValue();
//                        Toast.makeText(AllLeisureDetails.this, "Deleted to your favorites", Toast.LENGTH_SHORT).show();
//                        addFav = false;
//
//                    } else {
//                        //not liked, like it
//                        HashMap<String, Object> leisureMap = new HashMap<>();
//                        //put post info
//                        leisureMap.put("id", lUID);
//                        //path to store post data
//                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favorites");
//                        //put data in this ref
//                        ref.child(firebaseUser.getUid()).child(lUID).setValue(leisureMap);
//                        Toast.makeText(AllLeisureDetails.this, "Added to your favorites", Toast.LENGTH_SHORT).show();
//                        //favRef.child(firebaseUser.getUid()).child(lUID).child("favorites").setValue("true");
//                        addFav = false;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void loadAllReviews() {
        //path of all posts
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings");
//        //get all data from this ref
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                postList.clear();
//                for (DataSnapshot usersIDChild : dataSnapshot.getChildren()) {
//                    for (DataSnapshot usersTimeChild : usersIDChild.getChildren()) {
//                        ReviewModel reviewModel = usersTimeChild.getValue(ReviewModel.class);
//                        if (reviewModel.getLuid().equals(lUID) && reviewModel.getIsDeleted().equals("0")) {
//                            postList.add(reviewModel);
//                        }                        //adapter
//                        postAdapter = new ReviewAdapter(AllLeisureDetails.this, postList);
//                        //set adapter to recyclerview
//                        rvReviews.setAdapter(postAdapter);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //in case of error
//                Toast.makeText(AllLeisureDetails.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.leisure_details_menu, menu);
//        MenuItem item = menu.findItem(R.id.ldm_share);
//
//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                share();
//                return false;
//            }
//        });

        return super.onCreateOptionsMenu(menu);

    }

    private void share() {
//        Query leisureQuery = usersDbRef.orderByChild("luid").equalTo(lUID);
//        leisureQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    lname = "" + ds.child("name").getValue();
//                    lcategory = "" + ds.child("category").getValue();
//                    lstreet = "" + ds.child("street").getValue();
//                    lpic = "" + ds.child("photo").getValue();
//                    lprice = "" + ds.child("price").getValue();
//
//                    try {
//                        Picasso.get().load(lpic).fit().centerCrop().into(ivLPic);
//                        //if image is received the set
//                    } catch (Exception e) {
//                        //if theres any exception while getting image then set default
//                        //Picasso.get().load(R.drawable.ic_default_photo).into(ivUserPic);
//                    }
//                    String sharebody = lname + "\n" + lstreet.concat(", " + lbarangay + ", " + lcity) + "\n" + lcategory + "\n" + "Price starts at: " + lprice;
//                    //share intent
//                    Intent sintent = new Intent(Intent.ACTION_SEND);
//                    sintent.setType("text/plain");
//                    sintent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here"); // incase you share via email app
//                    sintent.putExtra(Intent.EXTRA_TEXT, sharebody); //text to share
//                    startActivity(Intent.createChooser(sintent, "Share Via")); //message to show in share dialog
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

//            case R.id.ldm_add_fav:
//                addFav();
//                favRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.child(firebaseUser.getUid()).hasChild(lUID)) {
//                            //user has liked this post
//                            /*To indicate that the post if like by this (Signed IN) user
//                             * Change drawable left icon of like button
//                             * change text of like button from "Like" to "Like"*/
//                            item.setTitle("Delete to Favorites");
//                        } else {
//                            /*To indicate that the post if like by this (Signed IN) user
//                             * Change drawable left icon of like button
//                             * change text of like button from "Like" to "Like"*/
//                            item.setTitle("Add to Favorites");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
//                break;

            case R.id.ldm_review:
                Intent addreview = new Intent(AllLeisureDetails.this, AddReview.class);
                addreview.putExtra("luid", lUID);
                addreview.putExtra("lname", lname);
                startActivity(addreview);
                break;

            case R.id.ldm_direction:
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lon));
                startActivity(intent);
                break;

            case R.id.ldm_share:
                Intent intents = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lon));
                startActivity(intents);
                break;

//            case R.id.ldm_add_photo:
//                showImageDialog();
//                break;
//
//            case R.id.ldm_message:
//                Intent addreview = new Intent(AllLeisureDetails.this, Chat.class);
//                addreview.putExtra("hisUID", lUID);
//                addreview.putExtra("chats", "leisure");
//                startActivity(addreview);
//                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
