package com.thatchosenone.travelmakati.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.activities.AddRate;
import com.thatchosenone.travelmakati.activities.ThereProfile;
import com.thatchosenone.travelmakati.models.RatePostModel;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RatePostAdapter extends RecyclerView.Adapter<RatePostAdapter.Myholder> {

    Context context;
    List<RatePostModel> postList;
    String myUID;

    private DatabaseReference likesRef; //for likes database node
    private DatabaseReference postsRef; //reference of posts

    boolean mProcessLike = false;

    public RatePostAdapter(Context context, List<RatePostModel> postList) {
        this.context = context;
        this.postList = postList;
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_rate_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_rate_posted, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        //get data
        final String uid = postList.get(position).getUid();
        String uEmail = postList.get(position).getuEmail();
        String uName = postList.get(position).getuName();
        String pTitle = postList.get(position).getpTitle();
        String uDp = postList.get(position).getuDp();
        final String pID = postList.get(position).getpID();
        String pDescription = postList.get(position).getpDescription();
        final String pImage = postList.get(position).getpImage();
        String pTimestamp = postList.get(position).getpTime();
        String pLikes = postList.get(position).getpLikes(); //contains total number of likes for  a post


        //conver t timestamp to dd/mm/yyyy hh:mm:pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimestamp));
        String pTime = DateFormat.format("dd/mm/yyyy hh:mm:aa", calendar).toString();

        //set data
        holder.uTVName.setText(uName);
        holder.pTVTIme.setText(pTime);
        holder.pTVTitle.setText(pTitle);
        holder.pTVDescription.setText(pDescription);
        holder.pTVLikes.setText("(" + pLikes + ") " + "Likes"); // e.g 100 likes

        setLikes(holder, pID);

        //handle click button
        holder.ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptions(holder.ibMore, uid, myUID, pID, pImage);
            }
        });

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get total number of like for the post, whose like button clicked
                //if currently signed in user has not liked it before
                ///increase value by 1, otherwise decrease value by 1
                int pLikes = Integer.parseInt(postList.get(position).getpLikes());
                mProcessLike = true;
                //get id of the post clicked
                final String postIde = postList.get(position).getpID();
                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (mProcessLike) {
                            if (dataSnapshot.child(postIde).hasChild(myUID)) {
                                //already liked, so remove like
                                postsRef.child(postIde).child("pLikes").setValue("" + (pLikes - 1));
                                likesRef.child(postIde).child(myUID).removeValue();
                                mProcessLike = false;
                            } else {
                                //not liked, like it
                                postsRef.child(postIde).child("pLikes").setValue("" + (pLikes + 1));
                                likesRef.child(postIde).child(myUID).setValue("Liked");
                                mProcessLike = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();

            }
        });

        holder.llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Click to go to ThereProfile with uid, this uid is of clicked user
                 * which will be used to show user specific data/posts*/
                Intent intent = new Intent(context, ThereProfile.class);
                intent.putExtra("uid", uid);
                context.startActivity(intent);
            }
        });


        //set user dp
//        try {
//            Picasso.get().load(uDp).placeholder(R.drawable.ic_default_img_blue).into(holder.uIVPic);
//        } catch (Exception e) {
//
//        }

        if (pImage.equals("noImage")) {
            //hide image
            holder.pIVPost.setVisibility(View.GONE);
        } else {
            //show image
            holder.pIVPost.setVisibility(View.VISIBLE);
            //set post image
            try {
                Picasso.get().load(pImage).into(holder.pIVPost);
            } catch (Exception e) {

            }
        }


    }

    private void setLikes(Myholder holder, String postKey) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postKey).hasChild(myUID)) {
                    //user has liked this post
                    /*To indicate that the post if like by this (Signed IN) user
                     * Change drawable left icon of like button
                     * change text of like button from "Like" to "Like"*/
                   // holder.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_blue, 0, 0, 0);
                    holder.btnLike.setText("Liked");
                } else {
                    /*To indicate that the post if like by this (Signed IN) user
                     * Change drawable left icon of like button
                     * change text of like button from "Like" to "Like"*/
                    //holder.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_black, 0, 0, 0);
                    holder.btnLike.setText("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void showMoreOptions(ImageButton ibMore, String uid, String myUID, String pID, String pImage) {
        //Creating popup menu currently having option delete, we will add more options later
        PopupMenu popupMenu = new PopupMenu(context, ibMore, Gravity.END);
        //add items in menu

        if (uid.equals(myUID)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Edit");
        }
        //items click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 0) {
                    //delete is clicked
                    beginDelete(pID, pImage);
                }
                if (id == 1) {
                    //Edit is clicked
                    //start addpostactivity with key "EditPost" and the id of the post clicked
                    Intent intent = new Intent(context, AddRate.class);
                    intent.putExtra("key", "editPost");
                    intent.putExtra("editPostID", pID);
                    context.startActivity(intent);

                }
                return false;
            }
        });
        // show menu
        popupMenu.show();

    }

    private void beginDelete(String pID, String pImage) {
        //post  can be with or without image
        if (pImage.equals("noImage")) {
            //post is without image
            deleteWithoutImage(pID);
        } else {
            //post is with image
            deleteWithImage(pID, pImage);
        }
    }

    private void deleteWithImage(String pID, String pImage) {
        //progress bar
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting...");
        /*Steps:
         * 1. Delete image using url
         * 2. Delete from database using post id*/
        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //image deleted, now delete database
                        Query fquery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pID").equalTo(pID);
                        fquery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ds.getRef().removeValue();
                                }
                                //deleted
                                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteWithoutImage(String pID) {
        //progress bar
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting...");

        Query fquery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pID").equalTo(pID);
        fquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().removeValue();
                }
                //deleted
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class Myholder extends RecyclerView.ViewHolder {

        //View from row_rate_post_fragement xml
        ImageView uIVPic, pIVPost;
        TextView uTVName, pTVTIme, pTVTitle, pTVDescription, pTVLikes;
        ImageButton ibMore;
        Button btnLike, btnShare, btnComment;
        LinearLayout llProfile;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            //init view
            uIVPic = itemView.findViewById(R.id.rrp_civ_upic);
            pIVPost = itemView.findViewById(R.id.rrp_iv_post);
            uTVName = itemView.findViewById(R.id.rrp_tv_uname);
            pTVTIme = itemView.findViewById(R.id.rrp_tv_time);
            pTVTitle = itemView.findViewById(R.id.rrp_tv_title);
            pTVDescription = itemView.findViewById(R.id.rrp_tv_description);
            pTVLikes = itemView.findViewById(R.id.rrp_tv_likes);
            llProfile = itemView.findViewById(R.id.rrp_ll_profile);
            btnLike = itemView.findViewById(R.id.rrp_btn_like);
            ibMore = itemView.findViewById(R.id.rrp_ibtn_more);
            // btnComment = itemView.findViewById(R.id.rrp_btn_comment);
            btnShare = itemView.findViewById(R.id.rrp_btn_share);

        }
    }
}
