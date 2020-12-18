package com.thatchosenone.travelmakati.activities;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thatchosenone.travelmakati.R;

public class ForgotPassword extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private Button mbtnReset;
    private Toolbar tbForgot;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        tilEmail = (TextInputLayout) findViewById(R.id.afp_til_email);
        mbtnReset = (Button) findViewById(R.id.afp_mbtn_reset);
        tbForgot = findViewById(R.id.afp_tb_forgot);


        email = tilEmail.getEditText().getText().toString().trim();

        tbForgot.setTitle("Forgot Password");
        setSupportActionBar(tbForgot);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        tbForgot.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ForgotPassword.this, Login.class));
//                finish();
//            }
//        });


        mbtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = tilEmail.getEditText().getText().toString().trim();

                if (!validateEmail()) {
                    return;
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this, "Password reset email send", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ForgotPassword.this, Login.class));
                                //finish();
                            } else {
                                tilEmail.getEditText().setText(" ");
                                Toast.makeText(ForgotPassword.this, "Error sending password reset email", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    //sendReset();
                }
            }
        });

    }

    private void sendReset() {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Password reset email send", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgotPassword.this, Login.class));
                    //finish();
                } else {
                    tilEmail.getEditText().setText(" ");
                    Toast.makeText(ForgotPassword.this, "Error sending password reset email", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
