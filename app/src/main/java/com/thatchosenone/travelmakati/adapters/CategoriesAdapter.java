package com.thatchosenone.travelmakati.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.thatchosenone.travelmakati.activities.LeisureCategory;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.CategoriesModel;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter <CategoriesAdapter.MyHolder>{

    Context context;
    List <CategoriesModel> categoriesModels;

    //create a parameterized constructor
    public CategoriesAdapter(Context context, List<CategoriesModel> categoriesModels) {
        this.context = context;
        this.categoriesModels = categoriesModels;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_place_item, parent, false); // inflate layout
        return new MyHolder(view); // return holder

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //get data
        String name = categoriesModels.get(position).getCategName();
        String photo = categoriesModels.get(position).getCategPhoto();

        //set data
        holder.tvName.setText(name);

        try {
            Picasso.get().load(photo).into(holder.ivCategories);
        } catch (Exception e) {

        }

        //clicked method
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  // when item is clicked goes to another activity
                Intent categ = new Intent(context, LeisureCategory.class);
                categ.putExtra("category", name);
                context.startActivity(categ);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesModels.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView ivCategories;
        TextView tvName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivCategories = itemView.findViewById(R.id.rpi_iv_leisure_photo);
            tvName = itemView.findViewById(R.id.rpi_tv_leisure_name);
        }
    }
}
