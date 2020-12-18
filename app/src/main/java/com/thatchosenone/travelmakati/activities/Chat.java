package com.thatchosenone.travelmakati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.adapters.ChatAdapter;
import com.thatchosenone.travelmakati.models.ChatModel;
import com.thatchosenone.travelmakati.models.UserModel;
import com.thatchosenone.travelmakati.notification.Data;
import com.thatchosenone.travelmakati.notification.Sender;
import com.thatchosenone.travelmakati.notification.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Chat extends AppCompatActivity {

    //Views from xml
    Toolbar tbChat;
    RecyclerView rvChat;
    CircleImageView ivProfile;
    TextView tvName, tvUserStatus;
    EditText etMessage;
    ImageButton ibSend;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef, leisureRef;
    FirebaseUser firebaseUser;
    //for checking if use has seen message or not
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ChatModel> chatList;
    ChatAdapter chatAdapter;

    String hisUID, lname, chat;
    String myUID;
    String hisImage;

    //volley request queue for notification
    private RequestQueue requestQueue;

    private boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //init views
        tbChat = (Toolbar) findViewById(R.id.ac_tb_chat);
        setSupportActionBar(tbChat);
        tbChat.setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rvChat = (RecyclerView) findViewById(R.id.ac_rv_chat);
        ivProfile = (CircleImageView) findViewById(R.id.ac_civ_user_profile);
        tvName = (TextView) findViewById(R.id.ac_tv_name);
        tvUserStatus = (TextView) findViewById(R.id.ac_tv_user_status);
        etMessage = (EditText) findViewById(R.id.ac_et_message);
        ibSend = (ImageButton) findViewById(R.id.ac_ib_send);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Layout (LinearLayout) for recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        //recyclerview properties
        rvChat.setHasFixedSize(true);
        rvChat.setLayoutManager(linearLayoutManager);


        /*on clicking user from user list we have passed that user's UID using intent
        so get that uid here to get the profile picture, name and start chats with that
        user's
         */
        Intent intent = getIntent();
        hisUID = intent.getStringExtra("hisUID");
        chat = intent.getStringExtra("chats");

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDbRef = firebaseDatabase.getReference("Users");
        leisureRef = firebaseDatabase.getReference("Leisure");

//        if(chats.equals("leisure")){
//
//        }
//        else {
//        }
//

        leisureInfo();
        userInfo();

        //click button to send message
        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                //get text from edit text
                String message = etMessage.getText().toString().trim();
                //check if text is empty or not
                if (TextUtils.isEmpty(message)) {
                    //text empty
                    Toast.makeText(Chat.this, "Cannot send empty message", Toast.LENGTH_SHORT).show();
                } else {
                    //text not empty
                    sendMessage(message);
                }
                //reset et after sending message
                etMessage.setText("");
            }
        });

        //check edit text change listener
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    checkTypingStatus("noOne");
                } else {
                    checkTypingStatus(hisUID); // uid of receiver
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        readMessage();
        seenMessage();

    }

    private void userInfo() {
        //Search user to get that user's info
        Query userQuery = usersDbRef.orderByChild("uid").equalTo(hisUID);
        //get user picture and name
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String name = "" + ds.child("name").getValue();
                    hisImage = "" + ds.child("display_photo").getValue();
                    String typingStatus = "" + ds.child("typing_to").getValue();

                    //check typing status
                    if (typingStatus.equals(firebaseUser.getUid())) {
                        tvUserStatus.setText("typing...");
                    } else {
                        //get the value of online status
                        String onlineStatus = "" + ds.child("online_status").getValue();
                        if (onlineStatus.equals("online")) {
                            tvUserStatus.setText(onlineStatus);
                        } else {
                            //conver time stamp tp proper time date
                            //convert time stamp to dd/mm/yyyy hh:mm: am/pm
                            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(Long.parseLong(onlineStatus));
                            String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
                            tvUserStatus.setText(dateTime);
                        }
                    }

                    //set data
                    tvName.setText(name);

                    try {
                        //image received, set it to image view in toolbar
                        Picasso.get().load(hisImage).into(ivProfile);
                    } catch (Exception e) {
                        //there is exception getting picture, set default picture
//                        Picasso.get().load(R.drawable.ic_default_photo).into(ivProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void leisureInfo() {

        //Search user to get that user's info
        Query userQuery = leisureRef.orderByChild("luid").equalTo(hisUID);
        //get user picture and name
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String name = "" + ds.child("name").getValue();
                    hisImage = "" + ds.child("photo").getValue();
                    //set data
                    tvName.setText(name);

                    try {
                        //image received, set it to image view in toolbar
                        Picasso.get().load(hisImage).into(ivProfile);
                    } catch (Exception e) {
                        //there is exception getting picture, set default picture
//                        Picasso.get().load(R.drawable.ic_default_photo).into(ivProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //check online status
    private void checkOnlineStatus(String status) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online_status", status);
        //update value of status
        dbRef.updateChildren(hashMap);

    }

    //check typing status
    private void checkTypingStatus(String typing) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("typing_to", typing);
        //update value of status
        dbRef.updateChildren(hashMap);

    }

    //seen message
    private void seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatModel chatModel = ds.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(firebaseUser.getUid()) && chatModel.getSender().equals(hisUID)) {
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("isSeen", true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //read message
    private void readMessage() {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatModel chatModel = ds.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(firebaseUser.getUid()) && chatModel.getSender().equals(hisUID)
                            || chatModel.getReceiver().equals(hisUID) && chatModel.getSender().equals(firebaseUser.getUid())) {
                        chatList.add(chatModel);
                    }
                    //adapter
                    chatAdapter = new ChatAdapter(Chat.this, chatList, hisImage);
                    chatAdapter.notifyDataSetChanged();
                    //setAdapter to recyclerview
                    rvChat.setAdapter(chatAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //send message
    private void sendMessage(final String message) {
        /*Chat's node will created that will contain all chats
         * whenever user send message it well create new child in "Chats"
         * the following key values
         * sender: UID of sender
         * receiver: UID if receiver
         * message: the actual message*/

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", firebaseUser.getUid());
        hashMap.put("receiver", hisUID);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isSeen", false);

        databaseReference.child("Chats").push().setValue(hashMap);
        //reset edittext after sending message
        //etMessage.setText("");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if (notify) {
                    sendNotification(hisUID, user.getName(), message);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //create chatlist node/child in firebase database
        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(hisUID);

        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef1.child("id").setValue(hisUID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //create chatlist node/child in firebase database
        final DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(hisUID)
                .child(firebaseUser.getUid());

        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef2.child("id").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String hisUID, final String name, final String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), name + ": " + message, "New Message", hisUID, R.drawable.default_profile_female);

                    Sender sender = new Sender(data, token.getToken());

                    // from json object request
                    try {
                        JSONObject senderJsonObj = new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //response of the request
                                        Log.d("JSON_RESPONSE", "onResponse: " + response.toString());


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON_RESPONSE", "onResponse: " + error.toString());

                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                //put params

                                Map<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/json");
                                headers.put("Authorization", "key=AAAAkf0eEWs:APA91bEbMnR4mtaAzUVj1Ym1FbBMJe-KV51frq_8OSTlZCFmj_0GNGkRkdmkD_zoMr8J_KarjgvtHbDa9Lfy8LKFFhsUf7MxmytHG20kzqAie16YltJroM4Y9_VS0kfS09kaEmUpuwNA");

                                return headers;
                            }
                        };

                        // add this request to queue
                        requestQueue.add(jsonObjectRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        //hide search view, as we don't need it here
//        menu.findItem(R.id.search_all_mm).setVisible(false);
//        menu.findItem(R.id.add_post_mm).setVisible(false);
//        return super.onCreateOptionsMenu(menu);
//
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // goto previous activity
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        //set online
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //get timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());

        //set offline with last seen time stamp;
        checkOnlineStatus(timestamp);
        checkTypingStatus("noOne");
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        //set offline with last seen time stamp;
        checkOnlineStatus("online");
        super.onResume();
    }
}
