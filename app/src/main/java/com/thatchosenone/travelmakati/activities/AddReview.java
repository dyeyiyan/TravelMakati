package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.R;

import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AddReview extends AppCompatActivity {

    Toolbar tbAReviews;
    String lUID, lNAME;
    MaterialRatingBar mrbRate;
    TextInputLayout tilExpi;
    //ImageView ivReview;
    MaterialButton btnPost;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ratingRef, userDbRef, totalRef, totalRatingRef;
    FirebaseUser firebaseUser;

//    //image uri will be same in this uri
//    Uri image_uri = null;
//
//    //permission constant
//    private static final int CAMERA_REQUEST_CODE = 100;
//    private static final int STORAGE_REQUEST_CODE = 200;
//
//    //image pick constant
//    private static final int IMAGE_PICK_CAMERA_CODE = 300;
//    private static final int IMAGE_PICK_GALLERY_CODE = 400;
//
//    //permission array
//    String[] cameraPermission;
//    String[] storagePermission;

    String expi, nave, name, dp;
    float rating;
    private ProgressDialog progressDialog;

    String editExpi, editRatings, editImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        tbAReviews = findViewById(R.id.tb_rate_us);
        mrbRate = findViewById(R.id.aar_mrb_leisure);
        tilExpi = findViewById(R.id.aar_til_expi);
        btnPost = findViewById(R.id.aar_btn_post);

//        //init permission arrays
//        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        Intent intent = getIntent();
        lUID = intent.getStringExtra("luid");
        lNAME = intent.getStringExtra("lname");

        Intent intent1 = getIntent();
        String isUpdateKey = "" + intent1.getStringExtra("key");
        String editReviewID = "" + intent1.getStringExtra("editReviewID");
        String editReviewLUID = "" + intent1.getStringExtra("editReviewLUID");


        if (isUpdateKey.equals("editPost")) {
            tbAReviews.setTitle("Update Review");
            setSupportActionBar(tbAReviews);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            btnPost.setText("Update");
            loadReviewData(editReviewID);
        } else {
            tbAReviews.setTitle(lNAME);
            setSupportActionBar(tbAReviews);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ratingRef = firebaseDatabase.getReference("ratings");
        totalRef = firebaseDatabase.getReference("totalRatingsPerLeisure");
        totalRatingRef = firebaseDatabase.getReference("totalRating/accumulated");

//        ratingRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot userRates: dataSnapshot.getChildren()){
//                    String luidR = "" + userRates.child("luid").getValue();
//                    String dateR = "" + userRates.child("date_time").getValue();
//                    if(luidR.equalsIgnoreCase(lUID)){
//                        loadReviewData(dateR);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(AddReview.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        progressDialog = new ProgressDialog(this);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expi = tilExpi.getEditText().getText().toString().trim();
                rating = mrbRate.getRating();
                if (!validateExpi() | !validateRating()) {
                    progressDialog.dismiss();
                    return;
                } else {
                    if (isUpdateKey.equals("editPost")) {
                        storeTotalRatess(editReviewLUID);
                        beginUpdate(expi, rating, editReviewID);
                    } else {
                        storeTotalRates();
                        storeRatings(expi, rating);
                        storaLeisureRatings(lUID);
                        storaTotalRatings();
                        storeTestResult();
                    }
                }
            }
        });

    }

    private void storeTestResult() {
        /* 1. if totalRatingRef is not success throw an error
         * 2. else query to get the snapshot
         * 3. once you get the snapshot
         * 4. set datatype double of mae, nmae, accuracy, precesion and recall as 0.0
         * 5. get the value of totalLeisureRate, totalHasRate, totalRateMore from the snapshot
         * 6. since the datatype are String, convert the data to double to perform calculation
         * 7. do a calculation of mae, nmae, accuracy , precision and recall
         * 8. after that save to database
         */
        totalRatingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double  mae = 0.0,
                        nmae = 0.0,
                        accuracy = 0.0,
                        precision = 0.0,
                        recall = 0.0;

                String totalLeisureRate = "" + dataSnapshot.child("totalLeisureRate").getValue();
                String totalHasRate = "" + dataSnapshot.child("totalUserHasRate").getValue();
                String totalRateMore = "" + dataSnapshot.child("totalUserRates>3").getValue();

                double tlr = Double.parseDouble(totalLeisureRate);
                double thr = Double.parseDouble(totalHasRate);
                double trm = Double.parseDouble(totalRateMore);

                mae = (((thr * 5) - tlr ) / thr);
                nmae = mae / (5-2);
                accuracy = tlr / (thr * 5);
                precision = trm / thr;
                recall = trm / 3000;

                HashMap<String, Object> testMap = new HashMap<>();
                //put post info
                testMap.put("mae", "" + mae);
                testMap.put("nmae", "" + nmae);
                testMap.put("accuracy", "" + accuracy);
                testMap.put("precision", "" + precision);
                testMap.put("recall", "" + recall);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("test");
                ref.child("result").updateChildren(testMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddReview.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storaTotalRatings() {
        totalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double total = 0.0;
                int utotal = 0;
                int totalmorethan = 0;

                for(DataSnapshot dss: dataSnapshot.getChildren()){
                    String rate = "" + dss.child("rateTotal").getValue();
                    String countone = "" + dss.child(("countHasRates")).getValue();
                    String counttwo = "" + dss.child(("countHasRate>3")).getValue();

                    double nrate = Double.parseDouble(rate);
                    int npoint = Integer.parseInt(countone);
                    int npoints = Integer.parseInt(counttwo);

                    total = total + nrate; // total leisure rate
                    utotal = utotal + npoint; // total user rate
                    totalmorethan = totalmorethan + npoints; // total user has rate more than 3

                }

                HashMap<String, Object> totalViewMap = new HashMap<>();
                //put post info
                totalViewMap.put("totalLeisureRate", "" + total);
                totalViewMap.put("totalUserHasRate", "" + utotal);
                totalViewMap.put("totalUserRates>3", "" + totalmorethan);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("totalRating");
                ref.child("accumulated").updateChildren(totalViewMap);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void storaLeisureRatings(String lUID) {
        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total = 0.0;
                int utotal = 0;
                int totalmorethan = 0;

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                        for(DataSnapshot dss: ds.getChildren()){
                            String rate = "" + dss.child("rateValue").getValue();
                            String point = "" + dss.child(("points")).getValue();
                            String luid = "" + dss.child("leisureID").getValue();
                            double nrate = Double.parseDouble(rate);
                            int npoint = Integer.parseInt(point);

                            if(lUID.equals(luid)){
                                total = total + nrate;
                                utotal = utotal + npoint;
                                if(nrate>3){
                                    totalmorethan = totalmorethan + npoint;
                                }
                            }



                        }
                        Log.d("TAG",  "titi" + total);
                    }

                HashMap<String, Object> totalViewMap = new HashMap<>();
                //put post info
                totalViewMap.put("rateTotal", "" + total);
                totalViewMap.put("countHasRate>3", "" + totalmorethan);
                totalViewMap.put("countHasRates", "" + utotal);
                totalViewMap.put("leisureID", "" + lUID);
                //totalViewMap.put("countHasRates", "" + countrates);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("totalRatingsPerLeisure");
                ref.child(lUID).updateChildren(totalViewMap);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void storeRatings(String expi, float rating) {
        progressDialog.setMessage("Publishing post...");
        progressDialog.show();

        final String rates = String.valueOf(rating);

        //for post image name, post-id, post-publish-time
        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<Object, String> ratingMap = new HashMap<>();
        //put post info
        //ratingMap.put("name", name);
        ratingMap.put("points", "1");
        ratingMap.put("isDeleted", "0");
        ratingMap.put("comment", expi);
        //ratingMap.put("account_photo", dp);
        ratingMap.put("rateValue", rates);
        ratingMap.put("timeStamp", timestamp);
        ratingMap.put("leisureID", lUID);
        ratingMap.put("uID", firebaseUser.getUid());

        //path to store post data
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings");
        //put data in this ref
        ref.child(firebaseUser.getUid()).child(timestamp).setValue(ratingMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added in database
                        progressDialog.dismiss();
                        EditText etExpi = findViewById(R.id.aar_et_expi);
                        Toast.makeText(AddReview.this, "Post Published", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding post in database
                        progressDialog.dismiss();
                        Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


//        userDbRef = FirebaseDatabase.getInstance().getReference("users");
//        Query query = userDbRef.orderByChild("u_id").equalTo(firebaseUser.getUid());
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String name = "" + ds.child("name").getValue();
//                    String dp = "" + ds.child("account_photo").getValue();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        if (ivReview.getDrawable() != null) {
//            //get some info of current user to include in post
//            userDbRef = FirebaseDatabase.getInstance().getReference("Users");
//            Query query = userDbRef.orderByChild("uid").equalTo(firebaseUser.getUid());
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        name = "" + ds.child("name").getValue();
//                        dp = "" + ds.child("display_photo").getValue();
//
//                        //get image from image view
//                        Bitmap bitmap = ((BitmapDrawable) ivReview.getDrawable()).getBitmap();
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        //image compress
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                        byte[] data = baos.toByteArray();
//                        //post with image
//                        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
//                        ref.putBytes(data)
//                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        //image is uploaded to firebase storage, now get it's uri
//                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                                        while (!uriTask.isSuccessful()) ;
//                                        String downloadUri = uriTask.getResult().toString();
//                                        if (uriTask.isSuccessful()) {
//                                            //url is received upload post to firebase database
//                                            HashMap<Object, String> ratingMap = new HashMap<>();
//                                            //put post info
//                                            ratingMap.put("name", name);
//                                            ratingMap.put("dp", dp);
//                                            ratingMap.put("comment", expi);
//                                            ratingMap.put("rate_value", rates);
//                                            ratingMap.put("rating_photo", downloadUri);
//                                            ratingMap.put("date_time", timestamp);
//                                            ratingMap.put("luid", lUID);
//                                            ratingMap.put("user_uid", firebaseUser.getUid());
//                                            ratingMap.put("point", "1");
//                                            ratingMap.put("plikes", "0");
//                                            //path to store post data
//                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ratings");
//                                            //put data in this ref
//                                            ref.child(timestamp).setValue(ratingMap)
//                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void aVoid) {
//                                                            //added in database
//                                                            progressDialog.dismiss();
//                                                            //reset views
//                                                            Toast.makeText(AddReview.this, "Post Published", Toast.LENGTH_SHORT).show();
//                                                            finish();
//                                                            //reset views
////                                                            etExpi.setText(null);
////                                                            mrbRate.setNumStars(0);
////                                                            image_uri = null;
//                                                        }
//                                                    })
//                                                    .addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            //failed adding post in database
//                                                            progressDialog.dismiss();
//                                                            Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                                        }
//                                                    });
//
//                                        }
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        //failed uploading image
//                                        progressDialog.dismiss();
//                                        Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                        // end
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//
//                }
//            });
//
//
//        } else {
//            //get some info of current user to include in post
//
//
//        }

//        //get image from image view
//        Bitmap bitmap = ((BitmapDrawable) ivReview.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        //image compress
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        //post with image
//        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
//        ref.putBytes(data)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        //image is uploaded to firebase storage, now get it's uri
//                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                        while (!uriTask.isSuccessful()) ;
//                        String downloadUri = uriTask.getResult().toString();
//                        if (uriTask.isSuccessful()) {
//                            //url is received upload post to firebase database
//                            HashMap<String, Object> leisureMap = new HashMap<>();
//                            //put post info
//                            leisureMap.put("comment", expi);
//                            leisureMap.put("rate_value", rates);
//                            leisureMap.put("photo", downloadUri);
//                            leisureMap.put("date_time", timestamp);
//                            leisureMap.put("luid", lUID);
//                            leisureMap.put("user_uid", firebaseUser.getUid());
//                            //path to store post data
//                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ratings");
//                            //put data in this ref
//                            ref.child(ratingKey).setValue(leisureMap)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            //added in database
//                                            progressDialog.dismiss();
//                                            Toast.makeText(AddReview.this, "Successfully Added", Toast.LENGTH_SHORT).show();
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            //failed adding post in database
//                                            progressDialog.dismiss();
//                                            Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    });
//
//
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //failed uploading image
//                        progressDialog.dismiss();
//                        Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

    }

    private void storeTotalRatess(String editReviewLUID) {

        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total = 0.0;
                double count = 0.0;
                double average = 0.0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dss : ds.getChildren()) {
                        String rates = "" + dss.child("rateValue").getValue();
                        String isdel = "" + dss.child("isDeleted").getValue();
                        String dates = "" + dss.child("leisureID").getValue();
                        double nrates = Double.parseDouble(rates);
                        if (dates.equals(editReviewLUID) && isdel.equals("0")) {
                            total = total + nrates;
                            count = count + 1;
                            average = total / count;
                        }
                    }
                }

                HashMap<String, Object> aveRateMap = new HashMap<>();
                //put post info
                aveRateMap.put("totalRate", "" + average);
                aveRateMap.put("leisureID", editReviewLUID);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("leisureAveRates");
                ref.child(editReviewLUID).updateChildren(aveRateMap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //
    private void storeTotalRates() {
        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total = 0.0;
                double count = 0.0;
                double average = 0.0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dss : ds.getChildren()) {
                        String rates = "" + dss.child("rateValue").getValue();
                        String isdel = "" + dss.child("isDeleted").getValue();
                        String uid = "" + dss.child("leisureID").getValue();
                        double nrates = Double.parseDouble(rates);
                        if (uid.equals(lUID) && isdel.equals("0")) {
                            total = total + nrates;
                            count = count + 1;
                            average = total / count;
                        }
                    }
                }

                HashMap<String, Object> aveRateMap = new HashMap<>();
                //put post info
                aveRateMap.put("totalRate", "" + average);
                aveRateMap.put("leisureID", lUID);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("leisureAveRates");
                ref.child(lUID).updateChildren(aveRateMap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void beginUpdate(String expi, float rating, String editReviewPID) {
        progressDialog.setMessage("Updating Post");
        progressDialog.show();
        updateWithOutImage(expi, rating, editReviewPID);
//        if (!editImage.equals("noImage")) {
//            //without image
//            updateWasWithImage(expi, rating, editReviewPID);
//        } else if (ivReview.getDrawable() != null) {
//            //with image
//            updateWithNowImage(expi, rating, editReviewPID);
//        } else {
//            //without image
//            updateWithOutImage(expi, rating, editReviewPID);
//        }
    }

    private void updateWithOutImage(String expi, float rating, String editReviewPID) {
        final String rates = String.valueOf(rating);

        HashMap<String, Object> ratingMap = new HashMap<>();
        //put post info
        ratingMap.put("comment", expi);
        ratingMap.put("rateValue", rates);
        //ratingMap.put("rating_photo", "noImage");
//        ratingMap.put("luid","-LpwH6_Vm5ryqAt-YTR3");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings");
        ref.child(firebaseUser.getUid() + "/" + editReviewPID)
                .updateChildren(ratingMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(AddReview.this, "Updated...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void updateWithNowImage(String expi, float rating, String editReviewPID) {
//        final String rates = String.valueOf(rating);
//        String timeStamp = String.valueOf(System.currentTimeMillis());
//        String filePathAndName = "Ratings/" + "photos" + timeStamp;
//        //get image from image view
//        Bitmap bitmap = ((BitmapDrawable) ivReview.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        //image compress
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
//        ref.putBytes(data)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        //image uploaded get its url
//                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                        while (!uriTask.isSuccessful()) ;
//                        String downloadUri = uriTask.getResult().toString();
//                        if (uriTask.isSuccessful()) {
//                            //url is received, upload to firebase database
//                            HashMap<String, Object> ratingMap = new HashMap<>();
//                            //put post info
//                            ratingMap.put("comment", expi);
//                            ratingMap.put("rate_value", rates);
//                            ratingMap.put("rating_photo", downloadUri);
//
//                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ratings");
//                            ref.child(editReviewPID)
//                                    .updateChildren(ratingMap)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(AddReview.this, "Updated...", Toast.LENGTH_SHORT).show();
//                                            finish();
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    });
//
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //image not uploaded
//                        progressDialog.dismiss();
//                        Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });


    }

    private void updateWasWithImage(String expi, float rating, String editReviewPID) {
        //post is with image, delete previous image first
//        StorageReference mPictureRef = FirebaseStorage.getInstance().getReferenceFromUrl(editImage);
//        mPictureRef.delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        //image deleted, upload new image
//                        //for post-image name, post id publish time
//                        final String rates = String.valueOf(rating);
//                        String timeStamp = String.valueOf(System.currentTimeMillis());
//                        String filePathAndName = "Ratings/" + "photos" + timeStamp;
//                        //get image from image view
//                        Bitmap bitmap = ((BitmapDrawable) ivReview.getDrawable()).getBitmap();
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        //image compress
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                        byte[] data = baos.toByteArray();
//
//                        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
//                        ref.putBytes(data)
//                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        //image uploaded get its url
//                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                                        while (!uriTask.isSuccessful()) ;
//                                        String downloadUri = uriTask.getResult().toString();
//                                        if (uriTask.isSuccessful()) {
//                                            //url is received, upload to firebase database
//                                            HashMap<String, Object> ratingMap = new HashMap<>();
//                                            //put post info
//                                            ratingMap.put("comment", expi);
//                                            ratingMap.put("rate_value", rates);
//                                            ratingMap.put("rating_photo", downloadUri);
//
//                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ratings");
//                                            ref.child(editReviewPID)
//                                                    .updateChildren(ratingMap)
//                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void aVoid) {
//                                                            progressDialog.dismiss();
//                                                            Toast.makeText(AddReview.this, "Updated...", Toast.LENGTH_SHORT).show();
//                                                            finish();
//                                                        }
//                                                    })
//                                                    .addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            progressDialog.dismiss();
//                                                            Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                                        }
//                                                    });
//
//                                        }
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        //image not uploaded
//                                        progressDialog.dismiss();
//                                        Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(AddReview.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
    }

    private void loadReviewData(String editReviewID) {
        EditText et = findViewById(R.id.aar_et_expi);
        float nr = Float.parseFloat(editReviewID);

        et.setText(editReviewID);
        mrbRate.setRating(nr);


//        ratingRef.orderByChild("date_time").equalTo("1583897344249").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    for (DataSnapshot dss : ds.getChildren()) {
//                        editExpi = "" + dss.child("comment").getValue();
//                        editRatings = "" + dss.child("rate_value").getValue();
//
//                        EditText et = findViewById(R.id.aar_et_expi);
//                        float nr = Float.parseFloat(editRatings);
//
//                        et.setText(editExpi);
//                        mrbRate.setRating(nr);
//
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

    //Validate Expi
    private boolean validateExpi() {
        String expi = tilExpi.getEditText().getText().toString().trim();

        if (expi.isEmpty()) {
            tilExpi.setError("Error: Field can't be empty");
            return false;
        } else {
            tilExpi.setError(null);
            return true;
        }
    }

    //Validate Expi
    private boolean validateRating() {
        final float ratings = mrbRate.getRating();
        if (ratings == 0.0) {
            Toast.makeText(AddReview.this, "Please rate", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            tilExpi.setError(null);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
