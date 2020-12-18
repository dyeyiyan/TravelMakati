package com.thatchosenone.travelmakati.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.connection.ConnectivityReceiver;
import com.thatchosenone.travelmakati.connection.MyApplication;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String COMMON_TAG = "UserFragment";
    private View userList;
    RecyclerView rvUserList;
    //UserAdapter userAdapter; //adapterUser
    List<UserModel> userModelLists; //userList
    androidx.appcompat.widget.SearchView searchView;

    public UserFragment() {
        // Required empty public constructor


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userList = inflater.inflate(R.layout.fragment_user, container, false);

        //init recyclerview
        rvUserList = (RecyclerView) userList.findViewById(R.id.flu_rv_users);

        //set it's properties
        rvUserList.setHasFixedSize(true);
        rvUserList.setLayoutManager(new LinearLayoutManager(getActivity()));

        //init user list
        userModelLists = new ArrayList<>();

        //get all user
        getAllUser();


        checkConnection();
        return userList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    //get all user
    private void getAllUser() {
        // get current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //get path of database name "UserModel" contains user info
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");

        //get all data from path
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModelLists.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserModel userModel = ds.getValue(UserModel.class);

                    //get all userModel's except currently signed in userModel
                    if (!userModel.getUid().equals(firebaseUser.getUid())) {
                        userModelLists.add(userModel);
                    }
                    //adapter
//                    userAdapter = new UserAdapter(getActivity(), userModelLists);
//                    //set adapter to recycler view
//                    rvUserList.setAdapter(userAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        Log.i(COMMON_TAG, "UserFragment onSaveInstanceState");
    }

    @Override
    public void onNetworkChange(boolean inConnected) {
        showSnack(inConnected);
    }



//    /*Inflate option menu*/
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        //inflating menu
//        inflater.inflate(R.menu.main_menu, menu);
//
//        menu.findItem(R.id.add_post_mm).setVisible(false);
//
//        //Searchview
//        MenuItem menuItem = menu.findItem(R.id.search_all_mm);
//        searchView = (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(menuItem);
//
//        //search listener
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                //called when user press search button from keyboard
//                //if search query is not empty then search
//                if (!TextUtils.isEmpty(s.trim())) {
//                    //search text contains text, search it
//                    searchUsers(s);
//                } else {
//                    getAllUser();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                //called whenever user press any single letter
//                //if search query is not empty then search
//                if (!TextUtils.isEmpty(s.trim())) {
//                    //search text contains text, search it
//                    searchUsers(s);
//                } else {
//                    getAllUser();
//                }
//                return false;
//            }
//        });
//
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    //search all users
//    private void searchUsers(String query) {
//        // get current user
//        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        //get path of database name "UserModel" contains user info
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
//
//        //get all data from path
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userModelLists.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    UserModel userModel = ds.getValue(UserModel.class);
//                    /*Condition to fulfil search
//                     * 1) UserModel not current userModel
//                     * 2) The userModel name or email contains text entered in search view (case insensitive) */
//                    //get all searched userModel's except currently signed in userModel
//                    if (!userModel.getUid().equals(firebaseUser.getUid())) {
//                        if (userModel.getName().toLowerCase().contains(query.toLowerCase())
//                                || userModel.getEmail().toLowerCase().contains(query.toLowerCase()))
//                        {
//                            userModelLists.add(userModel);
//                        }
//                    }
//                    //adapter
//                    userAdapter = new UserAdapter(getActivity(), userModelLists);
//                    //refresh adapter
//                    userAdapter.notifyDataSetChanged();
//                    //set adapter to recycler view
//                    rvUserList.setAdapter(userAdapter);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }

}
