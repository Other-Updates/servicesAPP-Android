package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.fragment.EmpEditServiceFragment;
import com.f2f.incls.model.EmpServiceModel;
import com.f2f.incls.model.WarrantyListModel;
import com.f2f.incls.utilitty.CustomerUploadModel;

import java.util.ArrayList;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmpTodayTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<EmpServiceModel> todaytaskList;
    ArrayList<WarrantyListModel> arrayList;
    ArrayList<CustomerUploadModel> imageList;
    Fragment fragment;

    public EmpTodayTaskAdapter(Context context, ArrayList<EmpServiceModel> todaytaskList,
                               ArrayList<WarrantyListModel> arrayList,
                               ArrayList<CustomerUploadModel> imageList) {
        this.context=context;
        this.todaytaskList=todaytaskList;
        this.arrayList=arrayList;
        this.imageList=imageList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewhOLder viewhOLder=null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emp_todaytask_view,parent,false);
        viewhOLder=new ViewhOLder(view);
        return viewhOLder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewhOLder viewhOLder = (ViewhOLder) holder;
        final EmpServiceModel model = todaytaskList.get(position);
        WarrantyListModel model1 = null;
        if (!arrayList.isEmpty()) {
            try {
                model1 = arrayList.get(position);
                viewhOLder.emp_job.setText(model1.getProduct_name());
            }catch (Exception e){}

        }
        String image = model.getPicture();
        Log.d("Success", "image_path_test_today_adapter::" + image);
        if (image == null || image.isEmpty()) {
            viewhOLder.emp_today_task_list_pic.setImageResource(R.drawable.favicon);
        } else {
            Glide.with(context)
                    .load(image)
                    .thumbnail(0.5f)
                    .into(viewhOLder.emp_today_task_list_pic);
        }
        String dt = model.getDate();
        StringTokenizer tokenizer = new StringTokenizer(dt);
        String date = tokenizer.nextToken();
//      String hours = tokenizer.nextToken();

        StringBuilder builder = new StringBuilder();
        String[] dat = date.split("/");
        String y = dat[2];
        String m = dat[1];
        String d = dat[0];
Log.d("Success","daterttrrrt"+dat);
        builder.append(d).append("/").append(m).append("/").append(y).toString();
        String final_date = new String(builder);



        viewhOLder.emp_date.setText(final_date);
        viewhOLder.emp_des.setText(model.getIssue());
        viewhOLder.emp_ticket.setText(model.getTicket());


        String status = model.getStatus();
        Log.d("Success", "today_task_status::" + status);
        if (status.equalsIgnoreCase("2")) {
            viewhOLder.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
        } if (status.equalsIgnoreCase("0")) {
            viewhOLder.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        } if (status.equalsIgnoreCase("1")) {
            viewhOLder.status_color.setBackgroundColor(Color.parseColor("#06801a"));
        }/*else {
            viewhOLder.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
        }*/
        viewhOLder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String warranty = model.getWarranty();
                String status = model.getStatus();
                String inv_no = model.getInvoice();
                String tk_no = model.getTicket();
                String date = model.getDate();
                String pro_name = model.getProduct_name();
                String pro_des = model.getProduct_desc();
                String inv_amt = model.getInvoice_amount();
                String issue = model.getIssue();
                String image = model.getPicture();
                String at_name = model.getAttendant_name();
                String service_details_id = model.getService_details_id();
                String service_id = model.getService_id();

                fragment = new EmpEditServiceFragment(arrayList, imageList);
                Bundle bundle = new Bundle();
                bundle.putString("warranty", warranty);
                bundle.putString("status", status);
                bundle.putString("invoice_no", inv_no);
                bundle.putString("ticket", tk_no);
                bundle.putString("date", date);
                bundle.putString("pro_name", pro_name);
                bundle.putString("pro_des", pro_des);
                bundle.putString("inv_amt", inv_amt);
                bundle.putString("issue", issue);
                bundle.putString("image", image);
                bundle.putString("at_name", at_name);
                bundle.putString("service_details_id", service_details_id);
                bundle.putString("service_id", service_id);
                bundle.putString("edit_service", "Today");
                fragment.setArguments(bundle);
                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container_emp, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("Success","Array_Size::"+todaytaskList.size());
        return todaytaskList.size();
    }

    private class ViewhOLder extends RecyclerView.ViewHolder {
        CircleImageView emp_today_task_list_pic;
        View status_color;
        TextView emp_job,emp_date,emp_des,emp_ticket;
        public ViewhOLder(View view) {
            super(view);
            emp_today_task_list_pic=view.findViewById(R.id.emp_today_task_list_pic);
            emp_job=view.findViewById(R.id.emp_job);
            emp_date=view.findViewById(R.id.emp_date);
            emp_des=view.findViewById(R.id.emp_des);
            emp_ticket=view.findViewById(R.id.emp_ticket);
            status_color=view.findViewById(R.id.status_color);



        }
    }
}
