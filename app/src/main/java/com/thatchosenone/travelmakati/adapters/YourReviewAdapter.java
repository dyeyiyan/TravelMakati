package com.thatchosenone.travelmakati.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.ReviewModel;
import com.thatchosenone.travelmakati.activities.AddReview;
import com.thatchosenone.travelmakati.activities.AllLeisureDetails;
import com.thatchosenone.travelmakati.activities.Chat;
import com.thatchosenone.travelmakati.activities.ThereProfile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class YourReviewAdapter extends RecyclerView.Adapter<YourReviewAdapter.MyHolder> {

    Context context;
    List<ReviewModel> postList;
    String myUID;
    String key, nplikes;
    private DatabaseReference likesRef; //for likes database node
    private DatabaseReference ratingRef; //reference of posts
    private DatabaseReference leisure; //reference of posts

    boolean mProcessLike = false;


    public YourReviewAdapter(Context context, List<ReviewModel> postList) {
        this.context = context;
        this.postList = postList;
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        ratingRef = FirebaseDatabase.getInstance().getReference().child("Ratings");
        leisure = FirebaseDatabase.getInstance().getReference().child("Leisure");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_rate_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_review, parent, false);
        return new YourReviewAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        final String uid = postList.get(position).getUser_uid();
       // String uName = postList.get(position).getName();
        //String uDp = postList.get(position).getDp();
        String pRating = postList.get(position).getRate_value();
        String pComment = postList.get(position).getComment();
        final String lID = postList.get(position).getLuid();
        //final String pImage = postList.get(position).getRating_photo();
        final String pTimestamp = postList.get(position).getDate_time();
        //String pLikes = postList.get(position).getPlikes(); //contains total number of likes for  a post

        //conver t timestamp to dd/mm/yyyy hh:mm:pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimestamp));
        String pTime = DateFormat.format("dd MMM yyyy 'at' hh:mm aa", calendar).toString();

        float nratings = Float.parseFloat(pRating);

        //int npL = Integer.parseInt(pLikes);
        //set data
        //holder.uTVName.setText(uName);
        holder.pTVTIme.setText(pTime);
        holder.ratingBar.setRating(nratings);
        holder.pTVComment.setText(pComment);
//        if (npL >= 1) {
//            holder.pTVLikes.setText("(" + pLikes + ")" + " Likes");
//            //holder.pTVLikes.setText("(" + pLikes + ") " + "Likes"); // e.g 100 likes
//        } else {
//            holder.pTVLikes.setText("(" + pLikes + ")" + " Like");
//        }
        //set user dp
//        try {
//            Picasso.get().load(uDp).placeholder(R.drawable.ic_default_img_blue).into(holder.uIVPic);
//        } catch (Exception e) {
//
//        }

//        if (pImage.equals("noImage")) {
//            //hide image
//            holder.pIVPost.setVisibility(View.GONE);
//        } else {
//            //show image
//            holder.pIVPost.setVisibility(View.VISIBLE);
//            //set post image
//            try {
//                Picasso.get().load(pImage).into(holder.pIVPost);
//            } catch (Exception e) {
//
//            }
//        }

        leisure.orderByChild("luid").equalTo(lID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    String lname = "" + dss.child("name").getValue();
                    String lpic = "" + dss.child("photo").getValue();
                    String ladd = "" + dss.child("street").getValue();

                    holder.lTVName.setText(lname);
//                    try {
//                        Picasso.get().load(lpic).placeholder(R.drawable.ic_default_img_blue).into(holder.lIVPic);
//                    } catch (Exception e) {
//
//                    }
                    holder.lTVAdd.setText(ladd);

                    holder.llLiesure.setVisibility(View.VISIBLE);

                    holder.llLiesure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AllLeisureDetails.class);
                            intent.putExtra("luid", lID);
                            context.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setLikes(holder, pTimestamp);

//        //handle click button
//        holder.ibMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showMoreOptions(holder.ibMore, uid, myUID, pTimestamp, pImage, lID);
//            }
//        });

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get total number of like for the post, whose like button clicked
                //if currently signed in user has not liked it before
                ///increase value by 1, otherwise decrease value by 1
                //int pLikes = Integer.parseInt(postList.get(position).getPlikes());
                mProcessLike = true;
                //get id of the post clicked
                final String uid = postList.get(position).getUser_uid();
                final String pID = postList.get(position).getDate_time();

                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (mProcessLike) {
//                            if (dataSnapshot.child(pID).hasChild(myUID)) {
//                                //already liked, so remove like
//                                ratingRef.child(pID).child("plikes").setValue("" + (pLikes - 1));
//                                likesRef.child(pID).child(myUID).removeValue();
//                                mProcessLike = false;
//
//                            } else {
//                                //not liked, like it
//                                ratingRef.child(pID).child("plikes").setValue("" + (pLikes + 1));
//                                likesRef.child(pID).child(myUID).child("id").setValue(myUID);
//                                mProcessLike = false;
//                            }
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
                /*some posts contains only text, and some contains iimage and text so, we will handle them both
                 */
                //get image from imageview
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.pIVPost.getDrawable();
                if (bitmapDrawable == null) {
                    //post without image
                    shareTextOnly(pComment);
                } else {
                    //post with image
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    shareImageAndTxt(pComment, bitmap);


                }
            }
        });

//        holder.llProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*Click to go to ThereProfile with uid, this uid is of clicked user
//                 * which will be used to show user specific data/posts*/
//                Intent intent = new Intent(context, ThereProfile.class);
//                intent.putExtra("uid", uid);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    private void shareImageAndTxt(String pComment, Bitmap bitmap) {

        String shareBody = pComment;

        //first we will save this image in cache, get the saved image uri
        Uri uri = saveImageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        intent.setType("image/png");
        context.startActivity(Intent.createChooser(intent, "Share Via"));
        //copy same code in post


    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdir();//create if not exists
            File file = new File(imageFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(context, "com.thatchosenone.travelmakati", file);

        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return uri;
    }

    private void shareTextOnly(String pComment) {

        String shareBody = pComment;

        //share intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(intent, "Share Via"));


    }

    private void showMoreOptions(ImageButton ibMore, String uid, String myUID, String pTimestamp, String pImage, String lID) {
        //Creating popup menu currently having option delete, we will add more options later
        PopupMenu popupMenu = new PopupMenu(context, ibMore, Gravity.END);
        //add items in menu
        if (uid.equals(myUID)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Edit");
        } else {
            popupMenu.getMenu().add(Menu.NONE, 2, 0, "Report");
            popupMenu.getMenu().add(Menu.NONE, 3, 0, "View Profile");
            popupMenu.getMenu().add(Menu.NONE, 4, 0, "Send Message");
        }
        //items click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 0) {
                    //delete is clicked
                    beginDelete(pTimestamp, pImage);
                }
                if (id == 1) {
                    //Edit is clicked
                    //start addpostactivity with key "EditPost" and the id of the post clicked
                    Intent intent = new Intent(context, AddReview.class);
                    intent.putExtra("key", "editPost");
                    intent.putExtra("editReviewID", pTimestamp);
                    intent.putExtra("editReviewLUID", lID);
                    //intent.putExtra("editReviewUID", uid);
                    context.startActivity(intent);
                }
                if (id == 2) {
                    showDialogReport();
                }

                if (id == 3) {
                    Intent intent = new Intent(context, ThereProfile.class);
                    intent.putExtra("uid", uid);
                    context.startActivity(intent);
                    //showDialogReport();
                }

                if (id == 4) {
                    Intent intent = new Intent(context, Chat.class);
                    intent.putExtra("hisUID", uid);
                    intent.putExtra("chats", "review_act");
                    context.startActivity(intent);
                    //showDialogReport();
                }
                return false;
            }
        });
        // show menu
        popupMenu.show();
    }

    private void showDialogReport() {
        //options {camera, gallery} to show dialog
        String[] options = {"Spam", "Violence", "Unrelated"};

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Report Review as");

        //set option to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //item click handle
                if (which == 0) {
                    Toast.makeText(context, "Spam", Toast.LENGTH_SHORT).show();
                }
                if (which == 1) {
                    Toast.makeText(context, "Violence", Toast.LENGTH_SHORT).show();
                }
                if (which == 2) {
                    Toast.makeText(context, "Unrelated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //create and show dialog
        builder.create().show();

    }

    private void setLikes(YourReviewAdapter.MyHolder holder, String pID) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(pID).hasChild(myUID)) {
                    //user has liked this post
                    /*To indicate that the post if like by this (Signed IN) user
                     * Change drawable left icon of like button
                     * change text of like button from "Like" to "Like"*/
                    //holder.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_blue, 0, 0, 0);
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

    private void beginDelete(String pID, String pImage) {
        //   post  can be with or without image
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
                        DatabaseReference fquery = FirebaseDatabase.getInstance().getReference("Ratings");
                        fquery.orderByChild("date_time").equalTo(pID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ds.getRef().removeValue();
                                }
                                //deleted
                                likesRef.child(pID).child(myUID).removeValue();
                                pd.dismiss();
                                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
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
//        Query fquery = FirebaseDatabase.getInstance().getReference("Ratings").orderByChild("date_time").equalTo(pID);
        DatabaseReference fquery = FirebaseDatabase.getInstance().getReference("Ratings");
        fquery.orderByChild("date_time").equalTo(pID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().removeValue();
                }
                //deleted
                likesRef.child(pID).child(myUID).removeValue();
                pd.dismiss();
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    class MyHolder extends RecyclerView.ViewHolder {

        //View from row_rate_post_fragement xml
        ImageView uIVPic, pIVPost, lIVPic;
        TextView uTVName, pTVTIme, pTVComment, pTVLikes, lTVName, lTVAdd;
        ImageButton ibMore;
        MaterialRatingBar ratingBar;
        Button btnLike, btnShare;
        LinearLayout llProfile, llLiesure;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.rr_ratings);
            uIVPic = itemView.findViewById(R.id.rr_civ_upic);
            pIVPost = itemView.findViewById(R.id.rr_iv_post);
            uTVName = itemView.findViewById(R.id.rr_tv_uname);
            pTVTIme = itemView.findViewById(R.id.rr_tv_time);
            pTVComment = itemView.findViewById(R.id.rr_tv_comment);
            pTVLikes = itemView.findViewById(R.id.rr_tv_likes);
            llProfile = itemView.findViewById(R.id.rr_ll_profile);
            btnLike = itemView.findViewById(R.id.rr_btn_like);
            ibMore = itemView.findViewById(R.id.rr_ibtn_more);
            // btnComment = itemView.findViewById(R.id.rrp_btn_comment);
            btnShare = itemView.findViewById(R.id.rr_btn_share);

            lIVPic = itemView.findViewById(R.id.rr_civ_lpic);
            lTVName = itemView.findViewById(R.id.rr_tv_lname);
            lTVAdd = itemView.findViewById(R.id.rr_tv_ladd);
            llLiesure = itemView.findViewById(R.id.ll_one);

        }
    }
}
