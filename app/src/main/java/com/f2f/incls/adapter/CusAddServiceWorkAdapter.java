package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.model.CustomerAddServiceWorkModel;
import com.f2f.incls.model.EmpUploadImgModel;
import com.f2f.incls.model.TestModel;

import java.util.ArrayList;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CusAddServiceWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<CustomerAddServiceWorkModel> workList;
    ArrayList<EmpUploadImgModel> new_empuploadList=new ArrayList<>();
    public CusAddServiceWorkAdapter(Context context,
                                    ArrayList<CustomerAddServiceWorkModel> workList) {
        this.context=context;
        this.workList=workList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_add_service_work_performed_view,parent,false);
        viewholder viewholder=new viewholder(view);
        return viewholder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        viewholder hold = (viewholder) holder;
        CustomerAddServiceWorkModel model = workList.get(position);
        EmpUploadImgModel model2=new EmpUploadImgModel();
       Log.d("Success","Array_test::"+model.getEmpuploadList().size());
       //  EmpUploadImgModel img_model = empuploadList.get(position);


        String select = model.getInv_select();
        String date = model.getDate();
        String status = model.getStatus();
        String image=model.getImage_path();

        LinearLayoutManager lrt=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        hold.work_per_rec.setLayoutManager(lrt);
        imageAdapter adapter=new imageAdapter(context,model.getWarrantyList());
        hold.work_per_rec.setAdapter(adapter);
        
        if (status.equals("1")) {
            hold.at_status_tv.setText("Completed");
            hold.at_status_tv.setTextColor(Color.parseColor("#06801a"));
        } if (status.equals("2")) {
            hold.at_status_tv.setText("Pending");
            hold.at_status_tv.setTextColor(Color.parseColor("#fd0606"));
        }if (status.equals("0")) {
            hold.at_status_tv.setText("Inprogress");
            hold.at_status_tv.setTextColor(Color.parseColor("#ff7e00"));
        }
        StringTokenizer tokenizer = new StringTokenizer(date);
        String dat = tokenizer.nextToken();
        String num = tokenizer.nextToken();
        StringBuilder builder = new StringBuilder();
        String[] old_date = dat.split("-");
        String y = old_date[0];
        String m = old_date[1];
        String d = old_date[2];
        String new_date = builder.append(d).append("/").append(m).append("/").append(y).toString();
        Log.d("Success", "select_invoice_test::" + status);
        if (select.equals("Select Invoice")) {
            hold.attender_name.setText("");
            hold.inv_date.setText("");
            hold.work_issue.setText("");
        } else {
            hold.attender_name.setText(model.getAttender_name());
            hold.inv_date.setText(new_date);
            hold.work_issue.setText(model.getIssue());
        }
    }
    @Override
    public int getItemCount() {
        Log.d("Success","Customer_add_service_return::"+workList.size());
        return workList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView attender_name,inv_date,at_status_tv,work_issue;
        CircleImageView emp_upload;
        RecyclerView work_per_rec;
        public viewholder(@NonNull View view) {
            super(view);
            findviewrec(view);
        }
        private void findviewrec(View view) {
            attender_name=view.findViewById(R.id.attendent_name);
            inv_date=view.findViewById(R.id.inv_date);
            at_status_tv=view.findViewById(R.id.at_status_tv);
            work_issue=view.findViewById(R.id.work_issue);
            work_per_rec=view.findViewById(R.id.work_per_rec);

        }
    }
}
