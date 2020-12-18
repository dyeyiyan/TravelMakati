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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.thatchosenone.travelmakati.models.LeisurePhotosModel;
import com.thatchosenone.travelmakati.activities.ActivityMain;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PreviewPhotoAdapter extends RecyclerView.Adapter<PreviewPhotoAdapter.MyHolder> {

    Context context;
    List<LeisurePhotosModel> lPhotoList;
    String myUID;
    private DatabaseReference lPhotoRef; //for likes database node


    public PreviewPhotoAdapter(Context context, List<LeisurePhotosModel> lPhotoList) {
        this.context = context;
        this.lPhotoList = lPhotoList;
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        lPhotoRef = FirebaseDatabase.getInstance().getReference().child("LeisurePhotos");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_rate_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_preview_photo, parent, false);
        return new PreviewPhotoAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        String lphoto = lPhotoList.get(position).getLeisure_photo();
        String uname = lPhotoList.get(position).getName();
        String userid = lPhotoList.get(position).getUser_uid();
        String dp = lPhotoList.get(position).getDp();
        String ldate = lPhotoList.get(position).getDate_time();
        String luid = lPhotoList.get(position).getLuid();
        String lcaption = lPhotoList.get(position).getCaption();

        //conver t timestamp to dd/mm/yyyy hh:mm:pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(ldate));
        String pTime = DateFormat.format("dd MMM yyyy 'at' hh:mm aa", calendar).toString();

        holder.tvUname.setText(uname);
        holder.tvPDate.setText(pTime);
        holder.tvUCaption.setText(lcaption);

        try {
            Picasso.get().load(lphoto).into(holder.ivUPhoto);
        } catch (Exception e) {

        }

        holder.ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(context, ActivityMain.class);
                context.startActivity(back);
            }
        });

        holder.ibtnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report(holder.ibtnReport, myUID, ldate, userid, lphoto, luid);
            }
        });

    }


    private void report(ImageButton ibtnReport, String myUID, String ldate, String userid, String lphoto, String luid) {
        //Creating popup menu currently having option delete, we will add more options later
        PopupMenu popupMenu = new PopupMenu(context, ibtnReport, Gravity.END);
        //add items in menu

        if (userid.equals(myUID)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
        } else {
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Report");
        }
        //items click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 0) {
                    //delete is clicked
                    beginDelete(ldate, lphoto);
                }
                if (id == 1) {
                    showDialogReport();
                }
                return false;
            }
        });
        // show menu
        popupMenu.show();
    }

    private void showDialogReport() {
        //options {camera, gallery} to show dialog
        String[] options = {"Nudity", "Not Helpful", "Unrelated", "Hate or Violence"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Report Photo as");

        //set option to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //item click handle
                if (which == 0) {
                    Toast.makeText(context, "You Reported as Nudity", Toast.LENGTH_SHORT).show();
                }
                if (which == 1) {
                    Toast.makeText(context, "You Reported as Not Helpful", Toast.LENGTH_SHORT).show();
                }
                if (which == 2) {
                    Toast.makeText(context, "You Reported as Unrelated", Toast.LENGTH_SHORT).show();
                }
                if (which == 3) {
                    Toast.makeText(context, "You Reported as Hate or Violence", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void beginDelete(String ldate, String lphoto) {
        //progress bar
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting...");
        /*Steps:
         * 1. Delete image using url
         * 2. Delete from database using post id*/
        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(lphoto);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //image deleted, now delete database
                        Query fquery = FirebaseDatabase.getInstance().getReference("LeisurePhotos").orderByChild("date_time").equalTo(ldate);
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

    @Override
    public int getItemCount() {
        return lPhotoList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageButton ibtnBack, ibtnReport;
        ImageView ivUPhoto;
        TextView tvUname, tvUCaption, tvPDate;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init view
            ibtnBack = itemView.findViewById(R.id.rpp_ibtn_back);
            ibtnReport = itemView.findViewById(R.id.rpp_ibtn_report);
            ivUPhoto = itemView.findViewById(R.id.rpp_iv_lpic);
            tvUname = itemView.findViewById(R.id.rpp_tv_uname);
            tvUCaption = itemView.findViewById(R.id.rpp_tv_caption);
            tvPDate = itemView.findViewById(R.id.rpp_tv_pdate);
        }
    }
}
