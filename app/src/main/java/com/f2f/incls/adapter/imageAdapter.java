package com.f2f.incls.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class imageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<String> imageList;
    public imageAdapter(Context context, ArrayList<String> imageList) {
        this.context=context;
        this.imageList=imageList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewholder viewholder;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emp_upload_image_view,parent,false);
        viewholder=new viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final viewholder viewholder= (imageAdapter.viewholder) holder;
        Log.d("Success","image_size_test_image_adapter::"+imageList.size());
        String image=imageList.get(position);
        Glide.with(context)
                .load(image)
                .thumbnail(0.5f)
                .into(viewholder.emp_upload);
        viewholder.img_close.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
    private class viewholder extends RecyclerView.ViewHolder{
        CircleImageView emp_upload;
        ImageView img_close;
        public viewholder(@NonNull View view) {
            super(view);
            emp_upload=view.findViewById(R.id.emp_upload);
            img_close=view.findViewById(R.id.img_close);
        }
    }
}
