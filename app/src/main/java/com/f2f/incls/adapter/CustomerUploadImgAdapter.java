package com.f2f.incls.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.model.AllLeadsListModel;
import com.f2f.incls.model.AllServiceModel;
import com.f2f.incls.model.EmpUploadImgModel;
import com.f2f.incls.utilitty.CustomerUploadModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomerUploadImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<CustomerUploadModel> uploadarrayList;


    public CustomerUploadImgAdapter(Context context, ArrayList<CustomerUploadModel> uploadarrayList) {
        this.context=context;
        this.uploadarrayList=uploadarrayList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Viewholder viewholder=null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_upload_image_view,parent,false);
        viewholder=new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        CustomerUploadModel model=uploadarrayList.get(position);
        final Viewholder viewholder=(Viewholder)holder;
        String path=model.getImage_path();
        Log.d("Adapter", "adapter_image_path::" + path);
        if (path==null ||path.isEmpty()){
            viewholder.img_close.setVisibility(View.GONE);
            viewholder.customer_upload.setVisibility(View.GONE);
        }else {

            Log.d("Adapter", "adapter_image_path::" + path);
            Glide.with(context)
                    .load(path)
                    .thumbnail(0.5f)
                    .into(viewholder.customer_upload);
        }

viewholder.img_close.setVisibility(View.GONE);
       /* viewholder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==viewholder.img_close.getId()){
                    uploadarrayList.remove(viewholder.getAdapterPosition());
                    notifyDataSetChanged();
                    Log.d("Success","holder_possition::"+viewholder.getPosition());
                    Log.d("Success","view_id::"+v.getId());
                    Log.d("Success","holder_id::"+viewholder.img_close.getId());
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        Log.d("Success","Bit_map_array::"+uploadarrayList.size());
        return uploadarrayList.size();
    }

    private class Viewholder extends RecyclerView.ViewHolder {
        ImageView customer_upload,img_close;
        Viewholder(View view) {
            super(view);
            img_close=view.findViewById(R.id.img_close);
            customer_upload=view.findViewById(R.id.customer_upload);
        }
    }
}
