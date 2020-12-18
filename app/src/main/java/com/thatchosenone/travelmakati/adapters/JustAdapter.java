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
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.LeisureModel;
import com.thatchosenone.travelmakati.activities.AllLeisureDetails;

import java.util.HashMap;
import java.util.List;

public class JustAdapter extends RecyclerView.Adapter <JustAdapter.MyHolder>{

    Context context;
    List<LeisureModel> leisureList;
    private DatabaseReference leisureRef;
    private DatabaseReference mostViewRef, userCount;
    String myUID;

    public JustAdapter(Context context, List<LeisureModel> leisureList) {
        this.context = context;
        this.leisureList = leisureList;
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        leisureRef = FirebaseDatabase.getInstance().getReference().child("leisures");
        mostViewRef = FirebaseDatabase.getInstance().getReference().child("leisureViews");
        userCount = FirebaseDatabase.getInstance().getReference().child("userCount");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_full_place_item, parent, false); // inflate layout
        return new JustAdapter.MyHolder(view); // return holder
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String luid = leisureList.get(position).getLeisureID();
        String lphoto = leisureList.get(position).getLeisurePhoto();
        String lname = leisureList.get(position).getName();

        holder.tvName.setText(lname);

        try {
            Picasso.get().load(lphoto).into(holder.ivPhoto);
        } catch (Exception e) {

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"UID: "+lDateTime, Toast.LENGTH_SHORT).show();
                //int tview = Integer.parseInt(leisureList.get(position).getTotal_view());
                Intent intent = new Intent(context, AllLeisureDetails.class);
                intent.putExtra("luid", luid);
                context.startActivity(intent);

                leisureRef.orderByChild("leisureID").equalTo(luid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                            storeTotalView(luid);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    private void storeTotalView(String luid) {

        mostViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                totalViewMap.put("leisureID", luid);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("totalView");
                ref.child(luid).updateChildren(totalViewMap);

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

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView ivPhoto;
        TextView tvName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.rfpi_iv_leisure_photo);
            tvName = itemView.findViewById(R.id.rfpi_tv_leisure_name);
        }
    }
}

