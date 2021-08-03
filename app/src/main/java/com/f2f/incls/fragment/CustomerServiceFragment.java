package com.f2f.incls.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.f2f.incls.R;
import com.f2f.incls.activity.CustomerDashBoard;
import com.f2f.incls.adapter.AllServiceAdapter;
import com.f2f.incls.model.AllServiceModel;
import com.f2f.incls.model.EmpServiceModel;
import com.f2f.incls.model.WarrantyListModel;
import com.f2f.incls.model.WorkPerformedModel;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.CustomerUploadModel;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class CustomerServiceFragment extends Fragment implements LoadinInterface {
    TextView no_service_data,current_status;
    FloatingActionButton add_service_float;
  //  android.app.Fragment fragment;
    ImageView lead_power,add_image_leads,ad_img_close,raise_ticket,add_leads;
    RelativeLayout ads_img_ly;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    GifImageView loader_img;
    Fragment fragment;
   // String id;
    RecyclerView full_lead_rec;
    ArrayList<AllServiceModel>arrayList;
   private ArrayList<WorkPerformedModel>workList=new ArrayList<>();

 //
    ArrayList<EmpServiceModel> serviceList = new ArrayList<EmpServiceModel>();
    ArrayList<CustomerUploadModel> ImagearrayList = new ArrayList<>();
    private Object container;

    //  private ArrayList<WarrantyListModel> arrayList = new ArrayList<>();


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviewid(view);
        ((CustomerDashBoard)getActivity()).addservice();
        ((CustomerDashBoard)getActivity()).adsimageget();

        session=new SessionManager(getActivity());
        sharpre =this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
        dialog=new ProgressDialog(getActivity());
        HashMap<String, String> user = session.getcustomerdetails();
        final String customer_id = user.get(SessionManager.KEY_USER_ID);
        String cutomer_type="customer";
        Log.d("Success","cus_ser_id::"+customer_id);


        try {
            ServiceListMethod(customer_id,cutomer_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        add_service_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CustomerDashBoard)getActivity()).addservicefloat();
                Log.d("Success","Paid_Service::");
                fragment=new CustomerPaidFragment();
                loadfragment(fragment);
            }
        });



           }
    private void loadfragment(Fragment fragment) {
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void findviewid(View view) {
        lead_power=view.findViewById(R.id.lead_power);
        full_lead_rec=view.findViewById(R.id.full_lead_rec);
        no_service_data=view.findViewById(R.id.no_service_data);
        loader_img=view.findViewById(R.id.loader_img);
        current_status=view.findViewById(R.id.currentstatus_id);
        raise_ticket=view.findViewById(R.id.raise_ticket);
        add_service_float=view.findViewById(R.id.add_service_float);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void ServiceListMethod(String id, String cutomer_type) throws JSONException {
       showProgress();
        JSONObject object=new JSONObject();
        object.put("customer_id",id);
        object.put("service_type",cutomer_type);
        Log.d("Success::", "Service_request_object::" + object);
        SendinBackground(Constants.CUS_SERVICE_LIST,object);
    }

    private void SendinBackground(String loginUrl, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), loginUrl,object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
              //  dialog.cancel();
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
             //   dialog.cancel();
                hideProgress();
                Log.d("Success:","Customer_service_list_Responce:::"+response);
                if (response.getString("status").equalsIgnoreCase("Success")){
                    arrayList=new ArrayList<>();
                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        AllServiceModel model=new AllServiceModel();
                        model.setInvoince(obj.getString("inv_no"));
                        model.setDate(obj.getString("created_date"));
                        model.setIssue(obj.getString("description"));
                        model.setAttendant_name(obj.getString("attendant"));
                        model.setTicket_number(obj.getString("ticket_no"));
                        model.setWarranty(obj.getString("warrenty"));
                        model.setAttendant_mobile(obj.getString("attendant_mobile_no"));
                    //    model.setinvoince_amount(obj.getString("inv_amt"));
                        model.setservice_id(obj.getString("id"));
                        model.setStatus(obj.getString("status"));
                  //      model.sethistory_id(obj.getString("service_id"));


                        Log.d("Success:","model data"+ new Gson().toJson(model));
                        JSONArray array2=obj.getJSONArray("service_history");
                        Log.d("Success:","service_history55:::"+array2);
                        for (int k=0;k<array2.length();k++) {
                            JSONObject obj1 = array2.getJSONObject(k);
//                            String history_service_id = obj1.getString("service_id");

                                WorkPerformedModel workLists = new WorkPerformedModel();
                                workLists.setservice_id(obj1.getString("service_id"));


                            }


                                //    workLists.setWork_not_select("");
                        JSONArray array1=obj.getJSONArray("customer_image_upload");
                        ArrayList<String>imageList=new ArrayList<>();
                        for (int j=0;j<array1.length();j++){
                            JSONObject obj2=array1.getJSONObject(j);
                            String path=obj2.getString("img_path");
                           // imageList.add(path);
                            model.setProduct_img(obj2.getString("img_path"));
                            model.setservice_id(obj2.getString("service_id"));
                        }
                      //  model.setImageList(imageList);
                        arrayList.add(model);
                    }
                    LinearLayoutManager lr=new LinearLayoutManager(getActivity());
                    full_lead_rec.setLayoutManager(lr);
                    AllServiceAdapter adapter=new AllServiceAdapter(getActivity(),arrayList,workList);
                    full_lead_rec.setAdapter(adapter);
                    Collections.reverse(arrayList);

                }else {
                    if (response.getString("status").equalsIgnoreCase("false")){
                        String no_data= response.getString("message");
                        Log.d("Success","no_data::"+no_data);
                        full_lead_rec.setVisibility(View.GONE);
                        no_service_data.setVisibility(View.VISIBLE);
                        no_service_data.setText(no_data);
                    }


                }
                return null;
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_customer_service, (ViewGroup) container, false);
        }




    @SuppressLint("RestrictedApi")
    @Override
    public void showProgress() {
        loader_img.setVisibility(View.VISIBLE);
        full_lead_rec.setVisibility(View.GONE);
        add_service_float.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void hideProgress() {
        loader_img.setVisibility(View.GONE);
        full_lead_rec.setVisibility(View.VISIBLE);
        add_service_float.setVisibility(View.VISIBLE);
    }
}
