package com.thatchosenone.travelmakati.adapters;

import android.content.Context;
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
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.LeisureModel;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.Myholder> {


    Context context;
    List<LeisureModel> leisureList;
    private DatabaseReference favRef; //for likes database node
    private DatabaseReference ratingRef; //reference of posts
    private DatabaseReference leisureRef;
    String myUID;
    boolean addFav = false;


    public FavAdapter(Context context, List<LeisureModel> leisureList) {
        this.context = context;
        this.leisureList = leisureList;
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        favRef = FirebaseDatabase.getInstance().getReference().child("Favorites");
        ratingRef = FirebaseDatabase.getInstance().getReference().child("Ratings");
        leisureRef = FirebaseDatabase.getInstance().getReference().child("Leisure");
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_rate_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_place_item, parent, false);
        return new FavAdapter.Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        //get data
        //String lphoto = leisureList.get(position).getPhoto();
        String lname = leisureList.get(position).getName();
        String lcategory = leisureList.get(position).getCategory();
//        String lstreet = leisureList.get(position).getStreet();
//        String lprice = leisureList.get(position).getPrice();
//        String luid = leisureList.get(position).getLuid();
//        String ltotal = leisureList.get(position).getTotal_rate();

        //set data
        holder.tvLName.setText(lname);
        holder.tvLCategory.setText("Category: " + lcategory);
//        holder.tvLAddress.setText("Address: " + lstreet);
//        holder.tvLPrice.setText("Price starts at: " + "Php. " + lprice);
//        float ntotal = Float.parseFloat(ltotal);
//        holder.mrbLRating.setRating(ntotal);
//
//        try {
//            Picasso.get().load(lphoto).into(holder.ivLPic);
//        } catch (Exception e) {
//
//        }
//
//        setFav(holder, luid);


        ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String luid = "" + ds.child("luid").getValue();
                    String pnt = "" + ds.child("point").getValue();
                    int npnt = Integer.parseInt(pnt);
//                    if (luid.equals(leisureList.get(position).getLuid())) {
//                        total = total + npnt;
//                        if (total >= 1) {
//                            holder.tvReviews.setText("Based on " + "(" + total + ") " + "Reviews");
//                        } else {
//                            holder.tvReviews.setText("Based on (0) Review");
//                        }
//                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String lid = leisureList.get(position).getLuid();
//                //get id of the post clicked
//                //final String postIde = postList.get(position).getDate_time();
//                favRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        favRef.child(myUID).child(lid).removeValue();
//                        Toast.makeText(context, "Deleted to your favorites", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"UID: "+lDateTime, Toast.LENGTH_SHORT).show();
//                final String lid = leisureList.get(position).getLuid();
//                Intent intent = new Intent(context, AllLeisureDetails.class);
//                intent.putExtra("luid", lid);
//                context.startActivity(intent);
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
//                    holder.ivFav.setImageResource(R.drawable.ic_fav_red);
                } else {
                    /*To indicate that the post if like by this (Signed IN) user
                     * Change drawable left icon of like button
                     * change text of like button from "Like" to "Like"*/
//                    holder.ivFav.setImageResource(R.drawable.ic_fav_blue);
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
        TextView tvLName, tvReviews, tvLCategory, tvLPrice, tvLAddress;
        MaterialRatingBar mrbLRating;
        ImageView ivFav;

        public Myholder(@NonNull View itemView) {

            super(itemView);
            //init view
//            ivLPic = itemView.findViewById(R.id.rli_iv_lpic);
//            tvLName = itemView.findViewById(R.id.rli_tv_lname);
//            tvLCategory = itemView.findViewById(R.id.rli_tv_lcategory);
//            tvLPrice = itemView.findViewById(R.id.rli_tv_lprice);
//            tvLAddress = itemView.findViewById(R.id.rli_tv_laddress);
//            mrbLRating = itemView.findViewById(R.id.rli_mrb_lrating);
//            ivFav = itemView.findViewById(R.id.rli_iv_fav);
//            tvReviews = itemView.findViewById(R.id.rli_tv_reviews);
        }
    }

}


