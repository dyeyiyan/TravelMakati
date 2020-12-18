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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.connection.ConnectivityReceiver;
import com.thatchosenone.travelmakati.connection.MyApplication;
import com.thatchosenone.travelmakati.adapters.LeisureAdapter;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.FavModel;
import com.thatchosenone.travelmakati.models.LeisureModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String COMMON_TAG = "FavFragment";
    private View fav;

    RecyclerView rvfavorite;
    Spinner sList;
    String listItem, key;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference favRef, leisureRef;
    FirebaseUser firebaseUser;

    List<LeisureModel> leisureList;
    List<FavModel> favList;
    LeisureAdapter leisureAdapter;

    String approved = "approved";

    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fav = inflater.inflate(R.layout.fragment_fav, container, false);

        rvfavorite = fav.findViewById(R.id.ff_rv_fav);
        sList = fav.findViewById(R.id.ff_s_fav);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        leisureRef = firebaseDatabase.getReference("Leisure");
        favRef = firebaseDatabase.getReference("Favorites");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recycler view
        rvfavorite.setLayoutManager(layoutManager);

        //init post list
        favList = new ArrayList<>();
        list();
        checkConnection();
        return fav;
    }

    private void list() {
        List<String> categories = new ArrayList<>();
        categories.add(0, "All");
        categories.add("Hotels");
        categories.add("Malls");
        categories.add("Parks");
        categories.add("Nightlife");
        categories.add("Restaurant");
        categories.add("Museums");
        categories.add("Art Galleries");
        categories.add("Cafe / Tea Shop");

        //Style and populate the spinner
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);

        //Dropdown layout stylea
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Attaching array adapter to spinner
        sList.setAdapter(arrayAdapter);

        sList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("All")) {
                    //get all data from thi3t6rs ref
                    favRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            favList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                FavModel fav = ds.getValue(FavModel.class);
                                favList.add(fav);
                            }
                            loadAllFav();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
//                    Intent intent = new Intent(LeisureAdvertisement.this, Hotels.class);
//                    startActivity(intent);
                    listItem = parent.getItemAtPosition(position).toString();
                    loadListFav(listItem);
                    // Show selected item
                    //Toast.makeText(parent.getContext(), "Selected: " + listItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void loadAllFav() {
        leisureList = new ArrayList<>();
        leisureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leisureList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);

                    for (FavModel fav : favList) {
//                        if (leisureModel.getLuid() != null && leisureModel.getLuid().equals(fav.getId())) {
//                            leisureList.add(leisureModel);
//                            break;
//                        }
                    }
                    //adapter
                    leisureAdapter = new LeisureAdapter(getContext(), leisureList);
                    //set adapter
                    rvfavorite.setAdapter(leisureAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadListFav(String listItem) {

        leisureList = new ArrayList<>();
        leisureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leisureList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);
                    for (FavModel fav : favList) {
//                        if (leisureModel.getLuid() != null && leisureModel.getLuid().equals(fav.getId())) {
//                            if (leisureModel.getCategory().equals(listItem)) {
//                                leisureList.add(leisureModel);
//                                break;
//                            }
//                        }
                    }
                    //adapter
                    leisureAdapter = new LeisureAdapter(getContext(), leisureList);
                    //set adapter
                    rvfavorite.setAdapter(leisureAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Favorites");
//        //get all data from thi3t6rs ref
//        ref.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                postList.clear();
//                for (DataSnapshot dss : dataSnapshot.getChildren()) {
//                    LeisureModel leisureModel = dss.getValue(LeisureModel.class);
//                    if (leisureModel.getCategory().equals(listItem)) {
//                        postList.add(leisureModel);
//                    }
//                }
//                //adapter
//                postAdapter = new FavAdapter(getContext(), postList);
//                postAdapter.notifyDataSetChanged();
//                //set adapter to recyclerview
//                rvfavorite.setAdapter(postAdapter);
//                //rvWhats.setAdapter(postAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //in case of error
//                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
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


    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(COMMON_TAG, "FavFragment onSaveInstanceState");
    }

    @Override
    public void onNetworkChange(boolean inConnected) {
        showSnack(inConnected);
    }

}

