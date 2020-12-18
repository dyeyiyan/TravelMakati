package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
import com.thatchosenone.travelmakati.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class AddPhotos extends AppCompatActivity {

    Toolbar tbAddPhotos;
    TextInputLayout tilCaption;
    ImageView ivAddPhoto;
    Button btnPost;

    String lUID, lNAME;
    String caption, name, dp;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference leisurePhotosRef, userDbRef;
    FirebaseUser firebaseUser;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);

        tbAddPhotos = findViewById(R.id.tb_add_photos);
        tilCaption = findViewById(R.id.aap_til_caption);
        ivAddPhoto = findViewById(R.id.aap_iv_pic);
        btnPost = findViewById(R.id.aap_btn_post);

        Intent intent = getIntent();
        lUID = intent.getStringExtra("luid");
        lNAME = intent.getStringExtra("lname");

        Bundle bd = getIntent().getExtras();
        Uri uri = bd.getParcelable("uri_photos");

        tbAddPhotos.setTitle(lNAME);
        tbAddPhotos.setSubtitle("Add Photo");
        setSupportActionBar(tbAddPhotos);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Log.e("URI", uri.toString());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            ivAddPhoto.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        leisurePhotosRef = firebaseDatabase.getReference("LeisurePhotos");


        progressDialog = new ProgressDialog(this);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caption = tilCaption.getEditText().getText().toString().trim();
                //rating = mrbRate.getRating();
                if (!validateCaption()) {
                    progressDialog.dismiss();
                    return;
                } else {
                    storeLeisurePhotos(caption);
                }

            }
        });


    }

    private void storeLeisurePhotos(String caption) {
        progressDialog.setMessage("Publishing post...");
        progressDialog.show();

        //for post image name, post-id, post-publish-time
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Leisure/" + "photos" + timestamp;

        //get some info of current user to include in post
        userDbRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userDbRef.orderByChild("uid").equalTo(firebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    name = "" + ds.child("name").getValue();

                    //get image from image view
                    Bitmap bitmap = ((BitmapDrawable) ivAddPhoto.getDrawable()).getBitmap();
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
                                        HashMap<Object, String> liesurePhotosMap = new HashMap<>();
                                        //put post info
                                        liesurePhotosMap.put("name", name);
                                        liesurePhotosMap.put("caption", caption);
                                        liesurePhotosMap.put("leisure_photo", downloadUri);
                                        liesurePhotosMap.put("date_time", timestamp);
                                        liesurePhotosMap.put("luid", lUID);
                                        liesurePhotosMap.put("user_uid", firebaseUser.getUid());
                                        //path to store post data
                                        //put data in this ref
                                        leisurePhotosRef.child(timestamp).setValue(liesurePhotosMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //added in database
                                                        progressDialog.dismiss();
                                                        //reset views
                                                        Toast.makeText(AddPhotos.this, "Post Published", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //failed adding post in database
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AddPhotos.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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
                                    Toast.makeText(AddPhotos.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    // end

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

    }

    //Validate caption
    private boolean validateCaption() {
        String expi = tilCaption.getEditText().getText().toString().trim();

        if (expi.isEmpty()) {
            tilCaption.setError("Error: Field can't be empty");
            return false;
        } else {
            tilCaption.setError(null);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
