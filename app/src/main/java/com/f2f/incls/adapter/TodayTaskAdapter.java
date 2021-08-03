package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f2f.incls.R;
import com.f2f.incls.model.PendinTodayTaskModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class TodayTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<PendinTodayTaskModel> todataskList;
    Fragment fragment;
    public TodayTaskAdapter(Context context, ArrayList<PendinTodayTaskModel> todataskList) {
        this.context=context;
        this.todataskList=todataskList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Myviewholder viewholder=null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emp_pending_today_task,parent,false);
        viewholder=new Myviewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Myviewholder viewholder=(Myviewholder)holder;
        PendinTodayTaskModel model=todataskList.get(position);
        String old_date=model.getDate();
     //   old_date= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String issue=model.getIssue();
        Log.d("Success","issues_test_today::"+issue);
        StringTokenizer tokenizer=new StringTokenizer(old_date);
        String split_date=tokenizer.nextToken();
     //   String hours=tokenizer.nextToken();

        StringBuilder builder=new StringBuilder();
        String[] dt=split_date.split("/");
        String y=dt[2];
        String m=dt[1];
        String d=dt[0];

   //    String  final_date=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String   final_date=builder.append(d).append("/").append(m).append("/").append(y).toString();


      //  String date_n=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
       // viewholder.today_task_date.setText(date_n);



    //    viewholder.today_task_date.getText().toString();

        viewholder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        viewholder.today_task_date.setText(final_date);
        viewholder.today_task_issue.setText(model.getIssue());
      //  viewholder.today_task_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        String status=model.getStatus();
        Log.d("success","current_status788"+status);
        if (status.equals("2")) {
            viewholder.today_task_color.setBackgroundColor(Color.parseColor("#fd0606"));
        }else {
            viewholder.today_task_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }


      /*  if (position%2==1) {
            viewholder.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
            viewholder.today_task_date.setText(final_date);
           // viewholder.today_task_date.getText().toString();
            viewholder.today_task_issue.setText(model.getIssue());
            viewholder.today_task_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }else {
            viewholder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            viewholder.today_task_date.setText(final_date);
            viewholder.today_task_issue.setText(model.getIssue());
            viewholder.today_task_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }*/

       /* viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Success","today_task_adpater::");
                fragment=new EmpEditServiceFragment();
                FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container_emp, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        Log.d("Success","retur_array::"+todataskList.size());
        return todataskList.size();
    }

    private class Myviewholder extends RecyclerView.ViewHolder {
        TextView today_task_date,today_task_issue;
        View today_task_color;
        public Myviewholder(@NonNull View view) {
            super(view);
            today_task_date=view.findViewById(R.id.today_task_date);
            today_task_issue=view.findViewById(R.id.today_task_issue);
            today_task_color=view.findViewById(R.id.today_task_color);
        }
    }
}
