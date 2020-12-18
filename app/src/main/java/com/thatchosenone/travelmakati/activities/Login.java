package com.thatchosenone.travelmakati.activities;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thatchosenone.travelmakati.R;

import java.util.Arrays;
import java.util.HashMap;


public class Login extends AppCompatActivity {

    String TAG = "FacebookLogin";
    CallbackManager mFacebookCallbackManager;
    LoginManager mLoginManager;
    AccessTokenTracker mAccessTokenTracker;

    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    private MaterialButton mbtnSignIn, mbtnFB, mbtnGoogle;
    private TextInputLayout tilEmail, tilPass;
    private TextView tvSignUp;
    private TextView tvForgot;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference account;
    FirebaseDatabase firebaseDatabase;

    private LoginButton fbSignIn;
    private CallbackManager mCallbackManager;
   // private static final String TAG = "FACELOG";

    private SignInButton googleSignIn;
    RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        tilEmail = (TextInputLayout) findViewById(R.id.al_til_email);
        tilPass = (TextInputLayout) findViewById(R.id.al_til_pass);
        mbtnSignIn = (MaterialButton) findViewById(R.id.al_mbtn_signin);
        tvSignUp = (TextView) findViewById(R.id.al_tv_sign_up);
        tvForgot = (TextView) findViewById(R.id.al_tv_forgot);
        mbtnGoogle = (MaterialButton) findViewById(R.id.al_mbtn_google_signin);
        mbtnFB = (MaterialButton) findViewById(R.id.al_mbtn_fb_signin);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mCallbackManager = CallbackManager.Factory.create();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //googleSignIn = (SignInButton) findViewById(R.id.al_google_sign);
        //fbSignIn = findViewById(R.id.al_fb_sign);

//        fbLogin = findViewById(R.id.al_cus_fb_sign);
//        googleLogin = findViewById(R.id.al_google_cus_sign);

        // Initialize Facebook Login button
       //fbSignIn.setReadPermissions(Arrays.asList("email", "public_profile"));

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

        //Email Signin
        mbtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        //When Button Sign UP is Click it goes to Registration Activity
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
                //finish();
            }
        });

        //When Button Sign UP is Click it goes to Registration Activity
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
                finish();
            }
        });

        setupFacebookStuff();
        //updateFacebookButtonUI();
    }

    private void handleFacebookLogin() {
        if (AccessToken.getCurrentAccessToken() != null){
            mLoginManager.logOut();
        }else{
            mAccessTokenTracker.startTracking();
            mLoginManager.logInWithReadPermissions(Login.this, Arrays.asList("email", "public_profile"));
        }
    }

//    private void updateFacebookButtonUI() {
//
//        if (AccessToken.getCurrentAccessToken() != null){
//            //FacebookLogin.setText("Logout");
//            //mUserNameTextView.setText("Hello Friend");
//        }else{
//            //FacebookLogin.setText("Facebook Connect");
//            //mUserNameTextView.setText("Hello Anonymous");
//        }
//
//    }

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

        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        progressBar.setVisibility(View.VISIBLE);

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
                            hashMap.put("onlineStatus", "online");
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

//                            Toast.makeText(Login.this, "Login Success." + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, ActivityMain.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    // Sign In method
    private void signIn() {
        final String email = tilEmail.getEditText().getText().toString().trim();
        final String pass = tilPass.getEditText().getText().toString().trim();
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        if (!validatePhone() | !validatePass()) {
            progressDialog.dismiss();
            return;
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    startActivity(new Intent(Login.this, ActivityMain.class));
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Please verify your email address ", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    // firebaseAuthWithGoogle method
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
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

                            startActivity(new Intent(Login.this, ActivityMain.class));
                            finish();
//
//                            Toast.makeText(Login.this, "Login Success." + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(Login.this, ForgotPassword.class));
//                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                            //  Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Toast.makeText(Login.this, " " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //Validate Email
    private boolean validatePhone() {
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
        }
//        else if(passInput!=passConInput){
//            tilPass.setError("Error: Password doesn't matched");
//            return false;
//        }
//        else if (!PASSWORD_PATTERN.matcher(passInput).matches())
//        {
//            tilPass.setError("Error: Password too weak");
//            return false;
//        }
        else {

            tilPass.setError(null);
            return true;
        }
    }

    // If users are logged in, automatically go to main activity otherwise login activity
    @Override
    protected void onStart() {
        super.onStart();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            Intent login = new Intent(Login.this, ActivityMain.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
            finish();
        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            Intent login = new Intent(Login.this, ActivityMain.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
            finish();
        }
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
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//            loadUserFBData(currentAccessToken);
//            startActivity(new Intent(Login.this, ActivityMain.class));
//            finish();
//        }
//    };
//
//    private void loadUserFBData(AccessToken newAccessToken) {
//        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                try {
//
//                    String firstname = object.getString("firstname");
//                    String lastname = object.getString("lastname");
//                    String email = object.getString("email");
//                    String id = object.getString("id");
//
//                    //When user is registered store user info in firebase realtime database too
//                    //using hash map
//                    HashMap<Object, String> hashMap = new HashMap<>();
//
//                    //Put info in HashMap
//                    hashMap.put("email", email);
//                    hashMap.put("uid", id);
//                    hashMap.put("name", firstname + " " + lastname);
//                    hashMap.put("account_type", "google");
//                    hashMap.put("image", "");
//
//                    //Store data to firebase realtime
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference reference = database.getReference("Users");
//                    reference.child(id).setValue(hashMap);
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields","firstname, lastname, email, id");
//        request.setParameters(parameters);
//        request.executeAsync();
//
//    }

}
