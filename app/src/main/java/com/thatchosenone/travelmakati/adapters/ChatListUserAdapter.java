package com.thatchosenone.travelmakati.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.UserModel;
import com.thatchosenone.travelmakati.activities.Chat;

import java.util.HashMap;
import java.util.List;

public class ChatListUserAdapter extends RecyclerView.Adapter<ChatListUserAdapter.MyHolder> {

    Context context;
    List<UserModel> userList; //get user info


    private HashMap<String, String> lastMessageMap;

//    //constructor
//    public ChatListUserAdapter(Context context, List<UserModel> userList) {
//        this.context = context;
//        this.userList = userList;
//        lastMessageMap = new HashMap<>();
//    }


    public ChatListUserAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
        lastMessageMap = new HashMap<>();
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_chatlist_item
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        String hisUID = userList.get(position).getUid();
        String userImage = userList.get(position).getDisplay_photo();
        String userName = userList.get(position).getName();
        String lastMessage = lastMessageMap.get(hisUID);
        //String status = userList.get(position).getDisplay_photo();


        holder.tvName.setText(userName);
        //set data
        if (lastMessage == null || lastMessage.equals("default")) {
            holder.tvLastMessage.setVisibility(View.GONE);
        } else {
            holder.tvLastMessage.setVisibility(View.VISIBLE);
            holder.tvLastMessage.setText(lastMessage);
        }

        try {
            Picasso.get().load(userImage).placeholder(R.drawable.default_profile_male).into(holder.ivProfile);
        } catch (Exception e) {

        }

//        if(userList.get(position).getOnlineStatus().equals("online")){
//            holder.ivOnlineStatus.setImageResource(R.drawable.circle_online);
//        }
//        else{
//            holder.ivOnlineStatus.setImageResource(R.drawable.circle_offline);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start chats activity with that user
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("hisUID", hisUID);
                context.startActivity(intent);

            }
        });
    }

    public void setLastMessageMap(String userID, String lastMessage) {
        lastMessageMap.put(userID, lastMessage);
    }

    @Override
    public int getItemCount() {
        return userList.size(); // size of the list
    }

    class MyHolder extends RecyclerView.ViewHolder {
        //views for xml
        ImageView ivProfile, ivOnlineStatus;
        TextView tvName, tvLastMessage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init views
            ivProfile = itemView.findViewById(R.id.rc_civ_profile_pic);
            ivOnlineStatus = itemView.findViewById(R.id.rc_civ_online_status);
            tvName = itemView.findViewById(R.id.rc_tv_name);
            tvLastMessage = itemView.findViewById(R.id.rc_tv_last_message);

        }
    }
}
