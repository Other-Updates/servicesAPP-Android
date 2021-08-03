package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.f2f.incls.R;
import com.f2f.incls.model.WarrantyListModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WarrantyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static Context context;
    ArrayList<WarrantyListModel> arrayList;
    public WarrantyListAdapter(Context context,
                               ArrayList<WarrantyListModel> arrayList) {
        this.context=context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.warranty_list_view,parent,false);
        myViewholder viewHolder=new myViewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WarrantyListModel model=arrayList.get(position);
        myViewholder hold=(myViewholder)holder;
        String inv_not_select=model.getInv_not_select();
        Log.d("Success","adapter_not_select_test::"+inv_not_select);
        if (!inv_not_select.equals("")) {
            if (inv_not_select.equals("Select Invoice")) {
                hold.product_name.setText("");
                hold.product_des.setText("");
                hold.warranty_service_pro_bg.setBackgroundColor(Color.WHITE);

            } else {
                hold.product_name.setText(model.getProduct_name());
                hold.product_des.setText(model.getProduct_des());
                hold.warranty_service_pro_bg.setBackground(context.getDrawable(R.drawable.warranty_bg));
                Log.d("Success", "adapter_not_select_test_else::" + inv_not_select);
            }

        }else {
            hold.product_name.setText(model.getProduct_name());
            hold.product_des.setText(model.getProduct_des());
            hold.warranty_service_pro_bg.setBackground(context.getDrawable(R.drawable.warranty_bg));
            Log.d("Success", "adapter_not_select_test_else_null::" + inv_not_select);
        }
    }

    @Override
    public int getItemCount()
    {
        Log.d("Success","warranty_array_size::"+arrayList.size());
        return arrayList.size();
    }

    private static class myViewholder extends RecyclerView.ViewHolder {
        TextView product_name,product_des;
        LinearLayout warranty_service_pro_bg;
        public myViewholder(View view) {
            super(view);
            product_name=view.findViewById(R.id.product_name);
            product_des=view.findViewById(R.id.product_des);
            warranty_service_pro_bg=view.findViewById(R.id.warranty_service_pro_bg);

        }


    }
}
