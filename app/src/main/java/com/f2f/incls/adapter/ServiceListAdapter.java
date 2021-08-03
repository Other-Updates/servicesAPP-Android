package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f2f.incls.R;
import com.f2f.incls.model.PendingListModel;
import com.f2f.incls.model.ServiceListModel;

import java.util.List;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ServiceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ServiceListModel> serviceList;
    Context context;
    public ServiceListAdapter(Context context,List<ServiceListModel> serviceList) {
        this.serviceList=serviceList;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyviewHolder viewHolder=null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_service_list_view,parent,false);
        viewHolder=new MyviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyviewHolder holder1 = (MyviewHolder) holder;
        ServiceListModel model = serviceList.get(position);
        StringBuilder builder=new StringBuilder();
        String dt=model.getService_date();

        // String date = "yyyy-mm-dd hh:mm:ss";
        StringTokenizer tk = new StringTokenizer(dt);
        String date = tk.nextToken();  // <---  yyyy-mm-dd
        String time = tk.nextToken();

        Log.d("Success","date_test::"+dt);
        String[] split=date.split("-");
        String y=split[0];
        String m=split[1];
        String d=split[2];
        Log.d("Success","date_test_year::"+y);
        Log.d("Success","date_test_month::"+m);
        Log.d("Success","date_test_day::"+d);

        builder.append(d).append("/").append(m).append("/").append(y).toString();
        String date1=new String(builder);


   //     holder1.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
        holder1.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
       // holder1.tn_no.setText(model.getTn_no());
        holder1.service_issue.setText(model.getService_issue());
        holder1.service_date.setText(date1);
        holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));

        String status=model.getColor();
        Log.d("success","current_status788"+status);
        if (status.equals("2")) {
            holder1.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
        }else {
            holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }
     /*   if (status.equals("1")) {
            holder1.status_color.setText("Complete");
            holder1.status_color.setTextColor(Color.parseColor("#ff7e00"));
        }*/
       /* if (status.equals("Inprogress")){
            holder1.status_color.setText("Inprogress");
            holder1.status_color.setTextColor(Color.parseColor("#ff7e00"));
        }if(status.equals("Pending")){
            holder1.status_color.setText("Pending");
            holder1.status_color.setTextColor(Color.parseColor("#fd0606"));
        }*/

       /* if (status.equals("2")) {
            holder1.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
        }
        if (status.equals("0")){
            holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }*/

        /*if (status.equals("Inprogress")||status.equals("Inprogress")){
            holder1.status_color.setBackgroundColor(Color.parseColor("#06801a"));
        }
        else {
            holder1.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
        }*/

        //     holder1.status_color.setBackgroundColor(Color.parseColor("#fd0606"));

      /*  if (position % 2 == 1) {

            holder1.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
            holder1.tn_no.setText(model.getTn_no());
            holder1.service_issue.setText(model.getService_issue());
            holder1.service_date.setText(date1);
            holder1.status_color.setBackgroundColor(Color.parseColor("#fd0606"));


        }else {
            holder1.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder1.tn_no.setText(model.getTn_no());
            holder1.service_issue.setText(model.getService_issue());
            holder1.service_date.setText(date1);
            holder1.status_color.setBackgroundColor(Color.parseColor("#fd0606"));
        }*/



    }

    @Override
    public int getItemCount() {
        Log.d("Success","Array_Size::"+serviceList.size());
        return serviceList.size();

    }

    public static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tn_no,service_issue,service_date;
        View status_color;
        public MyviewHolder(View view) {
            super(view);
          //  tn_no=view.findViewById(R.id.tn_no);
            service_issue=view.findViewById(R.id.services_issue);
            service_date=view.findViewById(R.id.services_date);
            status_color=view.findViewById(R.id.statuss_color);


        }
    }
}
