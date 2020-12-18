package com.thatchosenone.travelmakati.activities;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.thatchosenone.travelmakati.R;

import java.util.Arrays;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {

//    private static final Pattern EMAIL_ADDRESS =
//            Pattern.compile("^" +
//                    "(?=.*[0-9])" +         //at least 1 digit
//                    "(?=.*[a-z])" +         //at least 1 lower case letter
//                    "(?=.*[A-Z])" +         //at least 1 upper case letter
//                    //"(?=.*[a-zA-Z])" +      //any letter
//                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
//                    "(?=\\S+$)" +           //no white spaces
//                    //".{4,}" +               //at least 4 characters
//                    "$");

    String TAG = "FacebookLogin";
    CallbackManager mFacebookCallbackManager;
    LoginManager mLoginManager;
    AccessTokenTracker mAccessTokenTracker;

    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    private MaterialButton mbtnFB, mbtnGoogle;
    private LoginButton fbSignIn;
    private CallbackManager mCallbackManager;
    // private static final String TAG = "FACELOG";

    private SignInButton googleSignIn;


    private MaterialButton mbtnRegister;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPass;
    private TextInputLayout tilFullname;
    private Toolbar tbRegistration;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser firebaseUser;
    TextView tvAgree;
    private TextView tvLogin;

//    StorageReference UserImage;
//    private static int PICK_IMAGE = 123;
//    Uri imagePath;
//    Uri fileUri;
//
//    static int PReqCode = 1;
//    static int REQUESCODE = 1;
//    private static final int RESULT_LOAD_IMAGE = 1;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signup);

        // Initialize variable
        tilEmail = (TextInputLayout) findViewById(R.id.as_til_email);
        tilPass = (TextInputLayout) findViewById(R.id.as_til_pass);
        tilFullname = (TextInputLayout) findViewById(R.id.as_til_fullname);
        mbtnRegister = (MaterialButton) findViewById(R.id.as_mbtn_register);
        tbRegistration = findViewById(R.id.as_mbtn_register);
        tvAgree = findViewById(R.id.ar_tv_agree);
        mbtnGoogle = (MaterialButton) findViewById(R.id.as_mbtn_google_signin);
        mbtnFB = (MaterialButton) findViewById(R.id.as_mbtn_fb_signin);
        tvLogin = (TextView) findViewById(R.id.as_tv_login);

//        tbRegistration.setTitle("Registration");
//        setSupportActionBar(tbRegistration);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, Login.class));
                finish();
            }
        });

        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Registration.this, Terms.class));
                finish();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mCallbackManager = CallbackManager.Factory.create();


        loadingBar = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

//        city();
//        UserImage = FirebaseStorage.getInstance().getReference().child("UserImage");
//
//        StorageReference storageReference = firebaseStorage.getReference();


        //ProgressBarID
        //progressBar = (ProgressBar)findViewById(R.id.progress_circular);
        //progressBar.setVisibility(View.INVISIBLE);

        // Toolbar Task
//        setSupportActionBar(toolbar);
//        // getSupportActionBar().setTitle("Sign Up");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        civUserPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Build.VERSION.SDK_INT >= 22) {
//                    checkAndRequestForPermission();
//                } else {
//                    openGallery();
//                }
//                //                Intent intent = new Intent();
////                intent.setType("image/*");
////                intent.setAction(Intent.ACTION_GET_CONTENT);
////                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
//            }
//        });
//

//        tvAlSign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent home = new Intent(Registration.this, Login.class);
//                startActivity(home);
//            }
//        });

        //Button Sign Up Task
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TAG = "";
                final String email = tilEmail.getEditText().getText().toString().trim();
                final String pass = tilPass.getEditText().getText().toString().trim();
                final String firstname = tilFullname.getEditText().getText().toString().trim();

                final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Signing Up...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                if (!validateEmail() | !validatePass() | !validateFullname() ) {
                    progressDialog.dismiss();
                    return;
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();

                                        //When user is registered store user info in firebase realtime database too
                                        //using hash map
                                        HashMap<String, Object> hashMap = new HashMap<>();

                                        //Put info in HashMap
                                        hashMap.put("emailAddress", email);
                                        hashMap.put("uID", firebaseAuth.getCurrentUser().getUid());
                                        hashMap.put("onlineStatus", "offline");
                                        hashMap.put("typingTo", "noOne");
                                        hashMap.put("name", firstname);
                                        hashMap.put("accountType", "1");
                                        hashMap.put("accountPhoto", "");
                                        hashMap.put("accountCreated", "");
                                        hashMap.put("accountStatus", "1");
                                        hashMap.put("password", pass);
                                        hashMap.put("isSet", "0");

                                        //Store data to firebase realtime
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("users");
                                        reference.child(firebaseAuth.getCurrentUser().getUid()).setValue(hashMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            progressDialog.dismiss();
                                                            sendEmailVerification();
                                                        } else {
                                                            //display failure message
                                                            progressDialog.dismiss();
                                                            Toast.makeText(SignUp.this, "Sign up Failed!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    } else {
                                        try {
                                            throw task.getException();
                                        }
                                        // if user enters wrong email.
                                        catch (FirebaseAuthWeakPasswordException weakPassword) {
                                            tilPass.setError("Error: Password too weak");
                                            Log.d(TAG, "onComplete: weak_password");

                                            // TODO: take your actions!
                                        }
                                        // if user enters wrong password.
                                        catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                            tilEmail.setError("Error: Wrong format of email");
                                            Log.d(TAG, "onComplete: malformed_email");

                                            // TODO: Take your action
                                        } catch (FirebaseAuthUserCollisionException existEmail) {
                                            progressDialog.dismiss();
                                            tilEmail.setError("Error: Email already exist");
                                            Log.d(TAG, "onComplete: exist_email");

                                            // TODO: Take your action
                                        } catch (Exception e) {
                                            Log.d(TAG, "onComplete: " + e.getMessage());
                                        }
                                        //display failure message

                                    }
                                }
                            });
                }
            }
        });

        // Initialize Facebook Login button
        // fbSignIn.setReadPermissions(Arrays.asList("email", "public_profile"));

        //Facebook Signin
        mbtnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFacebookLogin();
//                fbLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                        handleFacebookAccessToken(loginResult.getAccessToken());
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        Log.d(TAG, "facebook:onCancel");
//                        Toast.makeText(Login.this, "Login Cancel.",
//                                Toast.LENGTH_SHORT).show();
//                        // ...
//                    }
//
//                    @Override
//                    public void onError(FacebookException error) {
//                        Log.d(TAG, "facebook:onError", error);
//                        // ...
//                    }
//                });
            }
        });

        // Google Signin
        mbtnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        setupFacebookStuff();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    // Google Sign in method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        } else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
////            //the user has successfully picked an image
////            //we need to save its reference to a uri variable
////            pickedImgUri = data.getData();
////            userPic.setImageURI(pickedImgUri);
////        }
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
//            imagePath = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
//                civUserPic.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }

//    private void openGallery() {
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), PICK_IMAGE);
//        //startActivityForResult(galleryIntent, REQUESCODE);
//    }
//
//    private void checkAndRequestForPermission() {
//        if (ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                Toast.makeText(Registration.this, "Please Accept for required permission", Toast.LENGTH_SHORT).show();
//            } else {
//                ActivityCompat.requestPermissions(Registration.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
//            }
//        } else {
//            openGallery();
//        }
//    }
//
//
//    private void sendPicData() {
//
//        StorageReference storageReference = firebaseStorage.getReference();
//        StorageReference imageReference = storageReference.child("UserPicture").child(firebaseAuth.getUid()).child("DisplayPhoto");
//        UploadTask uploadTask = imageReference.putFile(imagePath);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Registration.this, "Upload Failed", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(Registration.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUp.this, "Successfully Registered, Please check your email for verification", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(SignUp.this, Login.class));
                    } else {
                        Toast.makeText(SignUp.this, "Verification mail has'nt been sent", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleFacebookLogin() {
        if (AccessToken.getCurrentAccessToken() != null){
            mLoginManager.logOut();
        }else{
            mAccessTokenTracker.startTracking();
            mLoginManager.logInWithReadPermissions(SignUp.this, Arrays.asList("email", "public_profile"));
        }
    }

    private void setupFacebookStuff() {
// This should normally be on your application class
        FacebookSdk.sdkInitialize(getApplicationContext());

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,AccessToken currentAccessToken) {
                //updateFacebookButtonUI();
            }
        };

        mLoginManager = LoginManager.getInstance();
        mFacebookCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setMessage("Signing In...");
        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            String email = user.getEmail();
                            String uid = user.getUid();
                            String name = user.getDisplayName();
                            String image = user.getPhotoUrl().toString();

                            //When user is registered store user info in firebase realtime database too
                            //using hash map
                            HashMap<String, Object> hashMap = new HashMap<>();

                            //Put info in HashMap
                            hashMap.put("emailAddress", email);
                            hashMap.put("uID", uid);
                            hashMap.put("onlineStaus", "offline");
                            hashMap.put("typingTo", "noOne");
                            hashMap.put("name", name);
                            hashMap.put("accountType", "2");
                            hashMap.put("accountPhoto", image);
                            hashMap.put("accountCreated", "");
                            hashMap.put("accountStatus", "1");
                            hashMap.put("isSet", "0");
                            //Store data to firebase realtime
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("users");
                            reference.child(uid).setValue(hashMap);

                            startActivity(new Intent(SignUp.this, ActivityMain.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    // firebaseAuthWithGoogle method
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setMessage("Signing In...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //If user is signing in firstime then get and show user info from google account
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                //Get user email and uid from auth
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = user.getDisplayName();
                                String image = user.getPhotoUrl().toString();
                                //When user is registered store user info in firebase realtime database too
                                //using hash map
                                HashMap<String, Object> hashMap = new HashMap<>();

                                //Put info in HashMap
                                hashMap.put("emailAddress", email);
                                hashMap.put("uID", uid);
                                hashMap.put("onlineStatus", "online");
                                hashMap.put("typingTo", "noOne");
                                hashMap.put("name", name);
                                hashMap.put("accountType", "3");
                                hashMap.put("accountPhoto", image);
                                hashMap.put("accountCreated", "");
                                hashMap.put("accountStatus", "1");
                                hashMap.put("isSet", "0");

                                //Store data to firebase realtime
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("users");
                                reference.child(uid).setValue(hashMap);

                            }

                            startActivity(new Intent(SignUp.this, ActivityMain.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignUp.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Toast.makeText(SignUp.this, " " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //Validate Email
    private boolean validateEmail() {
        String emailInput = tilEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            tilEmail.setError("Error: Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            tilEmail.setError("Error: Please enter a valid Email Address");
            return false;
        } else {
            tilEmail.setError(null);
            return true;
        }
    }

    //Validate Password
    private boolean validatePass() {

        String passInput = tilPass.getEditText().getText().toString().trim();

        if (passInput.isEmpty()) {
            tilPass.setError("Error: Field can't be empty");
            return false;

        } else if (passInput.length() < 8) {
            tilPass.setError("Error: Atleast 8 Characters");
            return false;
        } else {

            tilPass.setError(null);
            return true;
        }
    }

    //Validate First Name
    private boolean validateFullname() {
        String firstNameInput = tilFullname.getEditText().getText().toString().trim();

        if (firstNameInput.isEmpty()) {
            tilFullname.setError("Error: Field can't be empty");
            return false;
        } else {
            tilFullname.setError(null);
            return true;
        }
    }

}
