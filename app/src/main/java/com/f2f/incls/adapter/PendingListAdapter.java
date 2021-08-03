package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.f2f.incls.R;
import com.f2f.incls.model.PendingListModel;

import java.util.List;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class PendingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<PendingListModel> listModels;
    Context context;
     Fragment fragment;

    public PendingListAdapter(Context context,List<PendingListModel> listModels) {
        this.listModels=listModels;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyviewHolder viewHolder=null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_pending_list_view,parent,false);
        viewHolder=new MyviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
           final MyviewHolder viewholder = (MyviewHolder) holder;
        StringBuilder builder = new StringBuilder();
        final PendingListModel models = listModels.get(position);
        String dt = models.getDate();

        // String date = "yyyy-mm-dd hh:mm:ss";
        StringTokenizer tk = new StringTokenizer(dt);
        String date = tk.nextToken();  // <---  yyyy-mm-dd
        String time = tk.nextToken();

        String[] split = date.split("-");
        String y = split[0];
        String m = split[1];
        String d = split[2];

        builder.append(d).append("/").append(m).append("/").append(y).toString();
        String date1 = new String(builder);
        Log.d("success", "position11" + position);


      /*  final MyviewHolder holder1 = (MyviewHolder) holder;
        holder1.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        holder1.full_leads_date.setText(date1);
        holder1.full_leads_inv.setText(models.getInvoice());
        holder1.full_leads_issue.setText(models.getIssue());
*/
     /*   String cat_id=models.getCat_id();
        String contact_2=models.getMobile2();
        String des=models.getIssue();*/



    /*    Log.d("Success", "status_test::" + test);
       *//* if (test.equals("order_conform")){
            String text="Order Confirm";
            holder1.status_color.setBackgroundColor(Color.parseColor("#06801a"));
            holder1.full_leads_issue.setText(text);
            holder1.full_leads_issue.setTextColor(Color.parseColor("#06801a"));
        }*//*
        if (test.equals("leads_rejected") || test.equals("quotation_rejected")) {
            holder1.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
            if (test.equals("leads_rejected")) {
                String text = "Leads Rejected";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#fd0606"));
            } else {
                String text = "Quotation Rejected";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#fd0606"));
            }
        }
        if (test.equals("leads_follow_up") || test.equals("leads")
                || test.equals("quotation") || test.equals("quotation_follow_up")) {
            holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
            if (test.equals("leads_follow_up")) {
                String text = "Leads Follow Up";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#ff7e00"));
            } else if (test.equals("leads")) {
                String text = "Leads";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#ff7e00"));
            } else if (test.equals("quotation")) {
                String text = "Quotation";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#ff7e00"));
            } else if (test.equals("quotation_follow_up")) {
                String text = "Quotation Follow Up";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#ff7e00"));
            }
        }

*/

    //    viewholder.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
        viewholder.invoice_no.setText(models.getInvoice());
        viewholder.issue.setText(models.getIssue());
        viewholder.date.setText(date1);
      //  viewholder.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));


        String test = (models.getColor());
        Log.d("Success","status_test::"+test);
       if (test.equals("leads_rejected") || test.equals("quotation_rejected")){
            viewholder.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
            }
         if (test.equals("leads_follow_up") || test.equals("leads")
                || test.equals("quotation") || test.equals("quotation_follow_up")){
             viewholder.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }


        /*if (position %2==1) {
            viewholder.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
            viewholder.invoice_no.setText(models.getInvoice());
            viewholder.issue.setText(models.getIssue());
            viewholder.date.setText(date1);
            viewholder.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));


          *//*  if (test.equals("order_conform")){
                viewholder.status_color.setBackgroundColor(Color.parseColor("#06801a"));
            }  if (test.equals("leads_rejected") || test.equals("quotaion_rejected")){
                viewholder.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
            } if (test.equals("leads_follow_up") || test.equals("leads")
                    || test.equals("quotation") || test.equals("quotation_follow_up")){
                viewholder.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
            }*//*


        }else {
            viewholder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            viewholder.invoice_no.setText(models.getInvoice());
            viewholder.issue.setText(models.getIssue());
            viewholder.date.setText(date1);
            viewholder.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));

            *//*if (test.equals("order_conform")){
                viewholder.status_color.setBackgroundColor(Color.parseColor("#06801a"));
            }  if (test.equals("leads_rejected") || test.equals("quotaion_rejected")){
                viewholder.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
            } if (test.equals("leads_follow_up") || test.equals("leads")
                    || test.equals("quotation") || test.equals("quotation_follow_up")){
                viewholder.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
            }*//*

        }
*/
    }


    @Override
    public int getItemCount() {
        Log.d("Success","Array_Size::"+listModels.size());
        return listModels.size();

    }

    public static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView invoice_no,issue,date,full_leads_issue, full_leads_inv,full_leads_date;
        Toolbar dashboard_tool,warranty_tool,service_tool,leads_list_tool,add_leads_tool,edit_tool;
        View status_color;
        public MyviewHolder(View view) {
            super(view);
            invoice_no=view.findViewById(R.id.invoice_no);
            issue=view.findViewById(R.id.issue);
            date =view.findViewById(R.id.date);
            status_color=view.findViewById(R.id.status_color);
       //     full_leads_issue = view.findViewById(R.id.full_leadshome_issue);

        }
    }
}





//    LinearLayout customer_options;
 //   Toolbar dashboard_tool,warranty_tool,service_tool,leads_list_tool,add_leads_tool,edit_tool;


  /*  public MyViewHolder(View view) {
        super(view);
        Log.d("Success", "myviewholder::");
        full_leads_issue = view.findViewById(R.id.full_leads_issue);
        full_leads_date = view.findViewById(R.id.full_leads_date);
        full_leads_inv = view.findViewById(R.id.full_leads_inv);
        product_img = view.findViewById(R.id.product_img);
        status_color = view.findViewById(R.id.status_color);
        dashboard_tool=view.findViewById(R.id.dashboard_tool);
        customer_options=view.findViewById(R.id.customer_options);
        warranty_tool=view.findViewById(R.id.warranty_tool);
        service_tool=view.findViewById(R.id.service_tool);
        leads_list_tool=view.findViewById(R.id.leads_list_tool);
        add_leads_tool=view.findViewById(R.id.add_leads_tool);
        edit_tool=view.findViewById(R.id.edit_tool);
        //   itemView.setOnClickListener(this);
    }

*/