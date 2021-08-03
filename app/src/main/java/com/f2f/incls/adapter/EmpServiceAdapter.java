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

public class EmpServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<EmpServiceModel> serviceList;
    ArrayList<WarrantyListModel> arrayList;
    ArrayList<CustomerUploadModel>ImageList;
    ArrayList<String>imageStringList=new ArrayList<>();
    Fragment fragment;
    public EmpServiceAdapter(Context context, ArrayList<EmpServiceModel> serviceList,
                             ArrayList<WarrantyListModel> arrayList,
                             ArrayList<CustomerUploadModel> ImageList) {
        this.context=context;
        this.serviceList=serviceList;
        this.arrayList=arrayList;
        this.ImageList=ImageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myvieWHolder  viewHolder;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emp_service_list_view,parent,false);
        viewHolder=new myvieWHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final EmpServiceModel model=serviceList.get(position);
    //     final CustomerUploadModel model1=ImageList.get(position);
        Log.d("Success","Imagelist_size::"+ImageList.size());
        myvieWHolder vholder=(myvieWHolder)holder;
        String date=model.getDate();
        StringTokenizer str=new StringTokenizer(date);
        String date1=str.nextToken();

        final StringBuilder builder=new StringBuilder();
        String[] dt=date1.split("/");
        String y=dt[2];
        String m=dt[1];
        String d=dt[0];

        builder.append(d).append("/").append(m).append("/").append(y).toString();
     //   final String image=model1.getImage_path();
        String img=model.getPicture();

        if (img!=null) {
            Glide.with(context)
                    .load(img)
                    .fitCenter()
                    .into(vholder.emp_service_pic);
        }else{

            Glide.with(context)
                    .load(R.drawable.favicon)
                    .fitCenter()
                    .into(vholder.emp_service_pic);
        }
        vholder.emp_service_invc.setText(model.getInvoice());
        vholder.emp_service_date.setText(builder);
        vholder.emp_service_description.setText(model.getIssue());
        vholder.emp_service_ticket.setText(model.getTicket());
        vholder.emp_service_warranty.setText(model.getWarranty());
        String status=model.getStatus();
        Log.d("success","current_status2"+status);
        if (status.equals("1")) {
           vholder.current_status.setText("Complete");
           vholder.current_status.setTextColor(Color.parseColor("#06801a"));
        }
        if (status.equals("0")){
            vholder.current_status.setText("In-Progress");
            vholder.current_status.setTextColor(Color.parseColor("#ff7e00"));
        }if(status.equals("2")){
            vholder.current_status.setText("Pending");
            vholder.current_status.setTextColor(Color.parseColor("#fd0606"));
        }


        String warranty=model.getTicket();
        if(warranty!="null") {
            vholder.emp_service_warranty.setText(warranty);
        }else if(warranty==null){
            vholder.emp_service_warranty.setText("--");
        }
        else {
            vholder.emp_service_warranty.setText("--");
        }
       /* if (warranty.equalsIgnoreCase("Available") || warranty.equalsIgnoreCase("available")){
            vholder.emp_service_warranty.setTextColor(Color.parseColor("#06801a"));
        }else {
            vholder.emp_service_warranty.setTextColor(Color.parseColor("#fd0606"));
        }*/
      /*  if (status.equalsIgnoreCase("1")){
            vholder.status_color.setBackgroundColor(Color.parseColor("#06801a"));
        }else {
            vholder.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }
*/
        vholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    String image_type=model1.getType();
                String warranty=model.getWarranty();
                String status=model.getStatus();
                String inv_no=model.getInvoice();
                String tk_no=model.getTicket();
                String date=model.getDate();
                String pro_name=model.getProduct_name();
                String pro_des=model.getProduct_desc();
                String inv_amt=model.getInvoice_amount();
                String issue=model.getIssue();
           //     String image=model1.getImage_path();
                String at_name=model.getAttendant_name();
                String service_id=model.getService_id();
                String at_service_details=model.getService_details_id();
               // String image_id=model1.getId();

               // Log.d("Success","product_img_full_path::"+image);

              //  fragment=new TestFragment(null,null);
              fragment=new EmpEditServiceFragment(arrayList,ImageList);
                Bundle bundle=new Bundle();
                bundle.putString("warranty",warranty);
                bundle.putString("status",status);
                bundle.putString("invoice_no",inv_no);
                bundle.putString("ticket",tk_no);
                bundle.putString("date",date);
                bundle.putString("pro_name",pro_name);
                bundle.putString("pro_des",pro_des);
                bundle.putString("inv_amt",inv_amt);
                bundle.putString("issue",issue);
             //   bundle.putString("image",image);
                bundle.putString("at_name",at_name);
                bundle.putString("service_id",service_id);
                bundle.putString("service_details_id",at_service_details);
                bundle.putString("edit_service","Edit");
            //    bundle.putString("image_type",image_type);
               // bundle.putString("image_id",image_id);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container_emp, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {

        return serviceList.size();
    }
    public class myvieWHolder extends RecyclerView.ViewHolder{
        CircleImageView emp_service_pic;
        View status_color;
        TextView emp_service_invc,emp_service_date,
                emp_service_description,emp_service_ticket,emp_service_warranty,current_status;
        public myvieWHolder(@NonNull View view) {
            super(view);
            emp_service_pic=view.findViewById(R.id.emp_service_pic);
            emp_service_invc=view.findViewById(R.id.emp_service_invc);
            emp_service_date=view.findViewById(R.id.emp_service_date);
            emp_service_description=view.findViewById(R.id.emp_service_description);
            emp_service_ticket=view.findViewById(R.id.emp_service_tickets);
            emp_service_warranty=view.findViewById(R.id.emp_service_warranty);
            status_color=view.findViewById(R.id.status_colors);
            current_status=view.findViewById(R.id.currentstatuses_id);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Log.d("Success","Adapter_possition_test::"+pos);
                }
            });*/
        }
    }
}
