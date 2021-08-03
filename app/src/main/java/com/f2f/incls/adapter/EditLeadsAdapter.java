package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.fragment.AddLeadsFragment;
import com.f2f.incls.model.AddLeadsModel;
import com.f2f.incls.model.EditLeadsModel;
import com.tooltip.Tooltip;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class EditLeadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<EditLeadsModel> arrayList;
    String old_id;
    int mSelectedItem=-1;
    Fragment fragment;
    int selecteId=100;
    public EditLeadsAdapter(Context context, ArrayList<EditLeadsModel> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewholder viewholder=null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_category_view,parent,false);
        viewholder=new MyViewholder(view);
        return viewholder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final EditLeadsModel model = arrayList.get(position);
        final MyViewholder viewholder = (MyViewholder) holder;
        viewholder.category_name.setText(model.getProduct_name());
        final int[] old_id = {Integer.parseInt(model.getOld_id())};
       final int new_id = model.getProduct_id();
        String img = model.getImage();
        if (img.isEmpty()){
            viewholder.product_img.setImageResource(R.drawable.favicon);
        }else {
            Glide.with(context)
                    .load(img)
                    .thumbnail(0.5f)
                    .into(viewholder.product_img);
        }
        if (model.isChecked()) {
            viewholder.click_img.setVisibility(View.VISIBLE);
        } else {
            viewholder.click_img.setVisibility(View.GONE);
        }
        if (selecteId==100){
            if (old_id[0] == new_id) {
                viewholder.click_img.setVisibility(View.VISIBLE);
                model.setChecked(true);
            } else {
                viewholder.click_img.setVisibility(View.GONE);
                model.setChecked(false);
            }
        }

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (EditLeadsModel model1 : arrayList) {
                    model1.setChecked(false);
                }
                if (model.isChecked()) {
                    model.setChecked(false);
                    viewholder.click_img.setVisibility(View.VISIBLE);
                } else {
                    model.setChecked(true);
                    viewholder.click_img.setVisibility(View.GONE);
                }
                selecteId=200;
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

    private class MyViewholder extends RecyclerView.ViewHolder {
        ImageView product_img, click_img;
        TextView category_name;
        EditText contact_1,contact_2;
        Fragment fragment;

        public MyViewholder(View view) {
            super(view);
            product_img = view.findViewById(R.id.product_img);
            category_name = view.findViewById(R.id.category_name);
            click_img = view.findViewById(R.id.click_img);
            contact_1 = view.findViewById(R.id.contact_1);
            contact_2 = view.findViewById(R.id.contact_2);

        }
    }

}
