package com.thatchosenone.travelmakati.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.adapters.ChatListUserAdapter;
import com.thatchosenone.travelmakati.connection.ConnectivityReceiver;
import com.thatchosenone.travelmakati.connection.MyApplication;
import com.thatchosenone.travelmakati.models.ChatListModel;
import com.thatchosenone.travelmakati.models.ChatModel;
import com.thatchosenone.travelmakati.models.LeisureModel;
import com.thatchosenone.travelmakati.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String COMMON_TAG = "ChatFragment";
    private View chats;

    //firebase auth
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, leisureRef;
    FirebaseUser currentUser;

    RecyclerView rvChatListUser;

    List<ChatListModel> chatlistList;
    List<UserModel> userList;
    List<LeisureModel> leisureList;

    ChatListUserAdapter chatListAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chats = inflater.inflate(R.layout.fragment_chat, container, false);

        //inits views
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        rvChatListUser = chats.findViewById(R.id.cf_rv_chat_list_user);
        //rvChatListLeisure = chats.findViewById(R.id.cf_rv_chat_list_leisure);

        chatlistList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chatlist").child(currentUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlistList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatListModel chatlist = ds.getValue(ChatListModel.class);
                    chatlistList.add(chatlist);
                }
                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        checkConnection();
        return chats;
    }

    private void loadChats() {
        userList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserModel user = ds.getValue(UserModel.class);
                    for (ChatListModel chatlist : chatlistList) {
                        if (user.getUid() != null && user.getUid().equals(chatlist.getId())) {
                            userList.add(user);
                            break;
                        }
                    }
                    //adapter
                    chatListAdapter = new ChatListUserAdapter(getActivity(), userList);
                    //set adapter
                    rvChatListUser.setAdapter(chatListAdapter);
                    //set last message
                    for (int i = 0; i < userList.size(); i++) {
                        lastMessage(userList.get(i).getUid());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void lastMessage(String userID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String theLastMessage = "default";
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatModel chat = ds.getValue(ChatModel.class);
                    if (chat == null) {
                        continue;
                    }
                    String sender = chat.getSender();
                    String receiver = chat.getReceiver();
                    if (sender == null || receiver == null) {
                        continue;
                    }
                    if (chat.getReceiver().equals(currentUser.getUid())
                            && chat.getSender().equals(userID) ||
                            chat.getReceiver().equals(userID)
                            && chat.getSender().equals(currentUser.getUid())) {
                            theLastMessage = chat.getMessage();
                    }
                }

                chatListAdapter.setLastMessageMap(userID, theLastMessage);
                chatListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
//        MenuItem searchAll = menu.findItem(R.id.add_search_mm);
//        Intent intent = new Intent(getContext(), SearchAllLeisure.class);
//        searchAll.setIntent(intent);

        super.onCreateOptionsMenu(menu, inflater);
    }


    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        if (isConnected) {
        } else {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame, new InternetConnection());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(COMMON_TAG, "DiscoverFragment onSaveInstanceState");
    }

    @Override
    public void onNetworkChange(boolean inConnected) {
        showSnack(inConnected);
    }

}
