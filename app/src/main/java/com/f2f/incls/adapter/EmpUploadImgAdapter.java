package com.f2f.incls.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.model.EmpUploadImgModel;
import com.f2f.incls.model.TestModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmpUploadImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<EmpUploadImgModel> empuploadList;
    ArrayList<TestModel> new_empuploadList;
    public EmpUploadImgAdapter(Context context,
                               ArrayList<EmpUploadImgModel> empuploadList) {
        this.context=context;
        this.empuploadList=empuploadList;

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
        EmpUploadImgModel model=empuploadList.get(position);
        final viewholder viewholder=(EmpUploadImgAdapter.viewholder)holder;
        String image=model.getImage_path();





        if (image!=null)

    {
        Glide.with(context)
                .load(image)
                .thumbnail(0.5f)
                .into(viewholder.emp_upload);
    }else{
            Glide.with(context)
                    .load(R.drawable.favicon)
                    .fitCenter()
                    .into(viewholder.emp_upload);
        }

        String id=model.getId();
        String service_id=model.getService_id();
        //  String path=model.getImage_path();
        Log.d("Adapter","image_id_test::"+image);
        Log.d("Adapter","image_service_id_test::"+service_id);
        Log.d("Adapter","employee_list_size_new::"+empuploadList.size());
        /*if (id.equals(service_id)) {
            String path=model.getImage_path();
            String id_inside=model.getService_id();
            Log.d("Adapter","adapter_image_path_inside::"+path);
            Log.d("Adapter","adapter_seervice_id_inside::"+id_inside);
            Glide.with(context)
                    .load(path)
                    .thumbnail(0.5f)
                    .into(viewholder.emp_upload);
        }*/
        viewholder.img_close.setVisibility(View.GONE);

      /*  viewholder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (v.getId()==viewholder.img_close.getId()){
                        empuploadList.remove(viewholder.getAdapterPosition());
                        notifyDataSetChanged();
                        Log.d("Success","holder_possition::"+viewholder.getPosition());
                        Log.d("Success","view_id::"+v.getId());
                        Log.d("Success","holder_id::"+viewholder.img_close.getId());
                    }
                }catch (Exception e){}
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return empuploadList.size();
    }
    public class viewholder extends RecyclerView.ViewHolder{
        CircleImageView emp_upload;
        ImageView img_close;
        public viewholder(@NonNull View view) {
            super(view);
            emp_upload=view.findViewById(R.id.emp_upload);
            img_close=view.findViewById(R.id.img_close);
        }
    }
}
