package com.f2f.incls.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.activity.CustomerDashBoard;
import com.f2f.incls.adapter.AllServiceAdapter;
import com.f2f.incls.adapter.CustomerUploadImgAdapter;
import com.f2f.incls.adapter.WarrantyListAdapter;
import com.f2f.incls.adapter.WorkperformedAdapter;
import com.f2f.incls.model.AllServiceModel;
import com.f2f.incls.model.EmpStatusUpdateModel;
import com.f2f.incls.model.EmpUploadImgModel;
import com.f2f.incls.model.WorkPerformedModel;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.CustomerUploadModel;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringTokenizer;

import pl.droidsonroids.gif.GifImageView;

import static com.f2f.incls.utilitty.SessionManager.KEY_MOBILE;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;


@SuppressLint("ValidFragment")
public class CustomerEditServiceFragment extends Fragment implements LoadinInterface {


    TextView no_service_data;

    android.app.Fragment fragment;
    ImageView lead_power, add_image_leads, ad_img_close, raise_ticket, add_leads,phone_call;
    RelativeLayout ads_img_ly;
    LinearLayout emp_edit_ly;
    LinearLayout emp_layout, emp_edit_service_rec_linear;

    String cus_id, adapter_id;
    ImageView photo_upload;
    Spinner status_spinner;
    RecyclerView emp_work_id;
    private String userChoosenTask;
    RecyclerView emp_edit_service_rec, emp_status_update_rec, upload_img_show_reg, emp_img_upload_rec, warranty_recycler, add_service_work_rec;
    TextView emp_edit_about_issue, warranty_tv, current_status, edit_invoice, edit_ticket1, edit_date,attendermob,mobile,
            edit_invoice_amt, attender_id, task_date, Warranty, work_performed1, service_date, status_color;

    TextView invoice_amt, warranty_status, warranty_invoice_id, warranty_ticket_id,
            warranty_date_id, expiry_id, attendent_name, inv_date, at_status, paid_service_tv, project_des;
    public static String b_warranty, b_status, b_inv_no, b_ticket, b_date, b_pro_name, work_performed2, work_performed, attendant, emp_image, workstatus, mobile_no, created_date,
            b_pro_des, b_inv_amt, b_issue, b_image, b_at_name,b_mobile_no,
            b_service_details, b_service_id, image_type, image_id;
    SessionManager session;
    String imagePath, store_path, id, type, click_pos, date1, spinner_value;
    public static String customer_type, invoince, date, issue, serid, customer, ticket_number, attendant_mobile, warranty, attendant_name, product_img, status;
    //  String[] task_status = {"Select Status","Inprogress", "Complete"};
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int STORAGE_PERMISSION_CODE = 1;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    GifImageView loader_img;
    Context context;


    ArrayList<EmpUploadImgModel> empuploadArrayList = new ArrayList<>();


    ArrayList<EmpStatusUpdateModel> statusList = new ArrayList<>();
    // ArrayList<EmpSpinnerModel> testList=new ArrayList();
    //  ArrayList<AllServiceModel> uploadArrayList=new ArrayList<AllServiceModel>();
    ArrayList<AllServiceModel> uploadArrayList = new ArrayList<>();
    ArrayList<String> imageselectList = new ArrayList<>();
    private Object container;

    ArrayList<AllServiceModel> arrayList;
    //  ArrayList<WorkPerformedModel> workList=new ArrayList<WorkPerformedModel>();
    ArrayList<CustomerUploadModel> ImageList;
  //  ArrayList<WorkPerformedModel> uploadarrayList = new ArrayList<>();
    ArrayList<WorkPerformedModel> workList = new ArrayList<>();
    // ArrayList<WorkPerformedModel> workList;


    public CustomerEditServiceFragment(ArrayList<AllServiceModel> arrayList, ArrayList<WorkPerformedModel> workList) {
        this.arrayList = arrayList;
        this.workList = workList;
        Log.d("Success", "Construct_test::" + this.arrayList.size());

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CustomerDashBoard) Objects.requireNonNull(getActivity())).allservicelist();
        findviewid(view);
        ((CustomerDashBoard) getActivity()).adsimageget();
        ((CustomerDashBoard) getActivity()).adsclose();

        dialog = new ProgressDialog(getActivity());
        HashMap<String, String> user = session.getcustomerdetails();
        final String customer_id = user.get(SessionManager.KEY_USER_ID);
        HashMap<String, String> user1 = session.getempdetails();
        String emp_id = user1.get(SessionManager.KEY_USER_ID);
        String cutomer_type = "customer";
        String services_id =getArguments().getString("service_id");

        Log.d("Success", "cus_ser_id::" + customer_id);
        Log.d("Success", "ser_vis_id::" + services_id);


       /* Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:9078784565"));
        if (ActivityCompat.checkSelfPermission(SOSCallHelp.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)

        {
            return;
        }
        startActivity(callIntent);*/

      /*  edit_invoice_amt.setText("ghhja");
        edit_invoice.setText(invoince);
        current_status.setText("fhf");
        emp_edit_about_issue.setText(issue);*/
/*
        try {
            workperformedhistory(customer_id,emp_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

       /* try {
            ServiceListMethod(customer_id,cutomer_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/





/*
        phone_call.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        call.setData(Uri.parse("tel:" + b_mobile_no));
         if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {
             return;
            *//* getActivity().*//*
         }
        startActivity(call);
    }
});*/


        Bundle bundle = getArguments();
        String edit = bundle.getString("edit_service");
        Log.d("Success", "edit_service_test::" + edit);
        if (edit.equals("Edit")) {
            Service_to_edit(bundle);
        }
        try {
            workperformedhistory(customer_id,cutomer_type,services_id,emp_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




   /* private void workperformedhistory(String emp_id, String id) throws JSONException {
        JSONObject object=new JSONObject();
        String type="customer";
        object.put("customer_id",id);
        object.put("emp_id",emp_id);
        object.put("service_type",type);
        Log.d("Success:::::", "pending_service_list_req::::: " + object);
        background(Constants.CUS_SERVICE_LIST,object);
    }*/

   /* private void background(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
                    @Override
                    public void onError(String message, VolleyError error) {
                    }

                    @Override
                    public String onResponse(JSONObject response) throws JSONException {
                        Log.d("Success", "workperformed_history::" + response);
                        if (response.getString("status").equalsIgnoreCase("Success")) {
                            JSONArray array = response.getJSONArray("data");
                            workList = new ArrayList();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                JSONArray array1 = obj.getJSONArray("service_history");
                                for (int k = 0; k < array1.length(); k++) {
                                    JSONObject obj1 = array1.getJSONObject(k);
                                    Log.d("Success", "service_history12::" + obj1);
                                    WorkPerformedModel workLists = new WorkPerformedModel();
                                    workLists.setAttendant_name(obj1.getString("name"));
                                    workLists.setWork_performed(obj1.getString("work_performed"));
                                    workLists.setService_date(obj1.getString("created_date"));
                                    workLists.setAttendantmobile_number(obj1.getString("mobile_no"));
                                    workLists.setStatus(obj1.getString("work_status"));
                                    workLists.setEmp_upload_image(obj1.getString("emp_image"));
                                    workList.add(workLists);
                                }
                            }

                            LinearLayoutManager lr = new LinearLayoutManager(getActivity());
                            emp_work_id.setLayoutManager(lr);
                            WorkperformedAdapter adapter = new WorkperformedAdapter(getActivity(), workList);
                            emp_work_id.setAdapter(adapter);


                        }
                        return null;
                    }

                });
    }

*/   /* private void ServiceListMethod(String id,String cutomer_type)throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("service_id",id);
      //  object.put("service_type",cutomer_type);
        Log.d("Success::", "Service_request_object123::" + object);
         SendinBackground(Constants.EMP_EDIT_SERVICE,object);

    }*/






    /*private void workperformed(String b_inv_no, String emp_id) {
        JSONObject object=new JSONObject();
        //   object.put("")
    }
*/


    private void findviewid(View view) {
        status_spinner = view.findViewById(R.id.emp_status_spinner);
       // emp_edit_service_rec = view.findViewById(R.id.emp_edit_service_rec);
        loader_img = view.findViewById(R.id.loader_img);
        emp_layout = view.findViewById(R.id.emp_layout);
        //    emp_status_update_rec=view.findViewById(R.id.emp_status_update_rec);
        emp_edit_about_issue = view.findViewById(R.id.emp_edit_about_issue);
       // emp_edit_ly = view.findViewById(R.id.emp_edit_ly);
        warranty_tv = view.findViewById(R.id.warranty_tv);
        current_status = view.findViewById(R.id.current_status);
        edit_invoice = view.findViewById(R.id.edit_invoice);
        edit_ticket1 = view.findViewById(R.id.edit_ticket1);
        edit_date = view.findViewById(R.id.edit_date);
     //   edit_invoice_amt = view.findViewById(R.id.edit_invoice_amt);
        //emp_edit_service_rec_linear=view.findViewById(R.id.emp_edit_service_rec_linear);
        upload_img_show_reg = view.findViewById(R.id.upload_img_show_reg);
        attender_id = view.findViewById(R.id.attender_id1);
        task_date = view.findViewById(R.id.task_date11);
        photo_upload = view.findViewById(R.id.photo_upload);
        emp_img_upload_rec = view.findViewById(R.id.emp_img_upload_rec);
        emp_work_id = view.findViewById(R.id.workperformed_rec);
        //    work_performed1=view.findViewById(R.id.workperformedhistory);
        service_date = view.findViewById(R.id.workeddate);
        phone_call=view.findViewById(R.id.phone_call);
        attendermob=view.findViewById(R.id.attendendermobilenumber);


     //   status_color = view.findViewById(R.id.statuses_color);


    }

    private void Service_to_edit(Bundle bundle) {
        if (bundle != null) {

            b_warranty = bundle.getString("warranty");
            b_status = bundle.getString("status");
            b_inv_no = bundle.getString("invoice_no");
            b_ticket = bundle.getString("ticket_no");
            b_date = bundle.getString("date");
            b_pro_name = bundle.getString("pro_name");
            b_pro_des = bundle.getString("pro_des");
            b_inv_amt = bundle.getString("inv_amt");
            b_issue = bundle.getString("issue");
            b_image = bundle.getString("image");
            b_at_name = bundle.getString("at_name");
            b_service_details = bundle.getString("service_details_id");
            b_mobile_no=bundle.getString("attendant_mobile_no");
            b_service_id = bundle.getString("service_id");
            // image_type=bundle.getString("image_type");
            //  image_type=bundle.getString("customer_image_upload");
            //  image_id=bundle.getString("employee_image_upload");
            //   work_performed2 = bundle.getString("service_history");

/*
            attendant = bundle.getString("name");
            work_performed = bundle.getString("work_performed");
            created_date = bundle.getString("created_date");
            mobile_no = bundle.getString("mobile_no");
            workstatus = bundle.getString("work_status");
            emp_image = bundle.getString("emp_image");*/


        }
/*
Log.d("success","imager"+b_image);
        for (int i=0;i<arrayList.size();i++){
            AllServiceModel model=arrayList.get(i);
            //     if (id.equals(b_service_id)){
            if(id != null && id.equals(b_service_id)){
                String id=model.getservice_id();
                Log.d("Success","model_id::"+id);
                String path=model.getProduct_img();
                AllServiceModel model1=new AllServiceModel();
                model1.setProduct_img(path);
                arrayList.add(model1);
            }
        }
        LinearLayoutManager lrt1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        upload_img_show_reg.setLayoutManager(lrt1);
        AllServiceAdapter adapter1 = new AllServiceAdapter(getActivity(),uploadArrayList,workList);
        upload_img_show_reg.setAdapter(adapter1);
*/

       /* for (int i=0;i<arrayList.size();i++){
            AllServiceModel   model =arrayList.get(i);
            String id=model.getservice_id();

            if (id.equals(b_service_id)){
                Log.d("Success","service_type::"+image_type);
                model = new AllServiceModel();
                String path=model.getProduct_img();
                model.setProduct_img(path);
                uploadArrayList.add(model);
            }

        }*/


        StringBuilder builder = new StringBuilder();
        StringTokenizer tk = new StringTokenizer(b_date);
        String date = tk.nextToken();  // <---  yyyy-mm-dd
       // String time = tk.nextToken();

        String[] split = date.split("/");
        String y = split[0];
        String m = split[1];
        String d = split[2];

        builder.append(d).append("/").append(m).append("/").append(y).toString();
        date1 = new String(builder);
        builder.append(date1).toString();

      //  String mobile = model.getAttendantmobile_number();
       /* final StringBuilder build = new StringBuilder();
        StringTokenizer mobile = new StringTokenizer(b_mobile_no);
        build.append(mobile).toString();
*/
       /* String mobi="mobile No:";
        final StringBuilder build=new StringBuilder();
        final String at_mobile_no=build.append(mobi).append(b_mobile_no).toString();
*/
        if (b_warranty.equals("Available") || b_warranty.equals("available")) {
            String tv = "Available";
            warranty_tv.setText(tv);
            warranty_tv.setTextColor(Color.parseColor("#06801a"));
        } else {
            String tv = "Not Available";
            warranty_tv.setText(tv);
            warranty_tv.setTextColor(Color.parseColor("#ff0000"));
        }
        warranty_tv.setText(b_warranty);

        if (b_status.equals("1")) {
            String tv = "Complete";
            current_status.setText(tv);
            current_status.setTextColor(Color.parseColor("#06801a"));
        }else if(b_status.equals("2")){
            String tv = "Pending";
            current_status.setText(tv);
            current_status.setTextColor(Color.parseColor("#fd0606"));
        }else{
            String tv = "Inprogress";
            current_status.setText(tv);
            current_status.setTextColor(Color.parseColor("#ff7e00"));
        }
        edit_invoice.setText(b_inv_no);
        edit_ticket1.setText(b_ticket);
        edit_date.setText(date1);
        task_date.setText(date1);
//        edit_invoice_amt.setText(b_inv_amt);
        emp_edit_about_issue.setText(b_issue);





        String  workperfom =b_mobile_no;
        Log.d("success" , "aryeyghdg"+workperfom);
        if(workperfom!="null"){
            attendermob.setText(b_mobile_no);
            phone_call.setImageResource(R.drawable.ic_mobile);
            phone_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    String temp = "tel:" + b_mobile_no;
                    intent.setData(Uri.parse(temp));

                    startActivity(intent);
                }

            });
        }else{
            attendermob.setText("--");
        }


        Log.d("Success:", "historybefore" + b_ticket);

   //     Log.d("Success:", "history before" + new Gson().toJson(workList));
       /* for (int i = 0; i < workList.size(); i++) {
            WorkPerformedModel model = workList.get(i);

            Log.d("Success", "b_service_id::" + b_service_id);
            Log.d("Success", "id::" + id);
            if (id != null && id.equals("b_service_id")) {
                String id = model.getservice_id();
                WorkPerformedModel model1 = new WorkPerformedModel();
                String path = model.getAttendant_name();
                String Emp_image = model.getEmp_upload_image();
                String status = model.getStatus();
                String workperformed = model.getWork_performed();
                String date4 = model.getService_date();
                String mobile = model.getAttendantmobile_number();
                //     String service_id=model.getservice_id();

                model1.setAttendant_name(path);
                model1.setEmp_upload_image(Emp_image);
                model1.setStatus(status);
                model1.setAttendantmobile_number(mobile);
                model1.setService_date(date4);
                model1.setWork_performed(workperformed);
                //   model1.setservice_id(service_id);
                workList.add(model1);
            } else {
                workList.remove(i);
            }
        }*/
/*
        for (int i = 1; i < workList.size(); i++) {
            WorkPerformedModel model = workList.get(i);

            Log.d("Success", "b_service_id::" + b_service_id);
            Log.d("Success", "id::" + id);
            if (id != null && id.equals("b_service_id")) {
                String id = model.getservice_id();
                WorkPerformedModel model1 = new WorkPerformedModel();
//                String service_id=model.getservice_id();
//                model.setservice_id(service_id);
                workList.add(model1);
            } else
                workList.remove(i);
        }
        Log.d("Success:", "history after" + new Gson().toJson(workList));
        LinearLayoutManager lr = new LinearLayoutManager(getActivity());
        emp_work_id.setLayoutManager(lr);
        WorkperformedAdapter adapter = new WorkperformedAdapter(getActivity(), workList);
        emp_work_id.setAdapter(adapter);
*/

        //   emp_work_id.setText("ff");

      /*  LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(llm);
        list.setAdapter( adapter );*/



        String  attendername =b_at_name;
        Log.d("success" , "aryeyghdg"+workperfom);
        if(attendername!="null"){
            attender_id.setText(b_at_name);
        }else{
            attender_id.setText("--");

        }
        task_date.setText(date);
       // edit_ticket1.setText(b_ticket);


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getActivity());
        sharpre = this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();

    }



    private void workperformedhistory(String id, String cutomer_type,String services_id,String emp_id) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("customer_id",id);
        object.put("service_id",services_id);
        object.put("service_type",cutomer_type);
        object.put("emp_id",emp_id);
      //  object.put()
        //   object.put("inv_no", adapter_id);
        Log.d("Success", "Work_performed_request::" + object);
        Log.d("success","ser89t"+emp_id);
        Log.d("success","custo89"+cutomer_type);
        Log.d("success","typoo89"+id);
        Log.d("success","service89"+services_id);
        runback(Constants.SERVICE_HISTORY, object);
    }

    private void runback(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                hideProgress();
                Log.d("error:::::", "error::::: " + message);
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Log.d("error:","obj:::"+obj);
                        Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success", "Work_performed_responce25::" + response);
                ArrayList<WorkPerformedModel> workList = new ArrayList<>();
                if (response.getString("status").equalsIgnoreCase("success")) {
                    JSONArray array = response.getJSONArray("data");
                    Log.d("Success", "Work_performed_array::" + array);
                    for (int i = 0; i < array.length(); i++) {
                            JSONObject obj1 = array.getJSONObject(i);
                            WorkPerformedModel model = new WorkPerformedModel();
                            model.setAttendant_name(obj1.getString("name"));
                            model.setService_date(obj1.getString("created_date"));
                            model.setAttendantmobile_number(obj1.getString("mobile_no"));
                            model.setStatus(obj1.getString("work_status"));
                            model.setEmp_upload_image(obj1.getString("emp_image"));
                            model.setservice_id(obj1.getString("service_id"));
                            model.setWork_performed(obj1.getString("work_performed"));
                        //    String ser_id = (obj1.getString("service_id"));
                            //    model.setInv_select(adapter_id);


                    /*    JSONArray array1 = obj.getJSONArray("employee_image_upload");
                        ArrayList<String> images = new ArrayList<>();
                        for (int j = 0; j < array1.length(); j++) {
                            JSONObject obj1 = array1.getJSONObject(j);
                            String path = obj1.getString("img_path");
                            images.add(path);
                        }*/
                            //  model.setWarrantyList(images);
                            Log.d("Success:","history123"+ new Gson().toJson(model));
                            workList.add(model);
                        }

                        LinearLayoutManager lrt = new LinearLayoutManager(getActivity());
                        emp_work_id.setLayoutManager(lrt);
                        WorkperformedAdapter adapter = new WorkperformedAdapter(getActivity(), workList);
                        emp_work_id.setAdapter(adapter);

                   /* for (WorkPerformedModel model:workList) {
                        for (String s : model.getWarrantyList()) {
                            Log.w("Images:", s);
                        }*/



                } /*else {
                    if (response.getString("status").equalsIgnoreCase("false")) {
                        String no_data = response.getString("message");
                        Log.d("Success", "no_data::" + no_data);
                      ;  emp_work_id.setVisibility(View.GONE);
                        no_service_data.setVisibility(View.VISIBLE);
                        no_service_data.setText(no_data)
                    }*/

             //   }
                return null;
            }
        });
    }







       /*private void ServiceListMethod(String id,String type) throws JSONException {

    }*/

  /*  private void SendinBackground(String loginUrl, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), loginUrl,object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                //  dialog.cancel();
                hideProgress();
                Log.d("error:::::", "error::::: " + message);
            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                hideProgress();
                Log.d("Success:","Customer_service_list_Responce:::"+response);
                if (response.getString("status").equalsIgnoreCase("Success")) {
                    arrayList = new ArrayList<>();
                    JSONArray array = response.getJSONArray("data");
                   *//* for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        AllServiceModel model = new AllServiceModel();
*//*

                        //  JSONArray array1=obj.getJSONArray("customer_image_upload");
                       *//* ArrayList<String>imageList=new ArrayList<>();
                        for (int j=0;j<array1.length();j++){
                            JSONObject obj1=array1.getJSONObject(j);
                            String path=obj1.getString("img_path");
                            // imageList.add(path);
                            model.setProduct_img(obj1.getString("img_path"));
                        }
                        //  model.setImageList(imageList);
                        arrayList.add(model);*//*
                                }
                return null;
            }
    });
    }
*/

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                return inflater.inflate(R.layout.fragment_customer_edit_service, (ViewGroup) container, false);
            }

            @Override
            public void showProgress() {
                loader_img.setVisibility(View.VISIBLE);
                emp_layout.setVisibility(View.GONE);

            }

            @Override
            public void hideProgress() {
                loader_img.setVisibility(View.GONE);
                emp_layout.setVisibility(View.VISIBLE);
            }


    }


