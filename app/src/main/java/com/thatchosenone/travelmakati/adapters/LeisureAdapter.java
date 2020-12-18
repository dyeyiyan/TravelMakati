package com.thatchosenone.travelmakati.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thatchosenone.travelmakati.activities.AllLeisureDetails;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.LeisureModel;

import java.util.HashMap;
import java.util.List;

public class LeisureAdapter extends RecyclerView.Adapter<LeisureAdapter.Myholder> {

    Context context;
    List<LeisureModel> leisureList;
    private DatabaseReference leisureRef;
    private DatabaseReference mostViewRef, userCount;
    String myUID;
    //boolean addFav = false;


    public LeisureAdapter(Context context, List<LeisureModel> leisureList) {
        this.context = context;
        this.leisureList = leisureList;
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        leisureRef = FirebaseDatabase.getInstance().getReference().child("leisures");
        mostViewRef = FirebaseDatabase.getInstance().getReference().child("leisureViews");
        userCount = FirebaseDatabase.getInstance().getReference().child("total");
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_rate_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_places_max_item, parent, false);
        return new LeisureAdapter.Myholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        //get data
        String lphoto = leisureList.get(position).getLeisurePhoto();
        String lname = leisureList.get(position).getName();
        String luid = leisureList.get(position).getLeisureID();
        String laddress = leisureList.get(position).getAddress();
        String ldesc = leisureList.get(position).getDescription();
        String ltotalviews = leisureList.get(position).getTotalViews();

        //set data
        holder.tvName.setText(lname);
        holder.tvAddress.setText(laddress);
        holder.tvDesc.setText(ldesc);

        try {
            Picasso.get().load(lphoto).into(holder.ivLPic);
        } catch (Exception e) {

        }
        //setFav(holder, luid);

//        ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int total = 0;
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String luid = "" + ds.child("luid").getValue();
//                    String pnt = "" + ds.child("point").getValue();
//                    int npnt = Integer.parseInt(pnt);
//                    if (luid.equals(leisureList.get(position).getLuid())) {
//                        total = total + npnt;
//                        if (total >= 1) {
//                            holder.tvReviews.setText("Based on " + "(" + total + ") " + "Reviews");
//                        } else {
//                            holder.tvReviews.setText("Based on (0) Review");
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        holder.ivFav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String lid = leisureList.get(position).getLuid();
//                addFav = true;
//                //get id of the post clicked
//                //final String postIde = postList.get(position).getDate_time();
//                favRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (addFav) {
//                            if (dataSnapshot.child(myUID).hasChild(lid)) {
//                                //already liked, so remove like
//                                favRef.child(myUID).child(lid).removeValue();
//                                Toast.makeText(context, "Deleted to your favorites", Toast.LENGTH_SHORT).show();
//                                addFav = false;
//
//                            } else {
//                                HashMap<String, Object> leisureMap = new HashMap<>();
//                                //put post info
//                                leisureMap.put("id", lid);
//                                //path to store post data
//                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Favorites");
//                                //put data in this ref
//                                ref.child(myUID).child(lid).setValue(leisureMap);
//                                Toast.makeText(context, "Added to your favorites", Toast.LENGTH_SHORT).show();
//                                //favRef.child(firebaseUser.getUid()).child(lUID).child("favorites").setValue("true");
//                                addFav = false;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 1. if leisureRef is not success throw an error
                 * 2. else query leisureRef where leisureID is equal to luid
                 * 3. check if snapshot is exist
                 * 4. then iterate the data from dataSnapshot
                 * 5. get the data of points and price from ds variable
                 * 6. after that save to database
                 */
                //Toast.makeText(context,"UID: "+lDateTime, Toast.LENGTH_SHORT).show();
                //int tview = Integer.parseInt(leisureList.get(position).getTotalViews());
                Intent intent = new Intent(context, AllLeisureDetails.class);
                intent.putExtra("luid", luid);
                context.startActivity(intent);

                leisureRef.orderByChild("leisureID").equalTo(luid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String category = "" + ds.child("category").getValue();
                                String price = "" + ds.child("price").getValue();

                                String timeStamp = String.valueOf(System.currentTimeMillis());
                                HashMap<String, Object> mostMap = new HashMap<>();
                                //put post info
                                mostMap.put("points", "1");
                                mostMap.put("uID", myUID);
                                mostMap.put("timeStamp", timeStamp);
                                mostMap.put("category", category);
                                mostMap.put("price", price);
                                //recentViewedRef.child(myUID).child(luid).setValue(leisureMap);
                                //mostViewRef.child(luid).child(myUID).setValue("" + (tview + 1));
                                mostViewRef.child(luid).child(timeStamp).setValue(mostMap);

                                storeTotalView(luid, price);
                                storeUserTotalView(luid, price, myUID);
                                storeSumViews();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private void storeSumViews() {
        userCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int total = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String points = "" + ds.child("totalViews").getValue();
                            int nrates = Integer.parseInt(points);
                            total = total + nrates;
                        }
                    HashMap<String, Object> totalViewMap = new HashMap<>();
                    //put post info
                    totalViewMap.put("allViews", "" + total);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("views");
                    ref.child("total").updateChildren(totalViewMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void storeUserTotalView(String luid, String price, String myUID) {

        /* 1. if mostViewRef is not success throw an error
         * 2. else query to get the dataSnapshot
         * 3. check if snapshot is exist
         * 4. set datatype int total as 0
         * 5  then iterate the data from dataSnapshot
         * 6. after that get the unique key of dataSnapshot
         * 7. once you get the key, iterate the data from ds
         * 8. get the data of points and uids from ds variable
         * 9. convert the value of points to int
         * 10. check if the luid is equal to key and myUID is equal to uids
         * 11. then do a calculation of total
         * 12. after that save to database
         */
        mostViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int total = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        for (DataSnapshot dss : ds.getChildren()) {
                            String points = "" + dss.child("points").getValue();
                            String uids = "" + dss.child(("uID")).getValue();
                            int nrates = Integer.parseInt(points);
                            if(uids.equals(myUID) && luid.equals(key)){
                                total = total + nrates;
                            }
                        }
                        Log.d("TAG",  "" + total);
                    }

                    HashMap<String, Object> totalViewMap = new HashMap<>();
                    //put post info
                    totalViewMap.put("totalViews", "" + total);
                    totalViewMap.put("leisureID", "" + luid);
                    totalViewMap.put("price", "" + price);
                    totalViewMap.put("uID", "" + myUID);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userTotal");
                    ref.child(myUID).child(luid).updateChildren(totalViewMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void storeTotalView(String luid, String price) {
        /* 1. if mostViewRef is not success throw an error
         * 2. else query to get the dataSnapshot
         * 3. check if snapshot is exist
         * 4. set datatype int total as 0
         * 5  then iterate the data from dataSnapshot
         * 6. after that get the unique key of dataSnapshot
         * 7. once you get the key, iterate the data from ds
         * 8. get the data of points from ds variable
         * 9. convert the value of points to int
         * 10. check if the luid is equal to key
         * 11. then do a calculation of total
         * 12. after that save to database
         */
        mostViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int total = 0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        for (DataSnapshot dss : ds.getChildren()) {
                            String points = "" + dss.child("points").getValue();
                            //String luids = "" + dss.child(("leisureID")).getValue();
                            int nrates = Integer.parseInt(points);
                            if (luid.equals(key)) {
                                total = total + nrates;
                            }
                        }
                        Log.d("TAG",  "" + total);
                    }

                    HashMap<String, Object> totalViewMap = new HashMap<>();
                    //put post info
                    totalViewMap.put("totalViews", "" + total);
                    totalViewMap.put("leisureID", "" + luid);
                    totalViewMap.put("price", "" + price);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("total");
                    ref.child(luid).updateChildren(totalViewMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setFav(Myholder holder, String luid) {
//        favRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(myUID).hasChild(luid)) {
//                    //user has liked this post
//                    /*To indicate that the post if like by this (Signed IN) user
//                     * Change drawable left icon of like button
//                     * change text of like button from "Like" to "Like"*/
//                    //holder.ivFav.setImageResource(R.drawable.ic_fav_red);
//                } else {
//                    /*To indicate that the post if like by this (Signed IN) user
//                     * Change drawable left icon of like button
//                     * change text of like button from "Like" to "Like"*/
//                    //holder.ivFav.setImageResource(R.drawable.ic_fav_blue);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return leisureList.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        //View from row_rate_post_fragement xml
        ImageView ivLPic;
        TextView tvDesc, tvAddress, tvName;

        public Myholder(@NonNull View itemView) {
            super(itemView);

            //init view
            ivLPic = itemView.findViewById(R.id.rpi_iv_leisure);
            tvName = itemView.findViewById(R.id.rpi_tv_name);
            tvAddress = itemView.findViewById(R.id.rpi_tv_address);
            tvDesc = itemView.findViewById(R.id.rpi_tv_desc);
        }
    }
}
