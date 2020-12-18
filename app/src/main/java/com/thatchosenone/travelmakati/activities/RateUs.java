package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.Rating;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RateUs extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvName;
    TextView sample;

    double ratingNumber;

    String name;
    String uID;

    String thought;
    private TextInputLayout tilExperience;
    private Button mbtnSubmit;
    private MaterialRatingBar ratingBar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ratingsTbl;
    DatabaseReference leisure;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

        tvName = (TextView) findViewById(R.id.tv_name_leisure_rus);
        tilExperience = (TextInputLayout) findViewById(R.id.til_experience);
        mbtnSubmit = (Button) findViewById(R.id.mbtn_rate_us);
        ratingBar = (MaterialRatingBar) findViewById(R.id.rate_us);

        name = getIntent().getStringExtra("leisureName");
        //uID = getIntent().getStringExtra("uniqueID");

        tvName.setText(name);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        leisure = firebaseDatabase.getReference().child("Leisure");
        ratingsTbl = firebaseDatabase.getReference().child("Ratings");


        toolbar = findViewById(R.id.tb_rate_us);
        toolbar.setTitle("Rate Us");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        mbtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingNumber = ratingBar.getRating();
                thought = tilExperience.getEditText().getText().toString();
                if (!validateExpi()) {
                    return;
                } else {
                    ratingCalculate();
                    rating();

                }
            }
        });
    }

    private void ratingCalculate() {
        try {//
//            final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//            final DatabaseReference dbRef = db.child("Ratings").child(name).child(firebaseUser.getUid());
            ratingsTbl.child(name).child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double total = 0.0;
                    double count = 0.0;
                    double average = 0.0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        num = (int) ds.getChildrenCount();
                        double rating = Double.parseDouble(String.valueOf(ds.child("rateValue").getValue()));
                        total = total + rating;
                        count = count + 1;
                        average = total / count;
//                     total = rates * 2;
                    }
                    final DatabaseReference newRef = ratingsTbl.child(name).child("AverageRating");
                    newRef.child("Accumulated").setValue(average);
                    //                        total = total + rates;
////                        count = count + 1;
//                        average = total / count;

                    //  sample.setText("" + rates);
                    //}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });
        } catch (Exception e) {
            Toast.makeText(RateUs.this, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void rating() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//        String saveCurrentTime = currentTime.format(calendar.getTime());


//        String datePosted = saveCurrentDate + " " + saveCurrentTime;

        Rating accountData = new Rating(
                ratingNumber, thought, saveCurrentDate
        );
        ratingsTbl.child(name).child("Users").child(firebaseUser.getUid()).setValue(accountData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //startActivity(new Intent(RateUs.this, AllLeisureDetails.class));
                            Toast.makeText(RateUs.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            clearText();
                        }
                    }
                });
//        ratingsTbl.child(name).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
//                    //remove old value
//                    ratingsTbl.child(name).child(firebaseUser.getUid()).removeValue();
//                    //update new value
//                    ratingsTbl.child(name).child(firebaseUser.getUid()).setValue(accountData);
//                } else {
//                    ratingsTbl.child(name).child(firebaseUser.getUid()).setValue(accountData);
//                }
//                Toast.makeText(RateUs.this, "Leisure added successfully", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        ////
//        //Map<String, Integer> myMap = new HashMap<String, Integer>();
//        HashMap<String, Object> ratingMap = new HashMap<>();
//        ratingMap.put("Rate Value", ratingNumber);
//        ratingMap.put("Comment", thought);
//
//        ratingsTbl.child(name).child(firebaseUser.getUid()).updateChildren(ratingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
////                        Intent intent = new Intent(RateUs.this, AllLeisureDetails.class);
////                        startActivity(intent);
//                    Toast.makeText(RateUs.this, "Leisure added successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    String message = task.getException().toString();
//                    Toast.makeText(RateUs.this, "Error:" + message, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        Rating rating = new Rating(thought, ratingNumber);
//
//
//        Query leisureRating = ratingsTbl.child(name).child(firebaseUser.getUid()).orderByChild("RateValue").equalTo(firebaseUser.getUid());
//
    }


    private boolean validateExpi() {
        String expi = tilExperience.getEditText().getText().toString().trim();

        if (expi.isEmpty()) {
            tilExperience.setError("Error: Field can't be empty");
            return false;
        } else {
            tilExperience.setError(null);
            return true;
        }
    }

    private void clearText() {
        ratingBar.setRating(0);
        tilExperience.getEditText().setText("");
    }





}
