package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.fragment.CustomerEditServiceFragment;
import com.f2f.incls.model.AllServiceModel;
import com.f2f.incls.model.EmpServiceModel;
import com.f2f.incls.model.WorkPerformedModel;
import com.f2f.incls.utilitty.CustomerUploadModel;
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class AllServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<AllServiceModel> arrayList;
    ArrayList<AllServiceModel>serviceList;
    ArrayList<WorkPerformedModel>workList;



    Fragment fragment;

    ArrayList<String>imageList;
        public AllServiceAdapter(Context context, ArrayList<AllServiceModel> arrayList,ArrayList<WorkPerformedModel>workList)
                             {
        this.context=context;
        this.arrayList=arrayList;
        this.workList=workList;

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myViewHolder viewHolder=null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_full_service_view,parent,false);
        viewHolder=new AllServiceAdapter.myViewHolder(view);
        return viewHolder;
    }






    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
 //      final CustomerUploadModel model1=ImageList.get(position);
       final  AllServiceModel model=arrayList.get(position);
//       final WorkPerformedModel model1=workList.get(position);
      //  model = arrayList.get(position);
        myViewHolder holder1 = (myViewHolder) holder;

        final StringBuilder builder=new StringBuilder();
        String dt=model.getDate();

        // String date = "yyyy-mm-dd hh:mm:ss";
        StringTokenizer tk = new StringTokenizer(dt);
        String date = tk.nextToken();  // <---  yyyy-mm-dd
    //    String time = tk.nextToken();

        String[] split=date.split("/");
        String y=split[2];
        String m=split[1];
        String d=split[0];
        builder.append(d).append("/").append(m).append("/").append(y).toString();
        String date1=new String(builder);

        String status=model.getStatus();
        Log.d("success","current_status2"+status);
        if (status.equals("1")) {
            holder1.current_status.setText("Complete");
            holder1.current_status.setTextColor(Color.parseColor("#06801a"));
        }
        if (status.equals("0")){
            holder1.current_status.setText("In-Progress");
            holder1.current_status.setTextColor(Color.parseColor("#ff7e00"));
        }if(status.equals("2")){
            holder1.current_status.setText("Pending");
            holder1.current_status.setTextColor(Color.parseColor("#fd0606"));
        }



       /* String warranty=model.getWarranty();
        Log.d("Success","warranty_test::"+warranty);
            if (warranty.equals("Available")||warranty.equals("available")){
                holder1.warranty_id.setTextColor(Color.parseColor("#06801a"));
            }
            else {
                holder1.warranty_id.setTextColor(Color.parseColor("#fd0606"));
            }*/
      /*  if (status.equals("1")) {
            holder1.status_color.setBackgroundColor(Color.parseColor("#06801a"));
        } else {
            holder1.status_color.setBackgroundColor(Color.parseColor("#ff7e00"));
        }*/
        Log.d("Success","Warranty_text::"+model.getWarranty());
      //      holder1.warranty_id.setText(model.getWarranty());
            holder1.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
    //        holder1.service_invc.setText(model.getInvoince());
            holder1.service_date.setText(date1);
            holder1.full_leads_issue.setText(model.getIssue());
      //      holder1.ticket_id.setText(model.getTicket_number());
       //     holder1.att_name.setText(model.getAttendant_name());




         //   holder1.current_status.setText(model.getStatus());
         //   holder1.status_color.setBackgroundColor(Color.parseColor("#06801a"));
           /* String mobi="mobile No:";
            final String mobile=model.getAttendant_mobile();
            final StringBuilder build=new StringBuilder();
            final String at_mobile_no=build.append(mobi).append(mobile).toString();*/


        String mobile = model.getAttendantmobile_number();
        final StringBuilder build = new StringBuilder();
        build.append(mobile).toString();

          String img=model.getProduct_img();
          String img1=model.getEmp_upload_image();

       /*


            imageList=model.getImageList();
            String img=imageList.get(position);*/
            Log.d("Success","service_list_image_path::"+img);



        if (img!=null) {
            Glide.with(context)
                    .load(img)
                    .fitCenter()
                    .into(holder1.product_img);
        }else{

            Glide.with(context)
                    .load(R.drawable.favicon)
                    .fitCenter()
                    .into(holder1.product_img);
        }

       // holder1.att_name.setText(model.getAttendant_name());
        String  attender =model.getAttendant_name();
        Log.d("success" , "aryeyghdg"+attender);
        if(attender!="null"){
            holder1.att_name.setText(model.getAttendant_name());

        }else{
            holder1.att_name.setText("--");
        }
        /*  if (img==null){
                holder1.product_img.setImageResource(0);
                holder1.product_img.destroyDrawingCache();
                holder1.product_img.setImageResource(R.drawable.favicon);
            }else{
                Glide.with(context)
                        .load(img)
                        .thumbnail(0.5f)
                        .into(holder1.product_img);
            }*/

           /* holder1.att_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tooltip.Builder builder = new Tooltip.Builder(v)
                            .setCancelable(true)
                            .setCornerRadius(20f)
                            .setTextColor(Color.WHITE)
                            .setBackgroundColor(Color.parseColor("#0250e1"))
                            .setText(at_mobile_no
                            );
               //     String image_type = model1.getType();
                    String warranty = model.getWarranty();
                    String status = model.getStatus();
             //       String inv_no = model.getInvoice();
           //         String tk_no = model.getTicket();
                    String date = model.getDate();
            //        String pro_name = model.getProduct_name();
            //        String pro_des = model.getProduct_desc();
             //       String inv_amt = model.getInvoice_amount();
                    String issue = model.getIssue();
               //     String image = model1.getImage_path();
                    String at_name = model.getAttendant_name();
             //       String service_id = model.getService_id();
              //      String at_service_details = model.getService_details_id();

                    builder.show();
*/

                    holder1.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String image_type=model.getProduct_img();
                            String warranty=model.getWarranty();
                            String status=model.getStatus();
                            String inv_no=model.getInvoince();
                            String tk_no=model.getTicket_number();
                            String date=model.getDate();
                            String service_id=model.getservice_id();
                            String pro_name=model.getProduct_name();
                            String pro_des=model.getProduct_desc();
                            String inv_amt=model.getinvoince_amount();
                            String issue=model.getIssue();
                         //   String image=model.getProduct_img();
                            String at_name=model.getAttendant_name();
                            String at_service_details=model.getService_details_id();

                       //     String image_id=model.getEmp_upload_image();
                          //  String work_performed=model.getWorkperformed();

                            String name=model.getAttendant_namework();
                            String mobile=model.getAttendant_mobile();
                        //    String emp_image=model.getEmp_upload_image();
                     //       String hisdate=model.getService_date();
                          //  String current_status1=model.getStatuses();
                            String workperformed3=model.getWork_performed();
                          //  String service_history=model.getservice_history();




                    fragment=new CustomerEditServiceFragment(arrayList,workList);
                    Bundle bundle=new Bundle();
                    bundle.putString("warranty",warranty);
                    bundle.putString("status",status);
                    bundle.putString("invoice_no",inv_no);
                    bundle.putString("ticket_no",tk_no);
                    bundle.putString("date",date);
                  //  bundle.putString("serid",service_id);
                    bundle.putString("pro_name",pro_name);
                    bundle.putString("pro_des",pro_des);
                    bundle.putString("inv_amt",inv_amt);
                    bundle.putString("issue",issue);
                 //   bundle.putString("image",image);
                    bundle.putString("at_name",at_name);
                    bundle.putString("service_id",service_id);
                    bundle.putString("service_details_id",at_service_details);
                    bundle.putString("edit_service","Edit");
                    bundle.putString("customer_image_upload",image_type);
                 //   bundle.putString("employee_image_upload",image_id);
                    bundle.putString("name",name);
                 //   bundle.putString("mobile_no",mobile_no);
                 //   bundle.putString("emp_image",emp_image);
               //     bundle.putString("created_date",hisdate);
                //    bundle.putString("work_status",current_status1);
                  //  bundle.putString("",current_status1);
                    bundle.putString("work_performed",workperformed3);
               //     bundle.putString("service_history",service_history);
                    bundle.putString("attendant_mobile_no",mobile);


                  //  bundle.putString("work_performed",work_performed);
                    fragment.setArguments(bundle);
                    Log.d("success","customer000"+bundle);





                            FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_container, fragment);
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                }
                @Override
                public int getItemCount() {
                    return arrayList.size();
                }

                private class myViewHolder extends RecyclerView.ViewHolder {
                    View status_color;
                    ImageView product_img;
                    TextView service_invc, service_date, full_leads_issue, warranty_id, ticket_id, att_name,current_status,work_performedhistory;

                    public myViewHolder(View view) {
                        super(view);
                    //    status_color = view.findViewById(R.id.status_color);
                        product_img = view.findViewById(R.id.product_img);
                     //   service_invc = view.findViewById(R.id.service_invc);
                        service_date = view.findViewById(R.id.service_date);
                        full_leads_issue = view.findViewById(R.id.full_leads_issue);
                   //     ticket_id = view.findViewById(R.id.ticket_id);
                    //    warranty_id = view.findViewById(R.id.warranty_id);
                        att_name = view.findViewById(R.id.att_name);
                        current_status=view.findViewById(R.id.currentstatus_id);
                  //      work_performedhistory=view.findViewById(R.id.workperformed_rec);


                    }
                }
            }
