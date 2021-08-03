package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.fragment.CustomerEditLeadsFragment;
import com.f2f.incls.model.AllLeadsListModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class FullLeadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<AllLeadsListModel> leadsList;
    CustomerEditLeadsFragment fragment;



    public FullLeadsAdapter(Context context, ArrayList<AllLeadsListModel> leadsList, CustomerEditLeadsFragment fragment) {
        this.context=context;
        this.leadsList=leadsList;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder viewHolder=null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_full_leads_view,parent,false);
      viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final AllLeadsListModel model = leadsList.get(position);
        String dt=model.getDate();

       StringBuilder builder=new StringBuilder();
       String[] split=dt.split("-");
       String y=split[0];
       String m=split[1];
       String d=split[2];
       builder.append(d).append("/").append(m).append("/").append(y).toString();
       String date=new String(builder);
        Log.d("Success","arraySize::"+leadsList.size());

        final MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder1.full_leads_date.setText(date);
            holder1.full_leads_inv.setText(model.getInvoince_no());
           holder1.full_leads_issue.setText(model.getIssue());
           holder1.leads_description.setText(model.getIssue());


            String cat_id=model.getCat_id();
            String contact_2=model.getMobile2();
            String des=model.getIssue();

            Log.d("Success","cat_id::"+cat_id);
            String test= (model.getColor());
            Log.d("Success","status_test::"+test);
           if (test.equals("order_conform")){
            String text="Order Confirm";
            holder1.status_color.setBackgroundColor(Color.parseColor("#06801a"));
            holder1.full_leads_issue.setText(text);
            holder1.full_leads_issue.setTextColor(Color.parseColor("#06801a"));
        }  if (test.equals("leads_rejected") || test.equals("quotation_rejected")){
            holder1.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
            if (test.equals("leads_rejected")){
                String text="Leads Rejected";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#fd0606"));
            }else {
                String text="Quotation Rejected";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#fd0606"));
            }
        } if (test.equals("leads_follow_up") || test.equals("leads")
                || test.equals("quotation") || test.equals("quotation_follow_up")){
            holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
            if (test.equals("leads_follow_up")){
                String text="Leads Follow Up";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#ff7e00"));
            }else if (test.equals("leads")){
                String text="Leads";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#ff7e00"));
            }else if (test.equals("quotation")){
                String text="Quotation";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#ff7e00"));
            }else if (test.equals("quotation_follow_up")){
                String text="Quotation Follow Up";
                holder1.full_leads_issue.setText(text);
                holder1.full_leads_issue.setTextColor(Color.parseColor("#ff7e00"));
            }
        }

        String image=model.getImage();
        if (image!=null) {
            Glide.with(context)
                    .load(image)
                    .fitCenter()
                    .into(holder1.product_img);
        }else{

            Glide.with(context)
                    .load(R.drawable.favicon)
                    .fitCenter()
                    .into(holder1.product_img);
        }
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder1.getAdapterPosition();
                    String cont=model.getMobile();
                    String cont_2=model.getMobile2();
                    String issue=model.getIssue();
                    String lead_id=model.getLeads_id();
                    String cat_id=model.getCat_id();
                    String invoice_no=model.getInvoince_no();
                    String status=model.getStatus();
                    Log.d("Sucees","Selected_pos::"+pos);
                    Log.d("Sucees","contact_1::"+cont);
                    Log.d("Sucees","contact_2::"+cont_2);
                    Log.d("Sucees","issue::"+issue);
                    Log.d("Sucees","leads_id::"+lead_id);
                    Log.d("Sucees","description::"+status);


                    fragment =new CustomerEditLeadsFragment();
                    Bundle args = new Bundle();
                    args.putString("possion", String.valueOf(pos));
                    args.putString("contact_1",cont);
                    args.putString("contact_2",cont_2);
                    args.putString("issue",issue);
                    args.putString("leads_id",lead_id);
                    args.putString("cat_id",cat_id);
                    args.putString("invoice_no",invoice_no);
                    args.putString("status",status);

                    fragment.setArguments(args);
                    FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, fragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

    }

    @Override
    public int getItemCount() {
        Log.d("Success", "inside_lead_size::"+leadsList.size());
        return leadsList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener */{
        View status_color;
        ImageView product_img;
        LinearLayout customer_options;
        Toolbar dashboard_tool,warranty_tool,service_tool,leads_list_tool,add_leads_tool,edit_tool;
        TextView full_leads_inv, full_leads_date, full_leads_issue,leads_description;

        public MyViewHolder(View view) {
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
            leads_description=view.findViewById(R.id.full_leads_description);
            //   itemView.setOnClickListener(this);
        }

      /*  @Override
        public void onClick(View v) {
            Log.d("Success", "lead_size::"+leadsList.size());

leadsList.size();
                    fragment =new CustomerEditLeadsFragment();
                    Bundle args = new Bundle();
                    args.putString("data", "This data has sent to FragmentTwo");
                    fragment.setArguments(args);
                    FragmentTransaction transaction = ((FragmentActivity)context)
                            .getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, fragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(null);
                    transaction.commit();

        }*/
    }
}
