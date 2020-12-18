package com.thatchosenone.travelmakati.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.thatchosenone.travelmakati.R;

import java.util.ArrayList;
import java.util.List;

public class LeisureSearch extends AppCompatActivity {


    String name, address, image;

    RecyclerView searchList;
    RecyclerView.LayoutManager layoutManager;

    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference placeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leisure_search);

        firebaseDatabase = FirebaseDatabase.getInstance();
        placeList = firebaseDatabase.getReference().child("Leisure");

//        data = FirebaseDatabase.getInstance().getReference().child("Leisure").child("Hotels");
//        Ref = FirebaseDatabase.getInstance().getReference().child("Leisure").child("Hotels").getRef().getKey();

        searchList = (RecyclerView) findViewById(R.id.rv_search_list);
        layoutManager = new LinearLayoutManager(this);
        searchList.setLayoutManager(layoutManager);
//        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(searchList.getContext(), R.anim.mytransition);
//        searchList.setLayoutAnimation(controller);


        materialSearchBar = (MaterialSearchBar) findViewById(R.id.search_bar_ls);
        materialSearchBar.setHint("Enter your Places");
        //materialSearchBar.setSpeechMode(false);
//        loadSuggests();


        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }

                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
//            @Override
//            public void onSearchStateChanged(boolean enabled) {
//                if (!enabled) {
//                    searchList.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onSearchConfirmed(CharSequence text) {
//                startSearch(text.toString());
//
//            }
//
//            @Override
//            public void onButtonClicked(int buttonCode) {
//
//            }
//        });
//
//        loadAllLeisure();

//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchInput = tilText.getEditText().getText().toString();
//                onStart();
//
//            }
//        });
    }

//    private void loadAllLeisure() {
//        Query searchByName = placeList;
//        FirebaseRecyclerOptions<Leisure> leisureOption = new FirebaseRecyclerOptions.Builder<Leisure>()
//                .setQuery(searchByName, Leisure.class)
//                .build();
//        adapter = new FirebaseRecyclerAdapter<Leisure, SearchViewHolder>(leisureOption) {
//            @Override
//            protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull Leisure model) {
//                String key = getRef(position).getKey();
//                placeList.child(key).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            String name = String.valueOf(dataSnapshot.child("Name").getValue());
//                            String address = String.valueOf(dataSnapshot.child("Address").getValue());
//                            String dLat = String.valueOf(dataSnapshot.child("Latitude").getValue());
//                            String dLongi = String.valueOf(dataSnapshot.child("Longitude").getValue());
//                            String desc = String.valueOf(dataSnapshot.child("Description").getValue());
//                            String hours = String.valueOf((dataSnapshot.child("Operating Hours").getValue()));
//                            String web = String.valueOf(dataSnapshot.child("Website").getValue());
//                            String image = String.valueOf((dataSnapshot.child("Image").getValue()));
//                            String contact = String.valueOf(dataSnapshot.child("Contact Number").getValue());
//                            holder.tvName.setText(name);
//                            holder.tvAddress.setText(address);
//                            Picasso.get().load(image).fit().into(holder.ivPhoto);
//
//                            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent hotelDetails = new Intent(getApplicationContext(), AllLeisureDetails.class);
//                                    hotelDetails.putExtra("uID", key );
//                                    hotelDetails.putExtra("name", name);
//                                    hotelDetails.putExtra("lat", dLat);
//                                    hotelDetails.putExtra("longi", dLongi);
//                                    hotelDetails.putExtra("desc", desc);
//                                    hotelDetails.putExtra("add", address);
//                                    hotelDetails.putExtra("image", image);
//                                    hotelDetails.putExtra("hours", hours);
//                                    hotelDetails.putExtra("contact", contact);
//                                    hotelDetails.putExtra("web", web);
//                                    startActivity(hotelDetails);
//                                }
//                            });
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//
//            @NonNull
//            @Override
//            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_leisure_item, viewGroup, false);
//                return new SearchViewHolder(view);
//            }
//        };
//
//        adapter.startListening();
//        searchList.setAdapter(adapter);
//        searchList.getAdapter().notifyDataSetChanged();
//        searchList.scheduleLayoutAnimation();
//
//    }
//
//    private void startSearch(CharSequence text) {
//        Query searchByName = placeList.orderByChild("Name").equalTo(text.toString());
//        //Create a option query
//        FirebaseRecyclerOptions<Leisure> leisureOption = new FirebaseRecyclerOptions.Builder<Leisure>()
//                .setQuery(searchByName, Leisure.class)
//                .build();
//        searchAdapter = new FirebaseRecyclerAdapter<Leisure, SearchViewHolder>(leisureOption) {
//            @Override
//            protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull Leisure model) {
//                String key = getRef(position).getKey();
//                placeList.child(key).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            String name = String.valueOf(dataSnapshot.child("Name").getValue());
//                            String address = String.valueOf(dataSnapshot.child("Address").getValue());
//                            String dLat = String.valueOf(dataSnapshot.child("Latitude").getValue());
//                            String dLongi = String.valueOf(dataSnapshot.child("Longitude").getValue());
//                            String desc = String.valueOf(dataSnapshot.child("Description").getValue());
//                            String hours = String.valueOf((dataSnapshot.child("Operating Hours").getValue()));
//                            String web = String.valueOf(dataSnapshot.child("Website").getValue());
//                            String image = String.valueOf((dataSnapshot.child("Image").getValue()));
//                            String contact = String.valueOf(dataSnapshot.child("Contact Number").getValue());
//                            holder.tvName.setText(name);
//                            holder.tvAddress.setText(address);
//                            Picasso.get().load(image).fit().into(holder.ivPhoto);
//
//                            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent hotelDetails = new Intent(getApplicationContext(), AllLeisureDetails.class);
//                                    hotelDetails.putExtra("uID", key);
//                                    hotelDetails.putExtra("name", name);
//                                    hotelDetails.putExtra("lat", dLat);
//                                    hotelDetails.putExtra("longi", dLongi);
//                                    hotelDetails.putExtra("desc", desc);
//                                    hotelDetails.putExtra("add", address);
//                                    hotelDetails.putExtra("image", image);
//                                    hotelDetails.putExtra("hours", hours);
//                                    hotelDetails.putExtra("contact", contact);
//                                    hotelDetails.putExtra("web", web);
//                                    startActivity(hotelDetails);
//                                }
//                            });
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_leisure_item, viewGroup, false);
//                return new SearchViewHolder(view);
//            }
//        };
//
//        searchAdapter.startListening();
//        searchList.setAdapter(searchAdapter);
//    }
//
//    private void loadSuggests() {
//        placeList.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String nameSuggest = String.valueOf(snapshot.child("Name").getValue());
//                    suggestList.add(nameSuggest);
//                }
//                materialSearchBar.setLastSuggestions(suggestList);
//            }
//
////                    String value = snapshot.getKey();
////                    final DatabaseReference newRef = placeList.child(value);
////                    newRef.addListenerForSingleValueEvent(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
////                                String value2 = String.valueOf(snapshot1.child("Name").getValue());
////                                suggestList.add(value2);
////                            }
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                        }
////                    });
//            //String value = String.valueOf(snapshot.child("Name").getValue());
//            //Leisure leisure = snapshot.getValue(Leisure.class);
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (adapter != null) {
//            adapter.startListening();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//
//        if (adapter != null) {
//            adapter.stopListening();
//        }
//        if (searchAdapter != null) {
//            searchAdapter.stopListening();
//        }
//        super.onStop();
//    }

//    private class SearchViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tvName, tvAddress;
//        ImageView ivPhoto;
//
//
//        public SearchViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvName = itemView.findViewById(R.id.name_leisure);
//            tvAddress = itemView.findViewById(R.id.address_leisure);
//            ivPhoto = itemView.findViewById(R.id.image_leisure);
//        }
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        options = new FirebaseRecyclerOptions.Builder<Leisure>()
//                .setQuery(Ref.orderByChild("Name").startAt(searchInput).endAt(searchInput + "\uf8ff"), Leisure.class)
//                .setLifecycleOwner(this)
//                .build();
//        adapter = new FirebaseRecyclerAdapter<Leisure, SearchViewHolder>(options) {
//            @NonNull
//            @Override
//            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_leisure_item, viewGroup, false);
//                return new SearchViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull Leisure model) {
//                String keys = getRef(position).getKey();
//                Ref.child(keys).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            String name = String.valueOf(dataSnapshot.child("Name").getValue());
//                            String address = String.valueOf(dataSnapshot.child("Address").getValue());
//                            String dLat = String.valueOf(dataSnapshot.child("Latitude").getValue());
//                            String dLongi = String.valueOf(dataSnapshot.child("Longitude").getValue());
//                            String desc = String.valueOf(dataSnapshot.child("Description").getValue());
//                            String hours = String.valueOf((dataSnapshot.child("Operating Hours").getValue()));
//                            String web = String.valueOf(dataSnapshot.child("Website").getValue());
//                            String image = String.valueOf((dataSnapshot.child("Image").getValue()));
//                            String contact = String.valueOf(dataSnapshot.child("Contact Number").getValue());
//                            holder.tvName.setText(name);
//                            holder.tvAddress.setText(address);
//                            Picasso.get().load(image).fit().into(holder.ivPhoto);
//
//                            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent hotelDetails = new Intent(getApplicationContext(), AllLeisureDetails.class);
//                                    hotelDetails.putExtra("uID", keys);
//                                    hotelDetails.putExtra("name", name);
//                                    hotelDetails.putExtra("lat", dLat);
//                                    hotelDetails.putExtra("longi", dLongi);
//                                    hotelDetails.putExtra("desc", desc);
//                                    hotelDetails.putExtra("add", address);
//                                    hotelDetails.putExtra("image", image);
//                                    hotelDetails.putExtra("hours", hours);
//                                    hotelDetails.putExtra("contact", contact);
//                                    hotelDetails.putExtra("web", web);
//                                    startActivity(hotelDetails);
//                                }
//                            });
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        };
//        searchList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        adapter.startListening();
//        searchList.setAdapter(adapter);
//    }
//
//    private class SearchViewHolder extends RecyclerView.ViewHolder {
//        TextView tvName, tvAddress;
//        ImageView ivPhoto;
//
//        public SearchViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvName = itemView.findViewById(R.id.name_leisure);
//            tvAddress = itemView.findViewById(R.id.address_leisure);
//            ivPhoto = itemView.findViewById(R.id.image_leisure);
//        }
//    }
}
