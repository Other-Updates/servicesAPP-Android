package com.f2f.incls.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.model.PaidCustomerImgUploadModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PaidCustomerImgUploadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    String path;
    ArrayList<PaidCustomerImgUploadModel> cusuploadList;
    public PaidCustomerImgUploadAdapter(Context context, ArrayList<PaidCustomerImgUploadModel> cusuploadList) {
        this.context=context;
        this.cusuploadList=cusuploadList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewholder viewholder;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.paid_customer_img_upload_view,parent,false);
        viewholder=new viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PaidCustomerImgUploadModel model=cusuploadList.get(position);
        final viewholder viewholder=(PaidCustomerImgUploadAdapter.viewholder)holder;
        path=model.getImagepath();
        Log.d("Message","Adapter_image_test::"+path);
        Glide.with(context)
                .load(path)
                .into(viewholder.paid_customer_upload);
        viewholder.img_close.setVisibility(View.GONE);

       /* viewholder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()== viewholder.img_close.getId()){
                    cusuploadList.remove(viewholder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return cusuploadList.size();
    }
    public class viewholder extends RecyclerView.ViewHolder{
        ImageView paid_customer_upload,img_close,iv_preview_image;
        public viewholder(@NonNull View view) {
            super(view);
            paid_customer_upload=view.findViewById(R.id.paid_customer_upload);
            img_close=view.findViewById(R.id.img_close);
            iv_preview_image=view.findViewById(R.id.iv_preview_image);

            paid_customer_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    nagDialog.setCancelable(false);
                    nagDialog.setContentView(R.layout.click_img_view);
                    Button btnClose = (Button) nagDialog.findViewById(R.id.btnIvClose);
                    ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);

                    Glide.with(context)
                            .load(path)
                            .thumbnail(0.5f)
                            .into(ivPreview);
                    Log.d("Success","image_test_on_click::"+path);
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {

                            nagDialog.dismiss();
                        }
                    });
                    nagDialog.show();
                }
            });


        }
    }
}
