package com.thatchosenone.travelmakati.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.LeisurePhotosModel;
import com.thatchosenone.travelmakati.activities.PreviewPhoto;

import java.util.List;

public class LeisurePhotosAdapter extends RecyclerView.Adapter<LeisurePhotosAdapter.MyHolder> {

    Context context;
    List<LeisurePhotosModel> lPhotoList;
    private DatabaseReference lPhotoSRef; //for likes database node
    String myUID;


    public LeisurePhotosAdapter(Context context, List<LeisurePhotosModel> lPhotoList) {
        this.context = context;
        this.lPhotoList = lPhotoList;
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        lPhotoSRef = FirebaseDatabase.getInstance().getReference().child("LeisurePhotos");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_rate_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_places_item, parent, false);
        return new LeisurePhotosAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        String lphoto = lPhotoList.get(position).getLeisure_photo();
//        String uname = lPhotoList.get(position).getName();
//        String userid = lPhotoList.get(position).getUser_uid();
//        String dp = lPhotoList.get(position).getDp();
        String ldate = lPhotoList.get(position).getDate_time();
//        String luid = lPhotoList.get(position).getLuid();
//        String lcaption = lPhotoList.get(position).getDp();

        try {
            Picasso.get().load(lphoto).into(holder.ivLPhoto);
        } catch (Exception e) {

        }

        holder.ivLPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preview = new Intent(context, PreviewPhoto.class);
                preview.putExtra("ldate", ldate);
//                preview.putExtra("luid", luid);
                context.startActivity(preview);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lPhotoList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView ivLPhoto;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
//            //init view
//            ivLPhoto = itemView.findViewById(R.id.rlp_iv_leisure_photo);
        }
    }
}
