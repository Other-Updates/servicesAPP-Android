package com.f2f.incls.adapter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.f2f.incls.Fullscreen_image;
import com.f2f.incls.R;
import com.f2f.incls.model.ServiceListModel;
import com.f2f.incls.model.WorkPerformedModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.graphics.Color.WHITE;

public class WorkperformedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<WorkPerformedModel> workList;
    Context context;
    public WorkperformedAdapter(Context context,ArrayList<WorkPerformedModel> workList) {
        this.workList=workList;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyviewHolder viewHolder=null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_workperformed,parent,false);
        viewHolder= new WorkperformedAdapter.MyviewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyviewHolder holder1 = (MyviewHolder) holder;
        WorkPerformedModel data = workList.get(position);
//        WorkPerformedModel data = workList.get(position);
        // holder.textView.setText(data);
       /* ((MyviewHolder) holder).workimage.setOnClickListener(new View.OnClickListener() {
            private ActionBar.Tab onItemClick;

            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.customer_workperformed);
                dialog.setTitle("Dialog Box");
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Android custom dialog example!");
                Button button = (Button) dialog.findViewById(R.id.workpicture);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });*//**/





        /* for (int i=0;i<2;i++) {*/



        /*}*/
       /* if (position != 0) {
            holder1.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
            holder1.service_date.setText("02/02/2021");
            holder1.work_performed.setText("repairing2");
            holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
            Log.d("success", "enter22" + holder1);
        }
        if (position != 0) {
            holder1.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
            holder1.service_date.setText("03/02/2021");
            holder1.work_performed.setText("repairing3");
            holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
            Log.d("success", "enter22" + holder1);
        }*/
         final WorkPerformedModel model = workList.get(position);
        Log.d("success", "positionOrder" + position);
        //  final   StringBuilder builder = new StringBuilder();





       /* String image1 = model.getEmp_upload_image();
        Log.d("Success", "image_path_test_today_adapter::" + image1);
        if (image1 == null || image1.isEmpty()) {
            holder1.workimage.setImageResource(R.drawable.favicon);
        } else {
            Glide.with(context)
                    .load(image1)
                    .thumbnail(0.5f)
                    .into(holder1.workimage);
        }
*/

        //    String dt = model.getService_date();
        String id = model.getservice_id();


        final StringBuilder builder = new StringBuilder();
        String dt = model.getService_date();
        StringTokenizer tk = new StringTokenizer(dt);
        // String date = "yyyy-mm-dd hh:mm:ss";
        String date = tk.nextToken();  // <---  yyyy-mm-dd
//        String time = tk.nextToken();

       /* String[] split = date.split("/");
        String y = split[0];
        String m = split[1];
        String d = split[2];
        String date1 = builder.append(d).append("/").append(m).append("/").append(y).toString();
*/
        Log.d ("success","fghhgh"+date);
String date1 = "";
        //  String mobi = "mobile_no";
        final String mobile = model.getAttendantmobile_number();
        final StringBuilder build = new StringBuilder();
        build.append(mobile).toString();


        String status = model.getStatus();
        Log.d("success", "current_status330" + status);
        if (status.equals("Completed")) {
            holder1.current_status.setText("Completed");
            holder1.current_status.setTextColor(Color.parseColor("#06801a"));
        } else if (status.equals("In-Progress")) {
            holder1.current_status.setText("In-Progress");
            holder1.current_status.setTextColor(Color.parseColor("#ff7e00"));
        } else if (status.equals("Pending")) {
            holder1.current_status.setText("Pending");
            holder1.current_status.setTextColor(Color.parseColor("#fd0606"));
        }

        holder1.service_date.setText(date);
//        holder1.current_status.setText(status);





        //     holder1.product_img.setImageResource(Integer.parseInt(model.getEmp_upload_image()));


//        Log.d("Success", "adapter_not_select_work_else::" + position);

        final String img1 = model.getEmp_upload_image();
        Log.d("success", "uploaddd" + img1);

        if (img1 != "null") {
            Glide.with(context)
                    .load(img1)
                    .fitCenter()
                    .into(holder1.workimage);
        } else {
            Glide.with(context)
                    .load(R.drawable.favicon)
                    .fitCenter()
                    .into(holder1.workimage);
        }

        String  attender =model.getAttendant_name();
        Log.d("success" , "aryeyghdg"+attender);
        if(attender!="null"){
            holder1.attendant_name.setText(model.getAttendant_name());
        }else{
            holder1.attendant_name.setText("--");
        }


        String  workperfom =model.getWork_performed();
        Log.d("success" , "aryeyghdg"+attender);
        if(workperfom!=null){
            holder1.work_performed.setText(model.getWork_performed());
        }else{
            holder1.work_performed.setText("--");
        }


        String  mobileno =mobile;
        Log.d("success" , "aryeyghdg"+attender);
        if(mobileno!="null"){
            holder1.workmobilenumber.setText(mobile);
        }else{
            holder1.workmobilenumber.setText("--");
        }

        holder1.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());

                final View dialogview = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialogbox, null);
                final ImageView dialog_img, closebutton;

                closebutton = (ImageView) dialogview.findViewById(R.id.close);
                //  closebutton=dialogview.findViewById(R.id.close);
                dialog_img = dialogview.findViewById(R.id.dialogbox);

                //dialog_img.(img1);
                //   dialog_img.setImageResource(getItemViewType(img1));
                //     int imageResource = context.getResources().getIdentifier(img1, null, context.getPackageName());
                //   Drawable res = context.getResources().getDrawable(R.drawable.camera_logo);
                //  dialog_img.setImageResource(img1);


                //   Button btn = (Button) dialoglayout.findViewById(R.id.custom_alert_dialog_horarios_btn_aceptar);

                Glide
                        .with(context)
                        .load(model.getEmp_upload_image())
                        .into(dialog_img);


                closebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogview, int id) {
                        dialogview.dismiss();

                    }
                });

                builder.setView(dialogview);
                builder.setCancelable(true);
                builder.show();
            }
        });
        }






        /*if (position != 0) {*/
        //   String work_not_select = model.getwork_not_select();
        //  Log.d("success", "adapter_not_select_work" + work_not_select);
       /* if (!work_not_select.equals("")) {
            if (work_not_select.equals("Select service")) {
                holder1.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
                holder1.service_date.setText("");
                holder1.current_status.setText("");
                holder1.work_performed.setText("");
                holder1.attendant_name.setText("");
                holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
                holder1.workmobilenumber.setText(" ");
           //     holder1.worklinear_bg.setBackgroundColor(Color.WHITE);

            }*//* if (position %2==0) {*/
      /*  if (id.equals("service_history")) {
            holder1.service_date.setText("");
            holder1.current_status.setText("");
            holder1.work_performed.setText("");
            holder1.attendant_name.setText("");
            holder1.workmobilenumber.setText("");
        } else {*/


        //     holder1.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));


        //    holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        //      holder1.worklinear_bg.setBackground(context.getDrawable(R.drawable.warranty_bg));

/*
            }else {
                holder1.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder1.service_date.setText("date1");
                holder1.current_status.setText("status");
                holder1.work_performed.setText("model.getWork_performed()");
                holder1.attendant_name.setText("model.getAttendant_name()");
                holder1.workmobilenumber.setText("mobile");
                holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
           //     holder1.worklinear_bg.setBackground(context.getDrawable(R.drawable.warranty_bg));
            //    Log.d("Success", "adapter_not_select_work_else_null::" + work_not_select);
        }*/
    /*    }else {
            holder1.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
            holder1.service_date.setText(date1);
            holder1.work_performed.setText(model.getWork_performed());
            holder1.attendant_name.setText(model.getAttendant_name());
            //     holder1.workmobilenumber.setText(model.getAttendantmobile_number());
            //    holder1.current_status.setText("Inprogress");
            holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }*/
        /* if (position % 2 == 1) {*/


        //   holder1.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));
         /*   holder1.service_date.setText("01/02/2021");
            holder1.work_performed.setText("repairing");
            holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
            Log.d("success","enter");*/

       /* } else {
            holder1.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder1.work_performed.setText("01/02/2021");
            holder1.service_date.setText("repairing");
            holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }*/




    @Override
    public int getItemCount() {
        Log.d("Success","worklist::"+workList.size());

        return  workList.size();


    }



    public static class MyviewHolder extends RecyclerView.ViewHolder {


        TextView service_date,work_performed,attendant_name,current_status,workmobilenumber;
        View status_color;
        LinearLayout worklinear_bg;
        ImageView workimage;
        public MyviewHolder(View view) {
            super(view);
        //    worklinear_bg=view.findViewById(R.id.worklinear);
            work_performed=view.findViewById(R.id.workperformedhistory);
            service_date=view.findViewById(R.id.workeddate);
            status_color=view.findViewById(R.id.statuses_color);
            attendant_name=view.findViewById(R.id.workattendant);
            workmobilenumber=view.findViewById(R.id.workmobilenumber);
            current_status=view.findViewById(R.id.currentstatuseswork);
            workimage=view.findViewById(R.id.workpicture);

           // product_img = view.findViewById(R.id.workpicture);

        }

    }


}
