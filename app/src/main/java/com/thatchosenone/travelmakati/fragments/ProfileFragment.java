package com.thatchosenone.travelmakati.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.activities.Login;
import com.thatchosenone.travelmakati.connection.ConnectivityReceiver;
import com.thatchosenone.travelmakati.connection.MyApplication;
import com.thatchosenone.travelmakati.R;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String COMMON_TAG = "ProfileFragment";
    private View profile;
//    ListView lvProfileItem;
//    String lvTitle[] = {"Profile", "Upload Photo", "Recently Viewed"};
//    String lvCount[] = {"256", "1000", "1213"};


    GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    MaterialButton mbtnLogout;

    //    FirebaseStorage firebaseStorage;
//
//    //Storage
//    StorageReference storageReference;
//
//    //path where images of user profile and cover will be stored
//    String storagePath = "Users_Profile_Cover_Imgs/";
//
//    //View for xml
//    ImageView ivUserPic, ivUserCover;
//    TextView tvName, tvEmail, tvCity, tvMorePhoto, tvMoreReviews, tvMoreRecent;
//    //Floating action button
//    FloatingActionButton fabEdit;
//    RecyclerView rvPhotos, rvReviews, rvRecent;
//
//    //uri of picked image
//    Uri image_uri;
//
//    //permission constant
//    private static final int CAMERA_REQUEST_CODE = 100;
//    private static final int STORAGE_REQUEST_CODE = 200;
//    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
//    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 400;
//
//    //arrays of permission to be requested
//    String cameraPermission[];
//    String storagePermission[];
//    String uid;
//
//    //for checking profile or cover photo
//    String profileOrCoverPhoto;
//
//    //Image Pick
//    private static int PICK_IMAGE = 123;
//    Uri imagePath;
//    static int PReqCode = 1;
//
//    List<LeisurePhotosModel> photoList;
//    LeisurePhotosAdapter photoAdapter;
//
//    List<ReviewModel> postList;
//    YourReviewAdapter postAdapter;
//
//    List<LeisureModel> leisureList;
//    RecentLeisureAdapter recentAdapter;
//
//    List<RecentViewModel> recentList;
//
//
//    ProgressDialog progressDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profile = inflater.inflate(R.layout.fragment_profile, container, false);
        //View inflatedView = getLayoutInflater().inflate(R.layout.edit_profile_pic, null);

        //Init fire base
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //init views
        mbtnLogout = (MaterialButton) profile.findViewById(R.id.fp_mbtn_logout);
        //lvProfileItem = (ListView) profile.findViewById(R.id.fp_lv_profile);


        mbtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        //firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseStorage = getInstance();
//        storageReference = getInstance().getReference(); // firebase storage reference
//
//        //Path of table of users
//        databaseReference = firebaseDatabase.getReference("Users");
//        recentRef = firebaseDatabase.getReference("RecentView");
//        leisureRef = firebaseDatabase.getReference("Leisure");
//
//        //Init view from xml
//        ivUserPic = (ImageView) profile.findViewById(R.id.fp_iv_profile_pic);
//        ivUserCover = (ImageView) profile.findViewById(R.id.fp_iv_cover);
//        tvEmail = (TextView) profile.findViewById(R.id.fp_tv_email);
//        tvName = (TextView) profile.findViewById(R.id.fp_tv_name);
//        tvCity = (TextView) profile.findViewById(R.id.fp_tv_city);
//        fabEdit = (FloatingActionButton) profile.findViewById(R.id.fp_mfab_edit);
//        rvPhotos = profile.findViewById(R.id.fp_rv_photo);
//        rvReviews = profile.findViewById(R.id.fp_rv_post);
//        rvRecent = profile.findViewById(R.id.fp_rv_recent);
        //lvProfileItem = profile.findViewById(R.id.fp_lv_profile);

//        tvMorePhoto = profile.findViewById(R.id.fp_tv_more_photo);
//        tvMoreReviews = profile.findViewById(R.id.fp_tv_more_post);
//        //tvMoreRecent = profile.findViewById(R.id.fp_tv_more_recent);
//
//        tvMorePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addreview = new Intent(getContext(), AllPhotos.class);
//                addreview.putExtra("myuid", firebaseUser.getUid());
//                addreview.putExtra("ald", "profile_fragment");
//                startActivity(addreview);
//            }
//        });
//
//        tvMoreReviews.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addreview = new Intent(getContext(), AllReviews.class);
//                addreview.putExtra("myuid", firebaseUser.getUid());
//                addreview.putExtra("ald", "profile_fragment");
//                startActivity(addreview);
//            }
//        });
//
//        //init progress dialog
//        progressDialog = new ProgressDialog(getActivity());
//
//        //init arrays permission
//        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        LinearLayoutManager layoutManagers = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        //show newest post first, for this load from last
        layoutManagers.setStackFromEnd(true);
        layoutManagers.setReverseLayout(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        LinearLayoutManager layoutManagerss = new LinearLayoutManager(getContext());
        //show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);


//        rvPhotos.setLayoutManager(layoutManagers);
//        rvReviews.setLayoutManager(layoutManager);
//        rvRecent.setLayoutManager(layoutManagerss);
//
//        //init post list
//        photoList = new ArrayList<>();
//        loadPhotos();
//
//        //init post list
//        postList = new ArrayList<>();
//        loadReviews();
//
//
//        recentList = new ArrayList<>();
//        loadRecent();

//
//        //fab action
//        fabEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showEditProfile();
//            }
//        });


//        LvAdapter adapter = new LvAdapter(getContext(), lvTitle, lvCount);
//        lvProfileItem.setAdapter(adapter);
//
//        //set item click on list view
//        lvProfileItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(position == 0){
//
//                }
//                if(position == 1){
//
//                }
//                if(position == 2){
//
//                }
//                if(position == 3){
//
//                }
//                if(position == 4){
//
//                }
//                if(position == 5){
//
//                }
//                if(position == 6){
//
//                }
//                if(position == 7){
//
//                }
//                if(position == 8){
//
//                }
//            }
//        });

//        //Getting the info of user
//        Query userData = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
//        userData.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //check until required data get
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    //get data
//                    String name = "" + ds.child("name").getValue();
//                    String email = "" + ds.child("email").getValue();
//                    String city = "" + ds.child("city").getValue();
//                    String profile = "" + ds.child("display_photo").getValue();
//                    String cover = "" + ds.child("cover_photo").getValue();
//
//                    //set data
//                    tvName.setText(name);
//                    tvEmail.setText(email);
//                    tvCity.setText(city);
//
//                    //try and catch
//                    try {
//                        Picasso.get().load(profile).fit().centerCrop().into(ivUserPic);
//
//                        //if image is received the set
//                    } catch (Exception e) {
//                        //if theres any exception while getting image then set default
////                        Picasso.get().load(R.drawable.ic_default_photo).into(ivUserPic);
//                    }
//
//                    try {
//                        Picasso.get().load(cover).fit().centerCrop().into(ivUserCover);
//                        //if image is received the set
//                    } catch (Exception e) {
//                        //if theres any exception while getting image then set default
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

        checkUserStatus();
        checkConnection();
        //loadMyPost();
        return profile;
    }

    private void logout() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Signing out...");
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("users").orderByChild("uID").equalTo(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        String type = "" + issue.child("accountType").getValue();
                        if (type.equalsIgnoreCase("1")) {
                            firebaseAuth.signOut();
                            Intent signOut = new Intent(getContext(), Login.class);
                            signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(signOut);
                            getActivity().finish();
                        }

                        if (type.equalsIgnoreCase("2")) {
                            firebaseAuth.signOut();
                            LoginManager.getInstance().logOut();
                            Intent signOut = new Intent(getContext(), Login.class);
                            signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(signOut);
                            getActivity().finish();
                        }

                        if (type.equalsIgnoreCase("3")) {
                            // Configure Google Sign In
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(getString(R.string.default_web_client_id))
                                    .requestEmail()
                                    .build();
                            mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                            mGoogleSignInClient.revokeAccess()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            firebaseAuth.signOut();
                                            Intent signOut = new Intent(getContext(), Login.class);
                                            signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(signOut);
                                            getActivity().finish();
                                        }

                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Unable to logout: " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


//    class LvAdapter extends ArrayAdapter<String>{
//        Context context;
//        String rTitle[];
//        String rImage[];
//
//        LvAdapter (Context c, String title[], String imgs[]){
//            super(c, R.layout.row_profile_item, R.id.fp_tv_list,  title, imgs);
//            this.context = c;
//            this.rTitle = title;
//            this.rImage = imgs;
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View row = layoutInflater.inflate(R.layout.row_profile_item, parent,false);
//
//            ImageView myImage = row.findViewById(R.id.fp_iv_icon);
//            TextView myTitle = row.findViewById(R.id.fp_tv_list);
//
//            //set resources on views
//            myImage.setImageResource(rImage[position]);
//            myTitle.setText(rTitle[position]);
//
//            return row ;
//        }
//    }

    private void loadRecent() {

//        recentRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                recentList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    RecentViewModel recent = ds.getValue(RecentViewModel.class);
//                    recentList.add(recent);
//                }
//                loadAllRecent();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    private void loadAllRecent() {
//        leisureList = new ArrayList<>();
//        leisureRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                leisureList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);
//                    for (RecentViewModel recent : recentList) {
//                        if (leisureModel.getLuid() != null && leisureModel.getLuid().equals(recent.getId())) {
//                            leisureList.add(leisureModel);
//                            break;
//                        }
//                    }
//                    //adapter
//                    recentAdapter = new RecentLeisureAdapter(getContext(), leisureList);
//                    //set adapter
//                    rvRecent.setAdapter(recentAdapter);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }

    private void loadReviews() {
        //path of all posts
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ratings");
//        //get all data from this ref
//        ref.limitToFirst(5).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                postList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    ReviewModel reviewModel = ds.getValue(ReviewModel.class);
//                    if (reviewModel.getUser_uid().equals(firebaseUser.getUid())) {
//                        postList.add(reviewModel);
//                    }
////                    for (DataSnapshot dss : ds.getChildren()) {
////
////                    }
//                    //adapter
//                    postAdapter = new YourReviewAdapter(getContext(), postList);
//                    //set adapter to recyclerview
//                    rvReviews.setAdapter(postAdapter);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //in case of error
//                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    private void loadPhotos() {
//        //path of all posts
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LeisurePhotos");
//        //get all data from this ref
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                photoList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    LeisurePhotosModel photoModel = ds.getValue(LeisurePhotosModel.class);
//                    if (photoModel.getUser_uid().equals(firebaseUser.getUid())) {
//                        photoList.add(photoModel);
//                    }
//                    //adapter
//                    photoAdapter = new LeisurePhotosAdapter(getContext(), photoList);
//                    //set adapter to recyclerview
//                    rvPhotos.setAdapter(photoAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //in case of error
//                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            //user is signed in stay here
            //set email of logged in user
            //myprofile set to user.getEmail
            String uid = user.getUid();

        } else {
            //user not signed, goto main activity
            Log.d("TAG", "firebaseUser is null");
            Intent login = new Intent(getContext(), Login.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
        }
    }

    //method of show edit profile
    private void showEditProfile() {
        //        Show dialog containing options
        //        1.)Edit prifle Pic
        //        2.)Edit cover Pic
        //        3.)Edit city Pic
        //        4.)Edit name Pic

        //options to show in dialog
//        String options[] = {"Edit Profile Picture", "Edit Cover Photo", "Edit Name", "Edit City"};
//
//        //alert dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        //set title
//        builder.setTitle("Choose action");
//
//        //set items to dialog
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //handle dialog item click
//                if (which == 0) {
//                    //edit profile clicked
//                    progressDialog.setMessage("Updating Profile Picture");
//                    profileOrCoverPhoto = "display_photo"; // i.e changing profile picture , make sure to assign same value
//                    showImageDialog();
//                } else if (which == 1) {
//                    //edit cover clicked
//                    profileOrCoverPhoto = "cover_photo"; // i.e changing profile picture , make sure to assign same value
//                    progressDialog.setMessage("Updating Cover Photo");
//                    showImageDialog();
//                } else if (which == 2) {
//                    //edit name clicked
//                    progressDialog.setMessage("Updating Name");
//                    showNameCityUpdateDialog("name");
//                } else if (which == 3) {
//                    //edit city clicked
//                    progressDialog.setMessage("Updating City");
//                    showNameCityUpdateDialog("city");
//                }
//            }
//        });
//        //create and show dialog
//        builder.create().show();
    }

    private void showNameCityUpdateDialog(String key) {
        /*parameter "key" will contain value:
//         *either "name" which is key in user's database which is used to update user's name
//         * or "city" which is key in user's database which is used to update user's phone
//         */
//
//        //custom dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Update " + key); // e.g update name or update city
//
//        LinearLayout linearLayout = new LinearLayout(getActivity());
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setPadding(10, 10, 10, 10);
//
//        //add edit text
//        EditText editText = new EditText(getActivity());
//        editText.setHint("Enter " + key);
//        linearLayout.addView(editText);
//
//        builder.setView(linearLayout);
//
//        //add buttons in dialog to update
//        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //input text from edit text
//                String value = editText.getText().toString().trim();
//
//                //validate if user has entered something or not
//                if (!TextUtils.isEmpty(value)) {
//                    progressDialog.show();
//                    HashMap<String, Object> result = new HashMap<>();
//                    result.put(key, value);
//
//                    databaseReference.child(firebaseUser.getUid()).updateChildren(result)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    //updated dismiss progress
//                                    progressDialog.dismiss();
//                                    Toast.makeText(getActivity(), "Updated..." + key, Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    //failed, dismiss progress, get and show error message
//                                    progressDialog.dismiss();
//                                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                    //if user edit his name, also change it from his posts
//                    if (key.equals("name")) {
//                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
//                        Query query = ref.orderByChild("uid").equalTo(uid);
//                        query.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    String child = ds.getKey();
//                                    dataSnapshot.getRef().child(child).child("uName").setValue(value);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "Please enter " + key, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        //add buttons in dialog to cancel
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builder.create().show();
    }

    //method of show image dialog
    private void showImageDialog() {
        //show dialog containing option camera and gallery to pick image

        //options to show in dialog
        String options[] = {"Camera", "Gallery"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //set title
        builder.setTitle("Pick Image");

        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item click
                if (which == 0) {
                    //camera clicked
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    //Gallery clicked
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

    //method of check storage permission
    private boolean checkStoragePermission() {
        //check if storage permission is enable or not
        //return true if enabled
        //return false if unabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    //method of request storage permission
    private void requestStoragePermission() {
        //request runtime storage permission
//        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    //method of check camera permission
    private boolean checkCameraPermission() {
        //check if storage permission is enable or not
        //return true if enabled
        //return false if not
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    //method of request camera permission
    private void requestCameraPermission() {
        //request runtime storage permission
//        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    //check connection
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    //method for pick from gallery
    private void pickFromGallery() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_REQUEST_CODE);
    }

    //method for pick from camera
    private void pickFromCamera() {
//        //Intent of picking image from device camera
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
//        //put image to uri
//        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        //intent to start camera
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
//        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_REQUEST_CODE);
    }

    //show snack
    private void showSnack(boolean isConnected) {
        if (isConnected) {
        } else {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame, new InternetConnection());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    /*Inflate option menu*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.main_menu, menu);
//        menu.findItem(R.id.add_post_mm).setVisible(false);
//        menu.findItem(R.id.search_all_mm).setVisible(false);
//
//
//        MenuItem searchAll = menu.findItem(R.id.add_search_mm);
//        Intent intent = new Intent(getContext(), SearchAllLeisure.class);
//        searchAll.setIntent(intent);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        /*This method called when user press allow or deny from permission request dialog
//        here we will handle permission cases (allowed & denied)
//         */
//        switch (requestCode) {
//            case CAMERA_REQUEST_CODE: {
//                //picking from camera, first if camera and storage permission allowed or not
//                if (grantResults.length > 0) {
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (cameraAccepted && writeStorageAccepted) {
//                        //permission enabled
//                        pickFromCamera();
//                    } else {
//                        //permission denied
//                        Toast.makeText(getActivity(), "Please enable camera & storage permission", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            break;
//            case STORAGE_REQUEST_CODE: {
//                //picking from camera, first if camera and storage permission allowed or not
//                if (grantResults.length > 0) {
//                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (writeStorageAccepted) {
//                        //permission enabled
//                        pickFromGallery();
//                    } else {
//                        //permission denied
//                        Toast.makeText(getActivity(), "Please enable storage permission", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            break;
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //this method will be called after picking image from camera or gallery
//        if (resultCode == RESULT_OK) {
//            if (requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE) {
//                //image is picked from gallery, get uri of image
//                image_uri = data.getData();
//                uploadProfilePicCoverPhoto(image_uri);
//            }
//            if (requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE) {
//                uploadProfilePicCoverPhoto(image_uri);
//            }
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);

    }

    //method of upload profilepic and cover photo
    private void uploadProfilePicCoverPhoto(Uri uri) {
        //show progress
//        progressDialog.show();
//
//        /* Instead of creating seperate function for profile and cover photo
//            im doing work for both in same function
//
//            to add check ill add a string variable and assign it value "image" when user clicks
//             "EDIT PROFILE PIC" and assign it value "cover"  when user clicks "EDIT COVER PHOTO"
//            Here: image is the key in each user containing url of user's profile pic
//                  cover is the key in each user containing url of user's cover photo
//        */
//
//        /*The parameter "image_uri" contains the uri of image picked either from camera or gallery
//         * We will user UID of the currently signed in user as name of the image so there will be only one
//         * profile and one image for cover each user*/
//
//        //path and name to be stored in firebase storage
//        String filePathAndName = storagePath + "" + profileOrCoverPhoto + "" + firebaseUser.getUid();
//        StorageReference storageReference1 = storageReference.child(filePathAndName);
//        storageReference1.putFile(uri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        //image is uploaded to storage, now get it's url ad store in user's database
//                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                        while (!uriTask.isSuccessful()) ;
//                        Uri downloadUrl = uriTask.getResult();
//
//                        //check if image is uploaded or not and url is received
//                        if (uriTask.isSuccessful()) {
//                            //image uploaded
//                            //add/update url in user's database
//                            HashMap<String, Object> results = new HashMap<>();
//
//                            /*First parameter is profileOrCoverPhoto that has value "image" or "cover"
//                             * which are keys in user's database where url will be saved in one of them
//                             * Second parameter contains the url of the image stored in firebase storage, this
//                             * url will be saved as value against key "image" or "cover" */
//                            results.put(profileOrCoverPhoto, downloadUrl.toString());
//
//                            databaseReference.child(firebaseUser.getUid()).updateChildren(results)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            //url in database of user is added successfully
//                                            //dismiss progress bar
//                                            progressDialog.dismiss();
//                                            Toast.makeText(getActivity(), "Image Updated...", Toast.LENGTH_SHORT).show();
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            //error adding url in database of user
//                                            //dismiss progress bar
//                                            progressDialog.dismiss();
//                                            Toast.makeText(getActivity(), "Error Updating Image...", Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    });
//
//                            //if user edit his name, also change it from his posts
//                            if (profileOrCoverPhoto.equals("image")) {
//                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
//                                Query query = ref.orderByChild("uid").equalTo(uid);
//                                query.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                            String child = ds.getKey();
//                                            dataSnapshot.getRef().child(child).child("uDp").setValue(downloadUrl.toString());
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//                            }
//                        } else {
//                            progressDialog.dismiss();
//                            Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(COMMON_TAG, "ProfileFragment onSaveInstanceState");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    

    @Override
    public void onNetworkChange(boolean inConnected) {
        showSnack(inConnected);
    }
}
