package com.thatchosenone.travelmakati.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.ReviewModel;
import com.thatchosenone.travelmakati.activities.AddReview;
import com.thatchosenone.travelmakati.activities.Chat;
import com.thatchosenone.travelmakati.activities.ThereProfile;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyHolder> {

    Context context;
    List<ReviewModel> postList;
    String myUID;
    String name, lname, ladd;
    String key, nplikes;
    //private DatabaseReference likesRef; //for likes database node
    private DatabaseReference ratingRef, userRef, aveRateRef; //reference of posts

    boolean mProcessLike = false;
    private String TAG;

    public ReviewAdapter(Context context, List<ReviewModel> postList) {
        this.context = context;
        this.postList = postList;
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        ratingRef = FirebaseDatabase.getInstance().getReference().child("ratings");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        aveRateRef = FirebaseDatabase.getInstance().getReference().child("leisureAveRates");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_rate_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_reviews_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //get data
        final String uid = postList.get(position).getUser_uid();
        String deleted = postList.get(position).getIsDeleted();
//        String uDp = postList.get(position).getAccount_photo();
        String pRating = postList.get(position).getRate_value();
        String pComment = postList.get(position).getComment();
        final String lID = postList.get(position).getLuid();
        final String pTimestamp = postList.get(position).getDate_time();

        //conver t timestamp to dd/mm/yyyy hh:mm:pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimestamp));
        String pTime = DateFormat.format("dd MMM yyyy 'at' hh:mm aa", calendar).toString();
        float nratings = Float.parseFloat(pRating);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String uids = "" + ds.child("u_id").getValue();
                        if (uid.equalsIgnoreCase(uids)) {
                            holder.uTVName.setText("" + ds.child("name").getValue());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //set data
        //holder.uTVName.setText(uid);
        holder.pTVTIme.setText(pTime);
        holder.ratingBar.setRating(nratings);
        holder.pTVComment.setText(pComment);

//        try {
//            Picasso.get().load(uDp).into(holder.uIVPic);
//        } catch (Exception e) {
//
//        }

        //handle click button
        holder.ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptions(holder.ibMore, uid, myUID, pTimestamp, lID);
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


    private void shareTextOnly(String lID, String uid, String pComment) {

//        String shareBody = pComment;
//
//        DatabaseReference lref = FirebaseDatabase.getInstance().getReference("Leisure");
//        lref.orderByChild("luid").equalTo(lID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                lname = "" + dataSnapshot.child("name").getValue();
//                ladd = "" + dataSnapshot.child("street").getValue();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        //share intent
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
//        intent.putExtra(Intent.EXTRA_TEXT, shareBody + "\n" + lname + "\n" + ladd);
//        context.startActivity(Intent.createChooser(intent, "Share Via"));


    }

    private void showMoreOptions(ImageButton ibMore, String uid, String myUID, String pTimestamp, String lID) {
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
                    beginDelete(pTimestamp, lID);
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
                    Toast.makeText(context, "You Reported as Spam", Toast.LENGTH_SHORT).show();
                }
                if (which == 1) {
                    Toast.makeText(context, "You Reported as Violence", Toast.LENGTH_SHORT).show();
                }
                if (which == 2) {
                    Toast.makeText(context, "You Reported as Unrelated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //create and show dialog
        builder.create().show();

    }

    private void beginDelete(String pID, String lID) {
        //   post  can be with or without image
        deleteWithoutImage(pID, lID);
    }

    private void deleteWithoutImage(String pID, String lID) {
        //progress bar
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting...");

        HashMap<String, Object> deleteMap = new HashMap<>();
        //put post info
        deleteMap.put("isDeleted", "1");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings");
        ref.child(myUID).child(pID).updateChildren(deleteMap);
        pd.dismiss();
        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();

        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total = 0.0;
                double count = 0.0;
                double average = 0.0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dss : ds.getChildren()) {
                        String rates = "" + dss.child("rate_value").getValue();
                        String isdel = "" + dss.child("isDeleted").getValue();
                        String uid = "" + dss.child("luid").getValue();
                        double nrates = Double.parseDouble(rates);
                        if (uid.equals(lID) && isdel.equals("0")) {
                            total = total +  nrates;
                            count = count + 1;
                            average = total / count;
                        }
                    }
                }

                HashMap<String, Object> aveRateMap = new HashMap<>();
                //put post info
                aveRateMap.put("total_rate", "" + average);
                aveRateMap.put("luid", lID);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("leisureAveRates");
                ref.child(lID).updateChildren(aveRateMap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, ""+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        //View from row_rate_post_fragement xml
        CircleImageView uIVPic;
        TextView uTVName, pTVTIme, pTVComment;
        ImageButton ibMore;
        MaterialRatingBar ratingBar;
//        Button btnLike, btnShare;
//        LinearLayout llProfile;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.rri_mrb_rate);
            //uIVPic = itemView.findViewById(R.id.rri_civ_dp);
            uTVName = itemView.findViewById(R.id.rri_tv_name);
            pTVTIme = itemView.findViewById(R.id.rri_tv_date_time);
            pTVComment = itemView.findViewById(R.id.rri_tv_comment);
            ibMore = itemView.findViewById(R.id.rri_ibtn_more);
        }
    }
}
