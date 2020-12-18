package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.thatchosenone.travelmakati.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class AddRate extends AppCompatActivity {

    //views
    EditText etDescription, etTitle;
    ImageView ivPost;
    TextView tvLNames;
    Button btnUpload;

    //double ratingNumber;

    //private MaterialRatingBar ratingBar;

    //firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference userDbRef;
    DatabaseReference ratingsTbl;

    Toolbar tbAdd;

    ActionBar actionBar;

    //permission constant
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    //image pick constant
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    //permission array
    String[] cameraPermission;
    String[] storagePermission;

    //image uri will be same in this uri
    Uri image_uri = null;

    //user info
    String name, email, uid, dp, lUID;

    //progress bar
    ProgressDialog progressDialog;

    //info of post to be edited
    String editTitle, editDescription, editImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rate);

//        actionBar = getSupportActionBar();
//        actionBar.setTitle("Rate Me");
//        actionBar.setSubtitle(email);

//        //enable back button in action bar
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        //init permission arrays
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        progressDialog = new ProgressDialog(this);

        //get data through intent from previous activities adapter
        //get uid of clicked user to retrieve his posts
        Intent intent = getIntent();
        //lUID = intent.getStringExtra("leisureName");
        String isUpdateKey = "" + intent.getStringExtra("key");
        String editPostID = "" + intent.getStringExtra("editPostID");
        //validate if we came here to update post i.e came from AdapterPost
        if (isUpdateKey.equals("editPost")) {
            //update
            //tbAdd.setTitle("Update Post");
            //btnUpload.setText("Update");
            loadPostData(editPostID);
        } else {

            //tbAdd.setTitle("Rate Me");
            //tnUpload.setText("Upload");
            //add
        }

        firebaseAuth = FirebaseAuth.getInstance();

        //init views
        etTitle = findViewById(R.id.aar_et_title);
        etDescription = findViewById(R.id.aar_et_description);
        ivPost = findViewById(R.id.aar_iv_post);
        btnUpload = findViewById(R.id.aar_btn_upload);

        tbAdd = findViewById(R.id.aar_tb_add_rate);

        tbAdd.setTitle("Rate Me");
        if (tbAdd != null) {
            setSupportActionBar(tbAdd);
            tbAdd.setSubtitle(email);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        //get image from camera gallerry on click
        ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image pick dialog
                showImageDialog();
            }
        });

        //upload butto click listener
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data (title, description, from ET
                String title = etTitle.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(AddRate.this, "Enter title", Toast.LENGTH_SHORT);
                    return;
                }

                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(AddRate.this, "Enter description", Toast.LENGTH_SHORT);
                    return;
                }

                if (isUpdateKey.equals("editPost")) {
                    beginUpdate(title, description, editPostID);
                } else {
//                    ratingNumber = ratingBar.getRating();
                    uploadData(title, description);
                }

//                if (image_uri == null) {
//                    //post without image
//                    uploadData(title, description, "noImage");
//                } else {
//                    //post with image
//                    uploadData(title, description, String.valueOf(image_uri));
//                }

            }
        });

        checkUserStatus();

    }

//    private void rating() {
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
//        String saveCurrentDate = currentDate.format(calendar.getTime());
//
//        ratingsTbl.child(lUID).child("Users").child(firebaseUser.getUid()).setValue(accountData)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            //startActivity(new Intent(RateUs.this, AllLeisureDetails.class));
//                            Toast.makeText(RateUs.this, "Successfully Added", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

//    private void ratingCalculate() {
//
//        try {//
////            final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
////            final DatabaseReference dbRef = db.child("Ratings").child(name).child(firebaseUser.getUid());
//            ratingsTbl.child(uid).child("Users").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    double total = 0.0;
//                    double count = 0.0;
//                    double average = 0.0;
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
////                        num = (int) ds.getChildrenCount();
//                        double rating = Double.parseDouble(String.valueOf(ds.child("rateValue").getValue()));
//                        total = total + rating;
//                        count = count + 1;
//                        average = total / count;
////                     total = rates * 2;
//                    }
//                    final DatabaseReference newRef = ratingsTbl.child(name).child("AverageRating");
//                    newRef.child("Accumulated").setValue(average);
//                    //                        total = total + rates;
//////                        count = count + 1;
////                        average = total / count;
//
//                    //  sample.setText("" + rates);
//                    //}
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    throw databaseError.toException();
//                }
//            });
//        } catch (Exception e) {
//            Toast.makeText(AddRate.this, "" + e, Toast.LENGTH_SHORT).show();
//        }
//
//    }

    private void beginUpdate(String title, String description, String editPostID) {
        progressDialog.setMessage("Updating Posts");
        progressDialog.show();

        if (!editImage.equals("noImage")) {
            //without image
            updateWasWithImage(title, description, editPostID);
        } else if (ivPost.getDrawable() != null) {
            updateWithNowImage(title, description, editPostID);
        } else {
            //without image
            updateWithOutImage(title, description, editPostID);
        }

    }

    private void updateWithOutImage(String title, String description, String editPostID) {

        HashMap<String, Object> hashMap = new HashMap<>();
        //put post info
        hashMap.put("uid", uid);
        hashMap.put("uName", name);
        hashMap.put("uEmail", email);
        hashMap.put("uDp", dp);
        hashMap.put("pTitle", title);
        hashMap.put("pDescription", description);
        hashMap.put("pImage", "noImage");


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.child(editPostID)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(AddRate.this, "Updated...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddRate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void updateWithNowImage(final String title, final String description, final String editPostID) {

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post" + timeStamp;

        //get image from image view
        Bitmap bitmap = ((BitmapDrawable) ivPost.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //image compress
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
        ref.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image uploaded get its url
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        String downloadUri = uriTask.getResult().toString();
                        if (uriTask.isSuccessful()) {
                            //url is received, upload to firebase database
                            HashMap<String, Object> hashMap = new HashMap<>();
                            //put post info
                            hashMap.put("uid", uid);
                            hashMap.put("uName", name);
                            hashMap.put("uEmail", email);
                            hashMap.put("uDp", dp);
                            hashMap.put("pTitle", title);
                            hashMap.put("pDescription", description);
                            hashMap.put("pImage", downloadUri);


                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                            ref.child(editPostID)
                                    .updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddRate.this, "Updated...", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddRate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(AddRate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void updateWasWithImage(String title, String description, String editPostID) {
        //post is with image, delete previous image first
        StorageReference mPictureRef = FirebaseStorage.getInstance().getReference(editImage);
        mPictureRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //image deleted, upload new image
                        //for post-image name, post id publish time
                        String timeStamp = String.valueOf(System.currentTimeMillis());
                        String filePathAndName = "Posts/" + "post" + timeStamp;

                        //get image from image view
                        Bitmap bitmap = ((BitmapDrawable) ivPost.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        //image compress
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                        ref.putBytes(data)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //image uploaded get its url
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isSuccessful()) ;
                                        String downloadUri = uriTask.getResult().toString();
                                        if (uriTask.isSuccessful()) {
                                            //url is received, upload to firebase database
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            //put post info
                                            hashMap.put("uid", uid);
                                            hashMap.put("uName", name);
                                            hashMap.put("uEmail", email);
                                            hashMap.put("uDp", dp);
                                            hashMap.put("pTitle", title);
                                            hashMap.put("pDescription", description);
                                            hashMap.put("pImage", downloadUri);


                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                            ref.child(editPostID)
                                                    .updateChildren(hashMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(AddRate.this, "Updated...", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(AddRate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    });

                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //image not uploaded
                                        progressDialog.dismiss();
                                        Toast.makeText(AddRate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddRate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void loadPostData(String editPostID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        //get detail of post using id of post
        Query query = reference.orderByChild("pID").equalTo(editPostID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    editTitle = "" + ds.child("pTitle").getValue();
                    editDescription = "" + ds.child("pDescription").getValue();
                    editImage = "" + ds.child("pImage").getValue();

                    //set data to views
                    //etTitle.setText(editTitle);
                    etDescription.setText(editDescription);

                    //set image
                    if (!editImage.equals("noImage")) {
                        try {
                            Picasso.get().load(editImage).into(ivPost);
                        } catch (Exception e) {

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uploadData(final String title, final String description) {
        progressDialog.setMessage("Publishing post...");
        progressDialog.show();

        //for post image name, post-id, post-publish-time
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post" + timestamp;

        if (ivPost.getDrawable() != null) {
            //get some info of current user to include in post
            userDbRef = FirebaseDatabase.getInstance().getReference("Users");
            Query query = userDbRef.orderByChild("email").equalTo(email);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        name = "" + ds.child("name").getValue();
                        email = "" + ds.child("email").getValue();
                        dp = "" + ds.child("display_photo").getValue();

                        //get image from image view
                        Bitmap bitmap = ((BitmapDrawable) ivPost.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        //image compress
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        //post with image
                        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                        ref.putBytes(data)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //image is uploaded to firebase storage, now get it's uri
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isSuccessful()) ;
                                        String downloadUri = uriTask.getResult().toString();
                                        if (uriTask.isSuccessful()) {
                                            //url is received upload post to firebase database
                                            HashMap<Object, String> hashMap = new HashMap<>();
                                            //put post info
                                            hashMap.put("uid", uid);
                                            hashMap.put("uName", name);//
                                            hashMap.put("uEmail", email);
                                            hashMap.put("uDp", dp);//
                                            hashMap.put("pID", timestamp);
                                            hashMap.put("pTitle", title);
                                            hashMap.put("pDescription", description);
                                            hashMap.put("pTime", timestamp);
                                            hashMap.put("pImage", downloadUri);
                                            hashMap.put("pLikes", "0");

                                            //path to store post data
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                            //put data in this ref
                                            ref.child(timestamp).setValue(hashMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            //added in database
                                                            progressDialog.dismiss();
                                                            Toast.makeText(AddRate.this, "Post Published", Toast.LENGTH_SHORT).show();

                                                            //reset views
                                                            etTitle.setText("");
                                                            etDescription.setText("");
                                                            ivPost.setImageURI(null);
                                                            image_uri = null;
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            //failed adding post in database
                                                            progressDialog.dismiss();
                                                            Toast.makeText(AddRate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    });


                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //failed uploading image
                                        progressDialog.dismiss();
                                        Toast.makeText(AddRate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });



                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });


        } else {
            //get some info of current user to include in post
            userDbRef = FirebaseDatabase.getInstance().getReference("Users");
            Query query = userDbRef.orderByChild("email").equalTo(email);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        name = "" + ds.child("name").getValue();
                        email = "" + ds.child("email").getValue();
                        dp = "" + ds.child("display_photo").getValue();

                        //post without image
                        HashMap<Object, String> hashMap = new HashMap<>();
                        //put post info
                        hashMap.put("uid", uid);
                        hashMap.put("uName", name);
                        hashMap.put("uEmail", email);
                        hashMap.put("uDp", dp);
                        hashMap.put("pID", timestamp);
                        hashMap.put("pTitle", title);
                        hashMap.put("pLikes","0");
                        hashMap.put("pDescription", description);
                        hashMap.put("pImage", "noImage");
                        hashMap.put("pTime", timestamp);

                        //path to store post data
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                        //put data in this ref
                        ref.child(timestamp).setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //added in database
                                        progressDialog.dismiss();
                                        Toast.makeText(AddRate.this, "Post Published", Toast.LENGTH_SHORT).show();
                                        //reset views
                                        //etTitle.setText("");
                                        etDescription.setText("");
                                        ivPost.setImageURI(null);
                                        image_uri = null;
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //failed adding post in database
                                        progressDialog.dismiss();
                                        Toast.makeText(AddRate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void showImageDialog() {
        //options {camera, gallery} to show dialog
        String[] options = {"Camera", "Gallery"};

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");

        //set option to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //item click handle
                if (which == 0) {
                    //camera clicked
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                }
                if (which == 1) {
                    //gallery click
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }

                }
            }
        });
        //create and show dialog
        builder.create().show();

    }

    private void pickFromGallery() {
        //intent to pick from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        //intent to pick image from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not
        // return true if enabled
        // return false is not enabled
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //check if camera permission is enabled or not
        // return true if enabled
        // return false is not enabled
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }


    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {

            email = user.getEmail();
            uid = user.getUid();
            //user is signed in stay here
            //set email of logged in user
            //myprofile set to user.getEmail
            //save uid of currently signed in user in shared preference
            //myUID = user.getUid(); // currently signed in user's uid
        } else {
            //user not signed, goto main activity
            startActivity(new Intent(AddRate.this, Login.class));
            finish();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // goto previous activity
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        menu.findItem(R.id.search_all_mm).setVisible(false);
//        menu.findItem(R.id.add_post_mm).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    //handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //this method is called when user press allow or deny from permission request dialog
        //here we will handle permission cases (allowed and denied)
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        //both permission are granted
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera & Storage permission are necessarry", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        //storage permisson  granted
                        pickFromGallery();
                    } else {
                        //camera or gallery or both permission were denied
                        Toast.makeText(this, "Storage permission are neccessary....", Toast.LENGTH_SHORT);

                    }

                } else {

                }
            }
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this method will be called after picking image from camera or gallery
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is picked from gallery, get uri of image
                image_uri = data.getData();

                //set to image view
                ivPost.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri of image
                ivPost.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
