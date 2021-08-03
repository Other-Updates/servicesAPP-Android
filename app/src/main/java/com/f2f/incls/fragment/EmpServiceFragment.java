package com.f2f.incls.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.f2f.incls.R;
import com.f2f.incls.activity.EmployeeDashboard;
import com.f2f.incls.adapter.EmpServiceAdapter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class EmpServiceFragment<showProgress> extends Fragment /*impemelnts LoadinInterface*/ {
    SessionManager session;
    SharedPreferences sharpre;
    TextView empty_leads;
    SharedPreferences.Editor editor;
    RecyclerView emp_service_rec;
    LinearLayout leads_list_loader, leads_list_ly;
    ArrayList<EmpServiceModel> serviceList = new ArrayList<EmpServiceModel>();
    ArrayList<CustomerUploadModel> ImagearrayList = new ArrayList<>();
    private ArrayList<WarrantyListModel> arrayList = new ArrayList<>();
    android.app.Fragment fragment;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       findviewid(view);
        ((EmployeeDashboard)getActivity()).empservicetool();
        ((EmployeeDashboard)getActivity()).adsimageget();
        session = new SessionManager(getActivity());
        sharpre = this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();

        HashMap<String,String>user=session.getempdetails();
        String emp_id=user.get(KEY_USER_ID);
        String type="employee";
        try {
            empservice(emp_id,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

   private void empservice(String emp_id, String type) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("service_type",type);
        object.put("emp_id",emp_id);
        doback(Constants.CUS_SERVICE_LIST,object);
        Log.d("Success","Employee_service_list_request::"+ String.valueOf(object));
    }


    private void doback(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                Log.d("Error","Employee_service_list_response::");

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success","Employee_service_list_responce88::"+response);
                hideProgress();

                if (response.getString("status").equalsIgnoreCase("success")){
                    empty_leads.setVisibility(View.GONE);
                    emp_service_rec.setVisibility(View.VISIBLE);
                    ImagearrayList.clear();
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
                    //    model.setService_details_id(obj.getString("service_details_id"));
                        model.setInvoice_amount(obj.getString("net_total"));

                        JSONArray array1=obj.getJSONArray("invoice_details");
                        Log.d("success","invoice_details"+array1);
                        for (int j=0;j<array1.length();j++){
                            JSONObject obj1=array1.getJSONObject(j);
                            WarrantyListModel model1=new WarrantyListModel();
                            model1.setProduct_name(obj1.getString("product_name"));
                            model1.setProduct_des(obj1.getString("product_description"));
                            model1.setInv_not_select("");
                            arrayList.add(model1);
                        }
                        JSONArray array2=obj.getJSONArray("customer_image_upload");
                        Log.d("Success","Imagelist_size_product::"+array2.length());
                        for (int k=0;k<array2.length();k++){
                            JSONObject obj2=array2.getJSONObject(k);
                            CustomerUploadModel emodel=new CustomerUploadModel();
                            emodel.setImage_path(obj2.getString("img_path"));
                            emodel.setService_id(obj2.getString("service_id"));
                            ImagearrayList.add(emodel);
                            String path=obj2.getString("img_path");
                            String id=obj2.getString("service_id");
                            model.setPicture(path);
                        }
                        serviceList.add(model);
                    }
                }else {
                    empty_leads.setText(response.getString("message"));
                    empty_leads.setVisibility(View.VISIBLE);
                    emp_service_rec.setVisibility(View.GONE);
                }
                LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                emp_service_rec.setLayoutManager(lrt);
                EmpServiceAdapter adapter=new EmpServiceAdapter(getActivity(),serviceList,arrayList,ImagearrayList);
                emp_service_rec.setAdapter(adapter);
                Collections.reverse(serviceList);
                return null;
            }
        });

    }

    private void findviewid(View view) {
        emp_service_rec=view.findViewById(R.id.emp_service_rec);
        leads_list_loader=view.findViewById(R.id.leads_list_loader);
        leads_list_ly=view.findViewById(R.id.leads_list_ly);
        empty_leads=view.findViewById(R.id.empty_leads);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity());
        sharpre = this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emp_service, container, false);
    }


    public void showProgress() {
        leads_list_loader.setVisibility(View.VISIBLE);
        leads_list_ly.setVisibility(View.GONE);
        emp_service_rec.setVisibility(View.GONE);


    }


    public void hideProgress() {
        leads_list_loader.setVisibility(View.GONE);
        leads_list_ly.setVisibility(View.VISIBLE);
        emp_service_rec.setVisibility(View.VISIBLE);

    }
    }

