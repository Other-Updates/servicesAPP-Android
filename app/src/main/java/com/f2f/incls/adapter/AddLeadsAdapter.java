package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.fragment.AddLeadsFragment;
import com.f2f.incls.model.AddLeadsModel;
import com.tooltip.Tooltip;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class AddLeadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<AddLeadsModel> arrayList;
    Fragment fragment;

    public AddLeadsAdapter(Context context, ArrayList<AddLeadsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolders viewHolder = null;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_category_view, parent, false);
        viewHolder = new MyViewHolders(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final AddLeadsModel model = arrayList.get(position);
        final MyViewHolders viewholder = (MyViewHolders) holder;
        viewholder.category_name.setText(model.getProduct_name());
        int product_id=model.getProduct_id();
        String image=model.getProduct_image();
        if (image.isEmpty()){
            viewholder.product_img.setImageResource(R.drawable.favicon);
        }else {
            Glide.with(context)
                    .load(image)
                    .thumbnail(0.5f)
                    .into(viewholder.product_img);
        }
        if (model.isChecked()) {
            viewholder.click_img.setVisibility(View.VISIBLE);
        } else {
            viewholder.click_img.setVisibility(View.GONE);
        }
        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=viewholder.getAdapterPosition();
                fragment =new AddLeadsFragment();
                Bundle args = new Bundle();
                args.putString("product_possion", String.valueOf(pos));
                fragment.setArguments(args);
                for (AddLeadsModel model1 : arrayList) {
                    model1.setChecked(false);
                }
                if (model.isChecked()) {
                    model.setChecked(false);
                    viewholder.click_img.setVisibility(View.VISIBLE);
                } else {
                    model.setChecked(true);
                    viewholder.click_img.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });
        viewholder.category_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.Builder builder = new Tooltip.Builder(v)
                        .setCancelable(true)
                        .setCornerRadius(20f)
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.parseColor("#0250e1"))
                        .setText(model.getProduct_name());
                builder.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    private class MyViewHolders extends RecyclerView.ViewHolder{
        ImageView product_img, click_img;
        TextView category_name;
        Fragment fragment;
        public MyViewHolders(View view) {
            super(view);
            product_img = view.findViewById(R.id.product_img);
            category_name = view.findViewById(R.id.category_name);
            click_img = view.findViewById(R.id.click_img);
        }

    }
}



