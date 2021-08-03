package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f2f.incls.R;
import com.f2f.incls.model.EmployeePendingServiceModel;

import java.util.ArrayList;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class EmpPendinglistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<EmployeePendingServiceModel>PendingList;
    Context context;
    public EmpPendinglistAdapter(Context context, ArrayList<EmployeePendingServiceModel> PendingList) {
        this.PendingList=PendingList;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myvIewholder viewholder=null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_pending_list_view,parent,false);
        viewholder=new myvIewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final myvIewholder viewholder=(myvIewholder)holder;
        EmployeePendingServiceModel model=PendingList.get(position);

        String old_date=model.getDate();
        StringTokenizer tokenizer=new StringTokenizer(old_date);
        String split_date=tokenizer.nextToken();
//        String hours=tokenizer.nextToken();

        StringBuilder builder=new StringBuilder();
        String[] dt=split_date.split("/");
        String y=dt[2];
        String m=dt[1];
        String d=dt[0];
      String final_date=builder.append(d).append("/").append(m).append("/").append(y).toString();

        viewholder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
       // viewholder.emp_invoice_no.setText(model.getTicket_no());
        viewholder.emp_issue.setText(model.getIssue());
        viewholder.emp_date.setText(final_date);
        viewholder.emp_status_color.setBackgroundColor(Color.parseColor("#ff7e00"));

        String status=model.getStatus();
        Log.d("success","current_status788"+status);
        if (status.equals("2")) {
            viewholder.emp_status_color.setBackgroundColor(Color.parseColor("#fd0606"));
        }else {
            viewholder.emp_status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }

      /*  if (position%2==1) {
        viewholder.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
        viewholder.emp_invoice_no.setText(model.getTicket_no());
        viewholder.emp_issue.setText(model.getIssue());
        viewholder.emp_date.setText(final_date);
        viewholder.emp_status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
    }else {
        viewholder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        viewholder.emp_invoice_no.setText(model.getTicket_no());
        viewholder.emp_issue.setText(model.getIssue());
        viewholder.emp_date.setText(final_date);
        viewholder.emp_status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
    }*/

}

    @Override
    public int getItemCount() {
        return PendingList.size();
    }

    private class myvIewholder extends RecyclerView.ViewHolder {
        View emp_status_color;
        TextView emp_invoice_no,emp_issue,emp_date;
        public myvIewholder(View view) {
            super(view);
            emp_status_color=view.findViewById(R.id.emp_status_color);
           // emp_invoice_no=view.findViewById(R.id.emp_invoice_no);
            emp_issue=view.findViewById(R.id.emp_issue);
            emp_date=view.findViewById(R.id.emp_date);
        }
    }
}
