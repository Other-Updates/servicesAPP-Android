package com.f2f.incls.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.f2f.incls.R;
import com.f2f.incls.activity.EmployeeDashboard;
import com.f2f.incls.adapter.EmpTodayTaskAdapter;
import com.f2f.incls.model.EmpServiceModel;
import com.f2f.incls.model.WarrantyListModel;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.CustomerUploadModel;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class TodayTaskFragment extends Fragment implements LoadinInterface {
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    RecyclerView emp_today_task_rec;
    GifImageView loader_img;
    ImageView date_pick,fromdate,todate,imgsearch,reset;
    LinearLayout fromlinear,tolinear,searchlinear;
    LinearLayout horizontal_ly,emp_tdy_task_ly;
    ArrayList<EmpServiceModel>todaytaskList;
    String[] status={"Inprogress","Complete","Reject","Complete","Reject"};
    int day,month,year;
    TextView from_date,to_date,search_date,emp_tdy_task_tv;
    String req_from_date,req_to_date,emp_id;
    String service_type="employee";
    SimpleDateFormat dateFormat;

    ArrayList<WarrantyListModel> arrayList=new ArrayList<>();
    ArrayList<CustomerUploadModel>imageList;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviewid(view);
        ((EmployeeDashboard)getActivity()).adsimageget();
        HashMap<String,String>emp=session.getempdetails();
        String type="employee";
          emp_id=emp.get(KEY_USER_ID);

        Log.d("Success","today_task_emp_id::"+emp_id);

     /*   try {
            todaytask(emp_id,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        String date_n=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        from_date.setText(date_n);
        to_date.setText(date_n);
        Log.d("success","!22"+ date_n);
        String from=from_date.getText().toString();
        String to=to_date.getText().toString();
        try {
            todaytaskdateselect(emp_id,service_type,from,to);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =
                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()  {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            from_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            String from=from_date.getText().toString();
                            String to=to_date.getText().toString();
                            if (!from.isEmpty() && !to.isEmpty()) {
                                try {
                                    todaytaskdateselect(emp_id,service_type, from, to);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },mYear,mMonth,mDay);
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dialog.show();
                fromlinear.setVisibility(View.VISIBLE);
                tolinear.setVisibility(View.VISIBLE);
                searchlinear.setVisibility(View.GONE);
            }
        });
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                day=c.get(Calendar.DAY_OF_MONTH);
                month=c.get(Calendar.MONTH);
                year=c.get(Calendar.YEAR);
                final DatePickerDialog dialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        to_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        String from=from_date.getText().toString();
                        String to=to_date.getText().toString();
                        try {
                            todaytaskdateselect(emp_id,service_type,from,to);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
                fromlinear.setVisibility(View.VISIBLE);
                tolinear.setVisibility(View.VISIBLE);
                searchlinear.setVisibility(View.GONE);
            }
        });
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =
                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()  {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                from_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                String from=from_date.getText().toString();
                                String to=to_date.getText().toString();
                                if (!from.isEmpty() && !to.isEmpty()) {
                                    try {
                                        todaytaskdateselect(emp_id,service_type, from, to);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },mYear,mMonth,mDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
                fromlinear.setVisibility(View.VISIBLE);
                tolinear.setVisibility(View.VISIBLE);
                searchlinear.setVisibility(View.GONE);
            }
        });

        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c=Calendar.getInstance();
                day=c.get(Calendar.DAY_OF_MONTH);
                month=c.get(Calendar.MONTH);
                year=c.get(Calendar.YEAR);
                final DatePickerDialog dialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        to_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        String from=from_date.getText().toString();
                            String to=to_date.getText().toString();
                            try {
                                todaytaskdateselect(emp_id,service_type,from,to);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
                fromlinear.setVisibility(View.VISIBLE);
                tolinear.setVisibility(View.VISIBLE);
                searchlinear.setVisibility(View.GONE);

            }
        });

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c=Calendar.getInstance();
                day=c.get(Calendar.DAY_OF_MONTH);
                month=c.get(Calendar.MONTH);
                year=c.get(Calendar.YEAR);
                final DatePickerDialog dialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        search_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        String from=search_date.getText().toString();
                        String to=search_date.getText().toString();
                        try {
                            todaytaskdateselect(emp_id,service_type,from,to);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
                fromlinear.setVisibility(View.GONE);
                tolinear.setVisibility(View.GONE);
                searchlinear.setVisibility(View.VISIBLE);

            }

        });
        search_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c=Calendar.getInstance();
                day=c.get(Calendar.DAY_OF_MONTH);
                month=c.get(Calendar.MONTH);
                year=c.get(Calendar.YEAR);
                final DatePickerDialog dialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        search_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        String from=search_date.getText().toString();
                        String to=search_date.getText().toString();
                        try {
                            todaytaskdateselect(emp_id,service_type,from,to);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
                fromlinear.setVisibility(View.GONE);
                tolinear.setVisibility(View.GONE);
                searchlinear.setVisibility(View.VISIBLE);

            }

        });



        reset.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String date_n=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                 search_date.setText(null);
                 from_date.setText(date_n);
                 to_date.setText(date_n);
                 Log.d("success","!22"+ date_n);
                 String from=from_date.getText().toString();
                 String to=to_date.getText().toString();
                 try {
                     todaytaskdateselect(emp_id,service_type,from,to);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 searchlinear.setVisibility(View.VISIBLE);
                 tolinear.setVisibility(View.VISIBLE);
                 fromlinear.setVisibility(View.VISIBLE);

             }

         });





       /* date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =
                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                if ((from_date.getText().toString().isEmpty() && to_date.getText().toString().isEmpty()) ||
                                        (!from_date.getText().toString().isEmpty() && to_date.getText().toString().isEmpty())||
                                        (!from_date.getText().toString().isEmpty()
                                                && !to_date.getText().toString().isEmpty())) {
                                    if (from_date.getText().toString().isEmpty() && to_date.getText().toString().isEmpty()) {
                                        from_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                        req_from_date = (dayOfMonth + "/" + (month + 1) + "/" + year);
                                        Log.d("Success", "date_test::" + req_from_date);
                                    } else if (!from_date.getText().toString().isEmpty()
                                            && to_date.getText().toString().isEmpty()) {
                                        to_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                        req_to_date = (dayOfMonth + "/" + (month + 1) + "/" + year);
                                        Log.d("Success", "date_pick_to_date::");
                                        try {
                                            todaytaskdateselect(emp_id,service_type,req_from_date,req_to_date);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }else if (!from_date.getText().toString().isEmpty()
                                            && !to_date.getText().toString().isEmpty()){
                                        from_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                        req_from_date = (dayOfMonth + "/" + (month + 1) + "/" + year);
                                        try {
                                            todaytaskdateselect(emp_id,service_type,req_from_date,req_to_date);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }else {

                                }
                            }
                        }, mYear, mMonth, mDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
*/    }
    private void todaytaskdateselect(String emp_id, String service_type, String req_from_date, String req_to_date) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("emp_id",emp_id);
        object.put("service_type",service_type);
        object.put("from_date",req_from_date);
        object.put("to_date",req_to_date);
        Log.d("Success","From_date::"+req_from_date);
        Log.d("Success","To_date::"+req_to_date);
        doinback(Constants.CUS_SERVICE_LIST,object);
        Log.d("Success","date_vice_selec_request::"+object);
    }


   /* private void todaytasksearchdate(String emp_id, String service_type, String req_search_date) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("emp_id",emp_id);
        object.put("service_type",service_type);
        object.put("search_flag","1");
        object.put("search_date",req_search_date);
      //  object.put("to_date",req_to_date);
        Log.d("Success","search_date::"+req_search_date);
    //    Log.d("Success","To_date::"+req_to_date);
        doinback(Constants.CUS_SERVICE_LIST,object);
        Log.d("Success","date_vice_selec_request::"+object);
    }*/


    private void doinback(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                hideProgress();

                todaytaskList =new ArrayList<>();
                arrayList=new ArrayList<>();
                imageList=new ArrayList<>();
                Log.d("Success","date_vice_selected::"+response);
                if (response.getString("status").equalsIgnoreCase("false")){
                   String error_msg=response.getString("message");
                   emp_tdy_task_tv.setText(error_msg);
                   emp_tdy_task_tv.setVisibility(View.VISIBLE);
                   emp_tdy_task_ly.setVisibility(View.GONE);
                }else {
                    emp_tdy_task_tv.setVisibility(View.GONE);
                    emp_tdy_task_ly.setVisibility(View.VISIBLE);
                }

                if (response.getString("status").equalsIgnoreCase("success")){

                    Log.d("Success","Today_task_responce::"+response);
                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        EmpServiceModel model=new EmpServiceModel();
                        model.setInvoice(obj.getString("inv_no"));
                        model.setDate(obj.getString("created_date"));
                        model.setIssue(obj.getString("description"));
                        model.setTicket(obj.getString("ticket_no"));
                        model.setWarranty(obj.getString("warrenty"));

                        model.setService_id(obj.getString("id"));
                        model.setStatus(obj.getString("status"));
                        model.setAttendant_name(obj.getString("attendant"));
                     //   model.setPicture(obj.getString("product_image"));
                        model.setInvoice_amount(obj.getString("net_total"));

                        JSONArray array1=obj.getJSONArray("invoice_details");
                        for (int j=0;j<array1.length();j++){
                            JSONObject obj1=array1.getJSONObject(j);
                            WarrantyListModel model1=new WarrantyListModel();
                            model1.setProduct_name(obj1.getString("product_name"));
                            model1.setProduct_des(obj1.getString("product_description"));
                            model1.setInv_not_select("");
                            arrayList.add(model1);
                        }
                        JSONArray array2=obj.getJSONArray("customer_image_upload");
                        for (int k=0;k<array2.length();k++){
                            JSONObject obj2=array2.getJSONObject(k);
                            CustomerUploadModel model1=new CustomerUploadModel();
                            model1.setImage_path(obj2.getString("img_path"));
                            model1.setService_id(obj2.getString("service_id"));
                            String path=obj2.getString("img_path");
                            model.setPicture(path);
                            imageList.add(model1);
                        }
                        todaytaskList.add(model);
                    }
                    LinearLayoutManager lrt1=new LinearLayoutManager(getActivity());
                    emp_today_task_rec.setLayoutManager(lrt1);
                    EmpTodayTaskAdapter adapter1=new EmpTodayTaskAdapter(getActivity(),todaytaskList,arrayList,imageList);
                    emp_today_task_rec.setAdapter(adapter1);
                }
                return null;
            }
        });
    }

    private void todaytask(String id, String type) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("emp_id",id);
        object.put("service_type",type);
        Log.d("Success","today_task_request::"+object);
        dobackground(Constants.EMP_GET_PENDING_SERVICE_LIST,object);
    }

    private void dobackground(String url, JSONObject object) {

        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {

            @Override
            public void onError(String message, VolleyError error) {
                Log.d("Error","Responce_test_today_task::");
            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                hideProgress();
                Log.d("Success","Responce_test_today_task::"+response);
                imageList=new ArrayList<>();
                arrayList=new ArrayList<>();
                todaytaskList =new ArrayList<>();
                if (response.getString("status").equalsIgnoreCase("success")){
                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        EmpServiceModel model=new EmpServiceModel();
                        model.setInvoice(obj.getString("inv_no"));
                        model.setDate(obj.getString("created_date"));
                        model.setIssue(obj.getString("description"));
                        model.setTicket(obj.getString("ticket_no"));
                        model.setWarranty(obj.getString("warrenty"));

                        model.setService_id(obj.getString("id"));
                        model.setStatus(obj.getString("status"));
                        model.setAttendant_name(obj.getString("attendant"));
                        model.setInvoice_amount(obj.getString("net_total"));
                      //  model.setService_details_id(obj.getString("service_details_id"));

                        JSONArray array1=obj.getJSONArray("invoice_details");
                        for (int j=0;j<array1.length();j++){
                            JSONObject obj1=array1.getJSONObject(j);
                            WarrantyListModel model1=new WarrantyListModel();
                            model1.setProduct_name(obj1.getString("product_name"));
                            model1.setProduct_des(obj1.getString("product_description"));
                            model1.setInv_not_select("");
                            arrayList.add(model1);
                        }
                        JSONArray array2=obj.getJSONArray("customer_image_upload");
                        for (int k=0;k<array2.length();k++){
                            JSONObject obj2=array2.getJSONObject(k);
                            CustomerUploadModel model1=new CustomerUploadModel();
                            model1.setImage_path(obj2.getString("img_path"));
                            model1.setService_id(obj2.getString("service_id"));
                            String path=obj2.getString("img_path");
                            model.setPicture(path);
                            imageList.add(model1);
                        }
                        todaytaskList.add(model);
                    }
                    LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                    emp_today_task_rec.setLayoutManager(lrt);
                    Log.d("Success","today_tasklist_size::"+todaytaskList.size());
                    Log.d("Success","today_arraylist_size::"+arrayList.size());
                    Log.d("Success","today_image_size::"+imageList.size());
                    EmpTodayTaskAdapter adapter=new EmpTodayTaskAdapter(getActivity(),todaytaskList,arrayList, imageList);
                    emp_today_task_rec.setAdapter(adapter);
                }else {
                    if (response.getString("status").equalsIgnoreCase("false")){
                        String error_msg=response.getString("message");
                        emp_tdy_task_tv.setText(error_msg);
                        emp_tdy_task_tv.setVisibility(View.VISIBLE);
                        emp_tdy_task_ly.setVisibility(View.GONE);
                    }else {
                        emp_tdy_task_tv.setVisibility(View.GONE);
                        emp_tdy_task_ly.setVisibility(View.VISIBLE);
                    }
                }
                return null;
            }
        });
    }

    private void findviewid(View view) {
        emp_today_task_rec=view.findViewById(R.id.emp_today_task_rec);
        loader_img=view.findViewById(R.id.loader_img);
        horizontal_ly=view.findViewById(R.id.horizontal_ly);
        fromdate=view.findViewById(R.id.fromdate);
        todate=view.findViewById(R.id.todate);
        imgsearch=view.findViewById(R.id.imgsearch);
        search_date=view.findViewById(R.id.txt_search);
        fromlinear=view.findViewById(R.id.linearfrom);
        tolinear=view.findViewById(R.id.tolinear);
        searchlinear=view.findViewById(R.id.searchlinear);
  //      date_pick=view.findViewById(R.id.date_pick);
        reset=view.findViewById(R.id.reset);
        from_date=view.findViewById(R.id.from_date);
        to_date=view.findViewById(R.id.to_date);
        emp_tdy_task_tv=view.findViewById(R.id.emp_tdy_task_tv);
        emp_tdy_task_ly=view.findViewById(R.id.emp_tdy_task_ly);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session=new SessionManager(getActivity());
        sharpre=this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor=sharpre.edit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today_task, container, false);
    }



    @Override
    public void showProgress() {
        emp_today_task_rec.setVisibility(View.GONE);
        loader_img.setVisibility(View.VISIBLE);
        horizontal_ly.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        emp_today_task_rec.setVisibility(View.VISIBLE);
        loader_img.setVisibility(View.GONE);
        horizontal_ly.setVisibility(View.VISIBLE);
    }
}
