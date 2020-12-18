package com.thatchosenone.travelmakati.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.ChatModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Myholder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ChatModel> chatList;
    String imageUrl;

    FirebaseUser firebaseUser;


    public ChatAdapter(Context context, List<ChatModel> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }


    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layouts: row_chat_left_item for receiver, row_chat_right_ for sender
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right_item, parent, false);
            return new Myholder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left_item, parent, false);
            return new Myholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, final int i) {
        //get data
        String message = chatList.get(i).getMessage();
        String timeStamp = chatList.get(i).getTimestamp();

        //boolean seend = chatList.get(i).isSeen();

        //convert time stamp to dd/mm/yyyy hh:mm: am/pm
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();


        //set data
        holder.tvMessage.setText(message);
        holder.tvTime.setText(dateTime);


        try {
            Picasso.get().load(imageUrl).into(holder.ivProfile);
        } catch (Exception e) {

        }


        //set seen/delivered start of message
        if (i == chatList.size() - 1) {
            if (chatList.get(i).isSeen()) {
                holder.tvIsSeen.setText("Seen");
            } else {
                holder.tvIsSeen.setText("Delivered");
            }
        } else {
            holder.tvIsSeen.setVisibility(View.GONE);
        }

//        holder.llMessage.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return true;
//            }
//        });

        //click to show delete dialog
        holder.llMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show delete dialog confirm dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this message?");
                //delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(i);
                    }
                });

                //cancel delete
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dismis dialog
                        dialog.dismiss();
                    }
                });

                //create and show dialog
                builder.create().show();

            }
        });


    }

    private void deleteMessage(int position) {
        //String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        /*Logic
         * Get timestamp of the clicked message
         * Compare the timestamp of the clicked message with all messages in Chats
         * Where both values matches delete that message*/
        String msgTimeStamp = chatList.get(position).getTimestamp();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = dbRef.orderByChild("timestamp").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    /*We can do one of two things here
                     * 1) Remove the message from chats
                     * 2) Set the value of message "This message was deleted..."
                     * so do whatever you want"*/
                    if (ds.child("sender").getValue().equals(firebaseUser.getUid())) {
                        // 1)Remove the message from chats
                        //ds.getRef().removeValue();

                        // 2)Set the value of message  "This message was deleted..."
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("message", "This message was deleted...");
                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context, "message deleted...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "You can delete only your messages...", Toast.LENGTH_SHORT);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed in user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }

    //view holder class
    class Myholder extends RecyclerView.ViewHolder {

        //views
        CircleImageView ivProfile;
        TextView tvMessage, tvTime, tvIsSeen;
        LinearLayout llMessage; // for click listener to show delete

        public Myholder(@NonNull View itemView) {
            super(itemView);

            //init views
            ivProfile = itemView.findViewById(R.id.rcl_civ_profile);
            tvMessage = itemView.findViewById(R.id.rcl_tv_message);
            tvTime = itemView.findViewById(R.id.rcl_tv_time);
            tvIsSeen = itemView.findViewById(R.id.rcl_tv_isSeen);
            llMessage = itemView.findViewById(R.id.rcl_ll_message);

        }
    }

}
