package com.thatchosenone.travelmakati.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.LeisureModel;
import com.thatchosenone.travelmakati.activities.AllLeisureDetails;

import java.util.List;

public class RecentLeisureAdapter extends RecyclerView.Adapter<RecentLeisureAdapter.Myholder> {

    Context context;
    List<LeisureModel> leisureList;
    private DatabaseReference favRef; //for likes database node
    private DatabaseReference recentViewedRef;
    private DatabaseReference leisureRef;
    String myUID;
    boolean addFav = false;


    public RecentLeisureAdapter(Context context, List<LeisureModel> leisureList) {
        this.context = context;
        this.leisureList = leisureList;
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        favRef = FirebaseDatabase.getInstance().getReference().child("Favorites");
        recentViewedRef = FirebaseDatabase.getInstance().getReference().child("RecentView");
        leisureRef = FirebaseDatabase.getInstance().getReference().child("Leisure");
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_rate_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_places_item, parent, false);
        return new RecentLeisureAdapter.Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        //get data
        //String lphoto = leisureList.get(position).getPhoto();
        String lname = leisureList.get(position).getName();
        //String lstreet = leisureList.get(position).getStreet();
        //String luid = leisureList.get(position).getLuid();

        //set data
//        holder.tvLName.setText(lname);
//        holder.tvLAddress.setText(lstreet);


//        try {
//            Picasso.get().load(lphoto).into(holder.ivLPic);
//        } catch (Exception e) {
//
//        }

        //setFav(holder, luid);

        holder.ibtnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // final String lid = leisureList.get(position).getLuid();
                addFav = true;
                //get id of the post clicked
                //final String postIde = postList.get(position).getDate_time();
                favRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (addFav) {
//                            if (dataSnapshot.child(myUID).hasChild(lid)) {
//                                //already liked, so remove like
//                                favRef.child(myUID).child(lid).removeValue();
//                                Toast.makeText(context, "Deleted to your favorites", Toast.LENGTH_SHORT).show();
//                                addFav = false;
//
//                            } else {
//                                //put post info
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
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AllLeisureDetails.class);
               // intent.putExtra("luid", luid);
                context.startActivity(intent);

            }
        });

    }

    private void setFav(Myholder holder, String luid) {
        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(myUID).hasChild(luid)) {
                    //user has liked this post
                    /*To indicate that the post if like by this (Signed IN) user
                     * Change drawable left icon of like button
                     * change text of like button from "Like" to "Like"*/
                   // holder.ibtnFav.setImageResource(R.drawable.ic_fav_red);
                } else {
                    /*To indicate that the post if like by this (Signed IN) user
                     * Change drawable left icon of like button
                     * change text of like button from "Like" to "Like"*/
                    //holder.ibtnFav.setImageResource(R.drawable.ic_fav_blue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return leisureList.size();
    }


    class Myholder extends RecyclerView.ViewHolder {

        //View from row_rate_post_fragement xml
        ImageView ivLPic;
        TextView tvLName, tvLAddress;
        ImageButton ibtnFav;

        public Myholder(@NonNull View itemView) {

            super(itemView);
            //init view
//            ivLPic = itemView.findViewById(R.id.rrl_civ_lpic);
//            tvLName = itemView.findViewById(R.id.rrl_tv_lname);
//            tvLAddress = itemView.findViewById(R.id.rrl_tv_ladd);
//            ibtnFav = itemView.findViewById(R.id.rrl_ibtn_fav);
        }
    }
}
