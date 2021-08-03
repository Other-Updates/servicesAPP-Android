package com.f2f.incls.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.model.PaidworkImgUploadModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PaidWorkImgUploadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<PaidworkImgUploadModel> workuploadList;
    public PaidWorkImgUploadAdapter(Context context, ArrayList<PaidworkImgUploadModel> workuploadList) {
        this.context=context;
        this.workuploadList=workuploadList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.paid_work_img_upload_view,parent,false);
        viewholder viewholder=new viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PaidworkImgUploadModel model=workuploadList.get(position);
        final viewholder viewholder=(PaidWorkImgUploadAdapter.viewholder)holder;
        String path=model.getImage_path();
        Glide.with(context)
                .load(path)
                .into(viewholder.paid_work_customer_upload);

        viewholder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()== viewholder.img_close.getId()){
                    workuploadList.remove(viewholder.getAdapterPosition());
                    notifyDataSetChanged();
                    Log.d("Success","holder_possition::"+viewholder.getPosition());
                    Log.d("Success","view_id::"+v.getId());
                    Log.d("Success","holder_id::"+viewholder.img_close.getId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return workuploadList.size();
    }
    public class viewholder extends RecyclerView.ViewHolder{
        ImageView paid_work_customer_upload,img_close;
        public viewholder(@NonNull View view) {
            super(view);
            paid_work_customer_upload=view.findViewById(R.id.paid_work_customer_upload);
            img_close=view.findViewById(R.id.img_close);
        }
    }
}
