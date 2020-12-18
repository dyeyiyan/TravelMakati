package com.thatchosenone.travelmakati.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thatchosenone.travelmakati.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LeisureAdvertisement extends AppCompatActivity {


    Button btnAddPhoto, btnSubmit;
    TextView tvAlert;

    Toolbar toolbar;
    Spinner sCategories;
    String categoryItem;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference leisureRef;


//    Spinner sCategories, sTimeToM, sTimeFromM, sTimeToT, sTimeFromT,
//            sTimeToW, sTimeFromW, sTimeToTH, sTimeFromTH, sTimeToF, sTimeFromF,
//            sTimeToSA, sTimeFromSA, sTimeToSU, sTimeFromSU, sBarangay;
//    String categoryItem, timeFromItemM, timeToItemM,
//            timeFromItemT, timeToItemT, timeFromItemW, timeToItemW,
//            timeFromItemTH, timeToItemTH, timeFromItemF, timeToItemF,
//            timeFromItemSA, timeToItemSA, timeFromItemSU, timeToItemSU,
//            barangayItem;

    //CheckBox checkBoxM, checkBoxT, checkBoxW, checkBoxTH, checkBoxF, checkBoxSA, checkBoxSU;

    TextInputLayout tilName, tilPrice, tilStreet, tilBarangay, tilCity, tilDesc, tilHours, tilContact;
    ImageView ivPhoto;

    //    AutoCompleteTextView actvCategory, actvBarangay, actvCity;
//
//    private static final String [] BARANGAY = new String[]{
//            "Bangkal", "Bel-Air", "Carmona", "Cembo", "Comembo", "East Rembo", "Forbes Park",
//            "Guadalupe Nuevo", "Guadalupe Viejo", "Kasilawan", "La Paz", "Maggalanes", "Olympia",
//            "Palanan","Pembo", "Pinagkaisahan", "Pio del Pilar", "Pitogo", "Poblacion", "Post Proper North",
//            "Rizal", "San Isidro", "Singkamas", "South Cembo", "Sta. Cruz", "Tejeros", "Urdaneta",
//            "Valenzuela", "Valenzuela", "West Rembo"
//    };
//
//    private static final String [] CATEGORY = new String[]{
//            "Parks", "Museums", "Art Galleries", "Cafe / Tea Shop",
//            "Restaurant", "Nightlife", "Malls", "Hotels"
//    };
//
//    private static final String [] CITY = new String[]{
//            "Makati City, Philippine"
//    };
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
    ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private Uri ImageUri;
    private ProgressDialog progressDialog;
    private int uploadCount = 0;

    String name, street, categories, barangays, citys, price, hours, desc, contacts;
    String comAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leisure_advertisement);

        //Init views
        tilName = (TextInputLayout) findViewById(R.id.ala_til_name);
        tilPrice = (TextInputLayout) findViewById(R.id.ala_til_price);
        tilStreet = (TextInputLayout) findViewById(R.id.ala_til_street);
        tilHours = (TextInputLayout) findViewById(R.id.ala_til_hours);
        tilDesc = (TextInputLayout) findViewById(R.id.ala_til_desc);
        tilContact = (TextInputLayout) findViewById(R.id.ala_til_phone);

        //tilCity = (TextInputLayout) findViewById(R.id.ala_til_city);
        //tilBarangay = (TextInputLayout) findViewById(R.id.ala_til_barangay);
        ivPhoto = findViewById(R.id.ala_iv_leisure);
        sCategories = (Spinner) findViewById(R.id.ala_s_category);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Path of table of users


//        sBarangay = (Spinner) findViewById(R.id.ala_s_barangay);
//        sTimeFromM = (Spinner) findViewById(R.id.ala_s_time_fromM);
//        sTimeToM = (Spinner) findViewById(R.id.ala_s_time_toM);
//
//        sTimeFromT = (Spinner) findViewById(R.id.ala_s_time_fromT);
//        sTimeToT = (Spinner) findViewById(R.id.ala_s_time_toT);
//
//        sTimeFromW = (Spinner) findViewById(R.id.ala_s_time_fromW);
//        sTimeToW = (Spinner) findViewById(R.id.ala_s_time_toW);
//
//        sTimeFromTH = (Spinner) findViewById(R.id.ala_s_time_fromTH);
//        sTimeToTH = (Spinner) findViewById(R.id.ala_s_time_toTH);
//
//        sTimeFromF = (Spinner) findViewById(R.id.ala_s_time_fromF);
//        sTimeToF = (Spinner) findViewById(R.id.ala_s_time_toF);
//
//        sTimeFromSA = (Spinner) findViewById(R.id.ala_s_time_fromSA);
//        sTimeToSA = (Spinner) findViewById(R.id.ala_s_time_toSA);
//
//        sTimeFromSU = (Spinner) findViewById(R.id.ala_s_time_fromSU);
//        sTimeToSU = (Spinner) findViewById(R.id.ala_s_time_toSU);
//
//        checkBoxM = (CheckBox) findViewById(R.id.ala_cb_monday);
//        checkBoxT = (CheckBox) findViewById(R.id.ala_cb_tuesday);
//        checkBoxW = (CheckBox) findViewById(R.id.ala_cb_wednesday);
//        checkBoxTH = (CheckBox) findViewById(R.id.ala_cb_thursday);
//        checkBoxF = (CheckBox) findViewById(R.id.ala_cb_friday);
//        checkBoxSA = (CheckBox) findViewById(R.id.ala_cb_sat);
//        checkBoxSU = (CheckBox) findViewById(R.id.ala_cb_sun);

        //sRole = (Spinner) findViewById(R.id.spinner_role);
        toolbar = findViewById(R.id.tb_leisure);
        tvAlert = findViewById(R.id.ala_tv_alert);

        btnAddPhoto = findViewById(R.id.ala_btn_add_photo);
        btnSubmit = findViewById(R.id.ala_btn_submit);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

//        sTimeFromM.setEnabled(false);
//        sTimeToM.setEnabled(false);
//
//        sTimeFromT.setEnabled(false);
//        sTimeToT.setEnabled(false);
//
//        sTimeFromW.setEnabled(false);
//        sTimeToW.setEnabled(false);
//
//        sTimeFromTH.setEnabled(false);
//        sTimeToTH.setEnabled(false);
//
//        sTimeFromF.setEnabled(false);
//        sTimeToF.setEnabled(false);
//
//        sTimeFromSA.setEnabled(false);
//        sTimeToSA.setEnabled(false);
//
//        sTimeFromSU.setEnabled(false);
//        sTimeToSU.setEnabled(false);

        toolbar.setTitle("Add Leisure");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        checkBoxM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (checkBoxM.isChecked()) {
//                    sTimeFromM.setEnabled(true);
//                    sTimeToM.setEnabled(true);
//                    m = "Monday";
//                } else {
//                    sTimeFromM.setEnabled(false);
//                    sTimeFromM.setSelection(0);
//                    sTimeToM.setEnabled(false);
//                    sTimeToM.setSelection(0);
//                }
//            }
//        });
//
//        checkBoxT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (checkBoxT.isChecked()) {
//                    sTimeFromT.setEnabled(true);
//                    sTimeToT.setEnabled(true);
//                    t = "Tuesday";
//                } else {
//                    sTimeFromT.setEnabled(false);
//                    sTimeFromT.setSelection(0);
//                    sTimeToT.setEnabled(false);
//                    sTimeToT.setSelection(0);
//                }
//            }
//        });
//
//        checkBoxW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (checkBoxW.isChecked()) {
//                    sTimeFromW.setEnabled(true);
//                    sTimeToW.setEnabled(true);
//                    w = "Wednesday";
//                } else {
//                    sTimeFromW.setEnabled(false);
//                    sTimeFromW.setSelection(0);
//                    sTimeToW.setEnabled(false);
//                    sTimeToW.setSelection(0);
//                }
//            }
//        });
//
//        checkBoxTH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (checkBoxTH.isChecked()) {
//                    sTimeFromTH.setEnabled(true);
//                    sTimeToTH.setEnabled(true);
//                    th = "Thursday";
//                } else {
//                    sTimeFromTH.setEnabled(false);
//                    sTimeFromTH.setSelection(0);
//                    sTimeToTH.setEnabled(false);
//                    sTimeToTH.setSelection(0);
//                }
//            }
//        });
//
//        checkBoxF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (checkBoxF.isChecked()) {
//                    sTimeFromF.setEnabled(true);
//                    sTimeToF.setEnabled(true);
//                    f = "Friday";
//                } else {
//                    sTimeFromF.setEnabled(false);
//                    sTimeFromF.setSelection(0);
//                    sTimeToF.setEnabled(false);
//                    sTimeToF.setSelection(0);
//                }
//            }
//        });
//
//        checkBoxSA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (checkBoxSA.isChecked()) {
//                    sTimeFromSA.setEnabled(true);
//                    sTimeToSA.setEnabled(true);
//                    sa = "Saturday";
//                } else {
//                    sTimeFromSA.setEnabled(false);
//                    sTimeFromSA.setSelection(0);
//                    sTimeToSA.setEnabled(false);
//                    sTimeToSA.setSelection(0);
//                }
//            }
//        });
//
//        checkBoxSU.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (checkBoxSU.isChecked()) {
//                    sTimeFromSU.setEnabled(true);
//                    sTimeToSU.setEnabled(true);
//                    su = "Sunday";
//                } else {
//                    sTimeFromSU.setEnabled(false);
//                    sTimeFromSU.setSelection(0);
//                    sTimeToSU.setEnabled(false);
//                    sTimeToSU.setSelection(0);
//                }
//            }
//        });

        //init permission arrays
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image pick dialog
                showImageDialog();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                name = tilName.getEditText().getText().toString().trim();
                street = tilStreet.getEditText().getText().toString().trim();
                price = tilPrice.getEditText().getText().toString().trim();
                hours = tilHours.getEditText().getText().toString().trim();
                desc = tilDesc.getEditText().getText().toString().trim();
                contacts = tilContact.getEditText().getText().toString().trim();

                comAddress = street;

                if (!validateName() | !validateStreet() | !validatePrice() | !validateImage()
                        | !validateHours() | !validateCont() | !validateDesc()) {
                    progressDialog.dismiss();
                    //        materialButtonSignUp.setVisibility(View.INVISIBLE);
                    //          progressBar.setVisibility(View.INVISIBLE);
                    return;
                } else {
                    try {
                        storeLeisureInformation();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        categories();
    }

    private void storeLeisureInformation() throws IOException {

        leisureRef = firebaseDatabase.getReference("Leisure");
        String luid = leisureRef.push().getKey();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        addresses = geocoder.getFromLocationName(comAddress, 1);
        Address address = addresses.get(0);
        String longitude = Location.convert(address.getLongitude(), Location.FORMAT_DEGREES);
         String latitude = Location.convert(address.getLatitude(), Location.FORMAT_DEGREES);


        //for post image name, post-id, post-publish-time
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Leisure/" + "photos" + timestamp;

        //get image from image view
        Bitmap bitmap = ((BitmapDrawable) ivPhoto.getDrawable()).getBitmap();
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
                            HashMap<String, Object> leisureMap = new HashMap<>();
                            //put post info
                            leisureMap.put("name", name);
                            leisureMap.put("category", categoryItem);
                            leisureMap.put("photo", downloadUri);
                            leisureMap.put("street", street);
                            leisureMap.put("barangay", barangays);
                            leisureMap.put("latitude", latitude);
                            leisureMap.put("longitude", longitude);
                            leisureMap.put("desc", desc);
                            leisureMap.put("hours", hours);
                            leisureMap.put("contacts", contacts);
                            leisureMap.put("price", price);
                            //leisureMap.put("barangay", barangayItem);
                            //leisureMap.put("website", webAddress);
                            leisureMap.put("city", citys);
                            leisureMap.put("status", "waiting");
                            leisureMap.put("date_time", timestamp);
                            leisureMap.put("luid", luid);
                            leisureMap.put("total_rate", "0");
                            leisureMap.put("total_view", "0");
                            leisureMap.put("user_uid", firebaseUser.getUid());
                            //path to store post data
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Leisure");
                            //put data in this ref
                            ref.child(luid).setValue(leisureMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //added in database
                                            progressDialog.dismiss();
                                            Toast.makeText(LeisureAdvertisement.this, "Please wait to approved your business", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //failed adding post in database
                                            progressDialog.dismiss();
                                            Toast.makeText(LeisureAdvertisement.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(LeisureAdvertisement.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //        StorageReference leisurePhotos = FirebaseStorage.getInstance().getReference().child("Leisure_Photos");
//        for (uploadCount = 0; uploadCount < ImageList.size(); uploadCount++) {
//
//            Uri individualImage = ImageList.get(uploadCount);
//            final StorageReference imageName = leisurePhotos.child("Image" + individualImage.getLastPathSegment());
//
//            imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String url = String.valueOf(uri);
//                            StoreLink(url);
//                        }
//                    });
//                }
//            });
//
//        }
    }

    private void StoreLink(String url) {


//        DatabaseReference dayTimeRef = FirebaseDatabase.getInstance().getReference("Time_Days");
//        DatabaseReference leisurePhotosRef = FirebaseDatabase.getInstance().getReference().child("Leisure_Photos");
//
//        DatabaseReference leisureRef = FirebaseDatabase.getInstance().getReference("Leisures");
//        String status = "pending";
//        String city = "Makati";
//        HashMap<String, Object> leisureMap = new HashMap<>();
//        leisureMap.put("category", categoryItem);
//        //leisureMap.put("Role", roleItem);
//        leisureMap.put("name", name);
//        leisureMap.put("photo", url);
//        leisureMap.put("street", street);
//        leisureMap.put("description", desc);
//        leisureMap.put("barangay", barangayItem);
//        leisureMap.put("contact", contact);
//        leisureMap.put("website", webAddress);
//        leisureMap.put("city", city);
//        leisureMap.put("status", status);
//        leisureRef.push().updateChildren(leisureMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
////
////                            HashMap<String, Object> days_time = new HashMap<>();
////                            days_time.put("day_one", m);
////                            days_time.put("day_two", t);
////                            days_time.put("day_three", w);
////                            days_time.put("day_four", th);
////                            days_time.put("day_five", f);
////                            days_time.put("day_six", sa);
////                            days_time.put("day_seven", su);
////                            days_time.put("uid",name);
////                            dayTimeRef.child(name).push().setValue(days_time);
////                            Intent intent = new Intent(LeisureAdvertisement.this, ActivityMain.class);
////                            startActivity(intent);
//                            progressDialog.dismiss();
////                            startActivity(new Intent(LeisureAdvertisement.this, ActivityMain.class));
////                            finish();
//                            Toast.makeText(LeisureAdvertisement.this, "Leisure added successfully", Toast.LENGTH_SHORT).show();
//                        } else {
//                            progressDialog.dismiss();
//                            String message = task.getException().toString();
//                            Toast.makeText(LeisureAdvertisement.this, "Error:" + message, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//        //tvAlert.setText("Image Uploaded Successfully");
//
////
////        HashMap<String, Object> photoUrl = new HashMap<>();
////        photoUrl.put("photo", url);
////        photoUrl.put("uid",name);
////        leisurePhotosRef.child(name).push().setValue(photoUrl);

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
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

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

    //Validate Name
    private boolean validateName() {
        String nameLeisure = tilName.getEditText().getText().toString().trim();

        if (nameLeisure.isEmpty()) {
            tilName.setError("Error: Field can't be empty");
            return false;
        } else {
            tilName.setError(null);
            return true;
        }
    }

    //Validate Street
    private boolean validateStreet() {
        String streetLeisure = tilStreet.getEditText().getText().toString().trim();

        if (streetLeisure.isEmpty()) {
            tilStreet.setError("Error: Field can't be empty");
            return false;
        } else {
            tilStreet.setError(null);
            return true;
        }
    }

    //validate city
    private boolean validateHours() {
        String hours = tilHours.getEditText().getText().toString().trim();

        if (hours.isEmpty()) {
            tilHours.setError("Error: Field can't be empty");
            return false;
        } else {
            tilHours.setError(null);
            return true;
        }
    }

    //validate city
    private boolean validateDesc() {
        String desc = tilDesc.getEditText().getText().toString().trim();

        if (desc.isEmpty()) {
            tilDesc.setError("Error: Field can't be empty");
            return false;
        } else {
            tilDesc.setError(null);
            return true;
        }
    }

    //validate city
    private boolean validateCont() {
        String cont = tilContact.getEditText().getText().toString().trim();

        if (cont.isEmpty()) {
            tilContact.setError("Error: Field can't be empty");
            return false;
        } else {
            tilContact.setError(null);
            return true;
        }
    }


    //validate price
    private boolean validatePrice() {
        String priceLeisure = tilPrice.getEditText().getText().toString().trim();
        if (priceLeisure.isEmpty()) {
            tilPrice.setError("Error: Field can't be empty");
            return false;
        } else {
            tilPrice.setError(null);
            return true;
        }
    }

    private boolean validateBarangay() {
        String bgyLeisure = tilBarangay.getEditText().getText().toString().trim();
        if (bgyLeisure.isEmpty()) {
            tilBarangay.setError("Error: Field can't be empty");
            return false;
        } else {
            tilBarangay.setError(null);
            return true;
        }
    }

    private boolean validateImage() {
        if (image_uri == null) {
            Toast.makeText(LeisureAdvertisement.this, "Please insert an image", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //    private void barangayList() {
//
//        List<String> list = new ArrayList<>();
//        list.add(0, "Barangay");
//        list.add("Bangkal");
//        list.add("Bel-Air");
//        list.add("Carmona");
//        list.add("Cembo");
//        list.add("Comembo");
//        list.add("East Rembo");
//        list.add("Forbes Park");
//        list.add("Guadalupe Nuevo");
//        list.add("Guadalupe Viejo");
//        list.add("Kasilawan");
//        list.add("La Paz");
//        list.add("Maggalanes");
//        list.add("Olympia");
//        list.add("Palanan");
//        list.add("Pembo");
//        list.add("Pinagkaisahan");
//        list.add("Pio del Pilar");
//        list.add("Pitogo");
//        list.add("Poblacion");
//        list.add("Post Proper North");
//        list.add("Rizal");
//        list.add("San Isidro");
//        list.add("San Lorenzo");
//        list.add("Singkamas");
//        list.add("South Cembo");
//        list.add("Sta. Cruz");
//        list.add("Tejeros");
//        list.add("Urdaneta");
//        list.add("Valenzuela");
//        list.add("West Rembo");
//
//        //Style and populate the spinner
//        ArrayAdapter<String> arrayAdapter;
//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
//
//        //Dropdown layout stylea
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        //Attaching array adapter to spinner
//        sBarangay.setAdapter(arrayAdapter);
//
//        sBarangay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Barangay")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//                    barangayItem = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + barangayItem, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//
//    }
//
//    private void timeFrom() {
//
//        List<String> list = new ArrayList<>();
//        list.add(0, "Start");
//        list.add("00:00");
//        list.add("00:15");
//        list.add("00:30");
//        list.add("00:45");
//
//        list.add("01:00");
//        list.add("01:15");
//        list.add("01:30");
//        list.add("01:45");
//
//        list.add("02:00");
//        list.add("02:15");
//        list.add("02:30");
//        list.add("02:45");
//
//        list.add("02:00");
//        list.add("02:15");
//        list.add("02:30");
//        list.add("02:45");
//
//        list.add("02:00");
//        list.add("02:15");
//        list.add("02:30");
//        list.add("02:45");
//
//        list.add("03:00");
//        list.add("03:15");
//        list.add("03:30");
//        list.add("03:45");
//
//        list.add("04:00");
//        list.add("04:15");
//        list.add("04:30");
//        list.add("04:45");
//
//        list.add("05:00");
//        list.add("05:15");
//        list.add("05:30");
//        list.add("05:45");
//
//        list.add("06:00");
//        list.add("06:15");
//        list.add("06:30");
//        list.add("06:45");
//
//        list.add("07:00");
//        list.add("07:15");
//        list.add("07:30");
//        list.add("07:45");
//
//        list.add("08:00");
//        list.add("08:15");
//        list.add("08:30");
//        list.add("08:45");
//
//        list.add("09:00");
//        list.add("09:15");
//        list.add("09:30");
//        list.add("09:45");
//
//        list.add("10:00");
//        list.add("10:15");
//        list.add("10:30");
//        list.add("10:45");
//
//        list.add("11:00");
//        list.add("11:15");
//        list.add("11:30");
//        list.add("11:45");
//
//        //Style and populate the spinner
//        ArrayAdapter<String> arrayAdapter;
//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
//
//        //Dropdown layout stylea
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        //Attaching array adapter to spinner
//        sTimeFromM.setAdapter(arrayAdapter);
//        sTimeFromM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Start")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//                    timeFromItemM = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeFromItemM, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        //Attaching array adapter to spinner
//        sTimeFromT.setAdapter(arrayAdapter);
//        sTimeFromT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Start")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//                    timeFromItemT = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeFromItemT, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        //Attaching array adapter to spinner
//        sTimeFromW.setAdapter(arrayAdapter);
//        sTimeFromW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Start")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//                    timeFromItemW = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeFromItemW, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        //Attaching array adapter to spinner
//        sTimeFromTH.setAdapter(arrayAdapter);
//        sTimeFromTH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Start")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//                    timeFromItemTH = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeFromItemTH, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        //Attaching array adapter to spinner
//        sTimeFromF.setAdapter(arrayAdapter);
//        sTimeFromF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Start")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//                    timeFromItemF = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeFromItemF, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        //Attaching array adapter to spinner
//        sTimeFromSA.setAdapter(arrayAdapter);
//        sTimeFromSA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Start")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//                    timeFromItemSA = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeFromItemSA, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        //Attaching array adapter to spinner
//        sTimeFromSU.setAdapter(arrayAdapter);
//        sTimeFromSU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Start")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//                    timeFromItemSU = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeFromItemSU, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//
//    }
//
//    private void timeTo() {
//
//        List<String> list = new ArrayList<>();
//        list.add(0, "End");
//        list.add("00:00");
//        list.add("00:15");
//        list.add("00:30");
//        list.add("00:45");
//
//        list.add("01:00");
//        list.add("01:15");
//        list.add("01:30");
//        list.add("01:45");
//
//        list.add("02:00");
//        list.add("02:15");
//        list.add("02:30");
//        list.add("02:45");
//
//        list.add("02:00");
//        list.add("02:15");
//        list.add("02:30");
//        list.add("02:45");
//
//        list.add("02:00");
//        list.add("02:15");
//        list.add("02:30");
//        list.add("02:45");
//
//        list.add("03:00");
//        list.add("03:15");
//        list.add("03:30");
//        list.add("03:45");
//
//        list.add("04:00");
//        list.add("04:15");
//        list.add("04:30");
//        list.add("04:45");
//
//        list.add("05:00");
//        list.add("05:15");
//        list.add("05:30");
//        list.add("05:45");
//
//        list.add("06:00");
//        list.add("06:15");
//        list.add("06:30");
//        list.add("06:45");
//
//        list.add("07:00");
//        list.add("07:15");
//        list.add("07:30");
//        list.add("07:45");
//
//        list.add("08:00");
//        list.add("08:15");
//        list.add("08:30");
//        list.add("08:45");
//
//        list.add("09:00");
//        list.add("09:15");
//        list.add("09:30");
//        list.add("09:45");
//
//        list.add("10:00");
//        list.add("10:15");
//        list.add("10:30");
//        list.add("10:45");
//
//        list.add("11:00");
//        list.add("11:15");
//        list.add("11:30");
//        list.add("11:45");
//
//        //Style and populate the spinner
//        ArrayAdapter<String> arrayAdapter;
//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
//
//        //Dropdown layout stylea
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        //Attaching array adapter to spinner
//        sTimeToM.setAdapter(arrayAdapter);
//        sTimeToM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("End")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//
//                    timeToItemM = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeToItemM, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        sTimeToT.setAdapter(arrayAdapter);
//        sTimeToT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("End")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//
//                    timeToItemM = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeToItemT, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        sTimeToW.setAdapter(arrayAdapter);
//        sTimeToW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("End")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//
//                    timeToItemW = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeToItemW, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        sTimeToTH.setAdapter(arrayAdapter);
//        sTimeToTH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("End")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//
//                    timeToItemTH = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeToItemTH, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        sTimeToF.setAdapter(arrayAdapter);
//        sTimeToF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("End")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//
//                    timeToItemF = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeToItemF, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        sTimeToSA.setAdapter(arrayAdapter);
//        sTimeToSA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("End")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//
//                    timeToItemM = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeToItemSA, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        sTimeToSU.setAdapter(arrayAdapter);
//        sTimeToSU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("End")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//
//                    timeToItemSU = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    Toast.makeText(parent.getContext(), "Selected: " + timeToItemSU, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//    }
//
    private void categories() {
        List<String> categories = new ArrayList<>();
        categories.add(0, "Choose Category");
        categories.add("Parks");
        categories.add("Malls");
        categories.add("Museum");
        categories.add("Hotels");
        categories.add("Nightlife");
        categories.add("Restaurant");
        categories.add("Art Galleries");
        categories.add("Cafe / Tea Shop");
        //Style and populate the spinner
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        //Dropdown layout stylea
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching array adapter to spinner
        sCategories.setAdapter(arrayAdapter);

        sCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Category")) {
                    Toast.makeText(LeisureAdvertisement.this, "Please choose category", Toast.LENGTH_SHORT).show();
                } else {
//                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
//                    startActivity(intent);
                    categoryItem = parent.getItemAtPosition(position).toString();
                    tilName.setHint("Name of " + categoryItem);
                    // Show selected item
                    //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
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

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is picked from gallery, get uri of image
                image_uri = data.getData();
                //set to image view
                ivPhoto.setImageURI(image_uri);
                ivPhoto.setVisibility(View.VISIBLE);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri of image
                ivPhoto.setImageURI(image_uri);
                ivPhoto.setVisibility(View.VISIBLE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

        //this method will be called after picking image from camera or gallery
//        if (resultCode == RESULT_OK) {
//            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
//                if (data.getClipData() != null) {
//                    int countClipData = data.getClipData().getItemCount();
//                    int currentImageSelect = 0;
//                    while (currentImageSelect < countClipData) {
//                        ImageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
//                        ImageList.add(ImageUri);
//                        currentImageSelect = currentImageSelect + 1;
//                    }
//
//                    tvAlert.setVisibility(View.VISIBLE);
//                    tvAlert.setText("You have selected " + ImageList.size() + " Images");
//
//                } else {
//                    Toast.makeText(this, "Please Select Multiple Image", Toast.LENGTH_SHORT).show();
//
//                }
//                //image is picked from gallery, get uri of image
//                // image_uri = data.getData();
//
//                //set to image view
//                //ivPost.setImageURI(image_uri);
//            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
//                //image is picked from camera, get uri of image
//                //ivPost.setImageURI(image_uri);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

}


//btnAddPhoto = (MaterialButton) findViewById(R.id.mbtn_add_photo);
// btnSubmit = (Button) findViewById(R.id.mbtn_submit);
//        insertPhoto = (ImageView) findViewById(R.id.iv_insert_photo);
//uploadPhoto = (RecyclerView) findViewById(R.id.rv_add_photo);

//        fileNameList = new ArrayList<>();
//        fileDoneList = new ArrayList<>();
//
//        uploadList = new UploadList(fileNameList, fileDoneList);

//        uploadPhoto.setLayoutManager(new LinearLayoutManager(this));
//        uploadPhoto.setHasFixedSize(true);
//        uploadPhoto.setAdapter(uploadList);


//        tilName = (TextInputLayout) findViewById(R.id.til_name);
//        tilAddress = (TextInputLayout) findViewById(R.id.til_address);
//        tilDescription = (TextInputLayout) findViewById(R.id.til_description);
//        tilOpenHrs = (TextInputLayout) findViewById(R.id.til_oper_hrs);
//        tilContact = (TextInputLayout) findViewById(R.id.til_contact);
//        tilWebAddress = (TextInputLayout) findViewById(R.id.til_web_address);
//        tilLatitude = (TextInputLayout) findViewById(R.id.til_latitude);
//        tilLongitude = (TextInputLayout) findViewById(R.id.til_longitude);

//
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//
//        CategoryImagesRef = FirebaseStorage.getInstance().getReference().child("CategoryImage");
//        LeisureRef = FirebaseDatabase.getInstance().getReference().child("Leisure");
////        databaseReference = FirebaseDatabase.getInstance().getReference().child("ImagePath");
////        storageReference = FirebaseStorage.getInstance().getReference();
//
//        loadingBar = new ProgressDialog(this);
//
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////
////                name = tilName.getEditText().getText().toString().trim();
////                address = tilAddress.getEditText().getText().toString().trim();
////                hours = tilOpenHrs.getEditText().getText().toString().trim();
////                contact = tilContact.getEditText().getText().toString().trim();
////                description = tilDescription.getEditText().getText().toString().trim();
////                webAddress = tilWebAddress.getEditText().getText().toString().trim();
////                latitude = tilLatitude.getEditText().getText().toString().trim();
////                longitude = tilLongitude.getEditText().getText().toString().trim();
////
////                if (!validateName() | !validateAddress() | !validateDescription() | !validateOpenHrs() | !validateContact()
////                        | !validateImage() | !validateLatitude() | !validateLongitude()) {
////                    //        materialButtonSignUp.setVisibility(View.INVISIBLE);
////                    //          progressBar.setVisibility(View.INVISIBLE);
////                    return;
////                } else {
////                    storeInformation();
////                }
//            }
//        });


//    private void storeInformation() {
//
//        loadingBar.setTitle("Adding new Leisure");
//        loadingBar.setMessage("Please wait...");
//        loadingBar.setCanceledOnTouchOutside(false);
//        loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        loadingBar.show();
//
//
//        Calendar calendar = Calendar.getInstance();
//
//        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
//        saveCurrentDate = currentDate.format(calendar.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//        saveCurrentTime = currentTime.format(calendar.getTime());
//
//
//        randomKey = saveCurrentDate + " " + saveCurrentTime;
//
//        StorageReference filePath = CategoryImagesRef.child(categoryItem).child(fileUri.getLastPathSegment() + randomKey + ".jpg");
////        StorageReference filepaths = filePath.child();
//        final UploadTask uploadTask = filePath.putFile(fileUri);
//
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                String message = e.toString();
//                Toast.makeText(LeisureAdvertisement.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                loadingBar.dismiss();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(LeisureAdvertisement.this, "Image Uploaded Successfully: ", Toast.LENGTH_SHORT).show();
//                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                        if (!task.isSuccessful()) {
//                            throw task.getException();
//
//                        }
//                        downloadImageUrl = filePath.getDownloadUrl().toString();
//                        return filePath.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()) {
//                            downloadImageUrl = task.getResult().toString();
//                            Toast.makeText(LeisureAdvertisement.this, "getting image url successfully", Toast.LENGTH_SHORT).show();
//                            SaveLeisureInfoToDatabase();
//                        }
//                    }
//                });
//            }
//        });
//
//    }
//
//    private void SaveLeisureInfoToDatabase() {
//
//        String status = "pending";
//        HashMap<String, Object> leisureMap = new HashMap<>();
//        leisureMap.put("Category", categoryItem);
//        //leisureMap.put("Role", roleItem);
//        leisureMap.put("Lid", randomKey);
//        leisureMap.put("Date", saveCurrentDate);
//        leisureMap.put("Time", saveCurrentTime);
//        leisureMap.put("Image", downloadImageUrl);
//        leisureMap.put("Name", name);
//        leisureMap.put("Address", address);
//        leisureMap.put("Description", description);
//        leisureMap.put("Operating Hours", hours);
//        leisureMap.put("Contact Number", contact);
//        leisureMap.put("Website", webAddress);
//        leisureMap.put("Latitude", latitude);
//        leisureMap.put("Longitude", longitude);
//        leisureMap.put("Status", status);
//
//        LeisureRef.child(name).updateChildren(leisureMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
////                            Intent intent = new Intent(LeisureAdvertisement.this, ActivityMain.class);
////                            startActivity(intent);
//                            loadingBar.dismiss();
//                            startActivity(new Intent(LeisureAdvertisement.this, ActivityMain.class));
//                            Toast.makeText(LeisureAdvertisement.this, "Leisure added successfully", Toast.LENGTH_SHORT).show();
//                        } else {
//                            loadingBar.dismiss();
//                            String message = task.getException().toString();
//                            Toast.makeText(LeisureAdvertisement.this, "Error:" + message, Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//    }
//
//    private void addPhoto() {
//        insertPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
//            }
//        });
//    }

//    private void role() {
//        List<String> role = new ArrayList<>();
//        role.add(0, "Choose your Role");
//        role.add("Customer");
//        role.add("Visitor");
//        role.add("Owner");
//        role.add("Employee");
//
//        //Style and populate the spinner
//        ArrayAdapter<String> arrayAdapter;
//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, role);
//
//
//        //Dropdown layout stylea
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        //Attaching array adapter to spinner
//        sRole.setAdapter(arrayAdapter);
//
//        sRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Choose your Role")) {
//                    // Do nothing
//                } else {
////                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
////                    startActivity(intent);
//                    roleItem = parent.getItemAtPosition(position).toString();
//                    // Show selected item
//                    //Toast.makeText(parent.getContext(), "Selected: " + roleItem, Toast.LENGTH_SHORT).show();
////
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
//            fileUri = data.getData();
//            Picasso.get().load(fileUri).fit().into(insertPhoto);
//        }
//
////        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
////            if (data.getClipData() != null) {
////                int totalItemSelected = data.getClipData().getItemCount();
////                for (int i = 0; i < totalItemSelected; i++) {
////                    fileUri = data.getClipData().getItemAt(i).getUri();
////                    fileName = getFileName(fileUri);
////                    fileNameList.add(fileName);
////                    uploadList.notifyDataSetChanged();
//
////                    storageReference = FirebaseStorage.getInstance().getReference();
////                    databaseReference= FirebaseDatabase.getInstance().getReference().child("ImagePath");
////
////                    StorageReference fileToUpload = storageReference.child("Images").child(fileName);
////                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
////                        @Override
////                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                            Toast.makeText(LeisureAdvertisement.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
////                        }
////                    });
//
////                }
//        // Toast.makeText(LeisureAdvertisement.this,"Selected Multiple Files",Toast.LENGTH_SHORT).show();
////            } else if (data.getData() != null) {
////                Toast.makeText(LeisureAdvertisement.this, "Selected Single Files", Toast.LENGTH_SHORT).show();
////            }
////        }
//
//    }
//
//    public String getFileName(Uri uri) {
//        String result = null;
//        if (uri.getScheme().equals("content")) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            try {
//                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                }
//            } finally {
//                cursor.close();
//            }
//        }
//        if (result == null) {
//            result = uri.getPath();
//            int cut = result.lastIndexOf('/');
//            if (cut != -1) {
//                result = result.substring(cut + 1);
//            }
//        }
//        return result;
//    }
//
//

