package com.f2f.incls.fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.f2f.incls.R;
import com.f2f.incls.activity.CustomerDashBoard;
import com.f2f.incls.adapter.PendingListAdapter;
import com.f2f.incls.adapter.ServiceListAdapter;
import com.f2f.incls.model.PendingListModel;
import com.f2f.incls.model.ServiceListModel;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class CustomerHomeFragment extends Fragment {
    Fragment fragment;
    GifImageView loader_img;
    ImageView customer_service,customer_leads;
    FrameLayout cus_home_id;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    TextView no_data,service_no_data;
    ProgressDialog dialog;
    String customer_id;
    RecyclerView pending_list_rec,service_rec;
    ArrayList<PendingListModel> pendinglist;
    ArrayList<ServiceListModel> serviceList;
    TextView text;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviewid(view);
        ((CustomerDashBoard)getActivity()).adsimageget();
        cus_home_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        try {
            HashMap<String,String> map=session.getcustomerdetails();
            customer_id=map.get(session.KEY_USER_ID);
            Log.d("Success","id_test::"+customer_id);
            pendinglist(customer_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pendingService(customer_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session=new SessionManager(getActivity());
        sharpre =this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
        dialog=new ProgressDialog(getActivity());
    }
    private void pendingService(String id) throws JSONException {
        JSONObject object=new JSONObject();
        String type="customer";
        object.put("customer_id",id);
        object.put("service_type",type);
        Log.d("Success:::::", "pending_service_list_req::::: " + object);
        background(Constants.CUS_PENDING_SERVICE,object);

        String login_date ;
        login_date =new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        login_date = sharpre.getString("login_date",login_date);

        String current_date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Log.d("Success", "today_task_requestdate88::" + login_date);
        Log.d("Success", "today_task_request23::" + current_date);
        //   Log.d("Success", "today_task_requestdate56::" + login_date12);
        if (session.ISLogIn()) {
            if (login_date != null) {
                if (!new String(login_date).equals(current_date)) {
                    logout(id, type);
                    session.logoutUser();
                }
            }
        }

    }

    private void logout(String customer_id, String customer_type) {
        JSONObject object=new JSONObject();
        try {
            object.put("user_id",customer_id);
            object.put("user_type","2");
            Log.d("Message","Logout_Request::"+object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyUtils.makeJsonObjectRequest(getActivity(), Constants.LOGIN_OUT, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Message","Logout_Responce::"+response);
                return null;
            }
        });
    }


    private void background(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
            }
            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success","pending_service_responce::"+response);
                if (response.getString("status").equalsIgnoreCase("Success")){
                    JSONArray array=response.getJSONArray("data");
                    serviceList=new ArrayList();
                    for (int  i=0;i<array.length();i++) {
                        JSONObject obj = array.getJSONObject(i);

                        ServiceListModel listModel=new ServiceListModel();
                        listModel.setTn_no(obj.getString("ticket_no"));
                        listModel.setService_issue(obj.getString("description"));
                        listModel.setService_date(obj.getString("created_date"));
                        listModel.setColor(obj.getString("status"));
                        serviceList.add(listModel);
                    }
                    LinearLayoutManager lr=new LinearLayoutManager(getActivity());
                    service_rec.setLayoutManager(lr);
                    ServiceListAdapter adapter=new ServiceListAdapter(getActivity(),serviceList);
                    service_rec.setAdapter(adapter);
                    Collections.reverse(serviceList);
                }else {
                    if (response.getString("status").equalsIgnoreCase("error")) {
                        service_rec.setVisibility(View.GONE);
                        service_no_data.setVisibility(View.VISIBLE);
                        service_no_data.setText(response.getString("message"));
                    }
                }
                return null;
            }
        });
    }
    protected void showProgress() {
        if (getActivity() instanceof LoadinInterface) {
            ((LoadinInterface) getActivity()).showProgress();
        }
    }
    protected void hideProgress() {
        if (getActivity() instanceof LoadinInterface) {
            ((LoadinInterface) getActivity()).hideProgress();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_home, container, false);
    }

    private void pendinglist(String customer_id) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("customer_id",customer_id);
        Log.d("Success:::::", "req_obj::::: " + object);
        sendInBackground(Constants.CUS_PENDING_LEADS, object);

    }
    private void sendInBackground(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url,object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                hideProgress();
                loader_img.setVisibility(View.GONE);
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
            public String onResponse(JSONObject response) {
                hideProgress();
                Log.w("Succes","Pending_Responce::"+response);
                try {
                    if (response.getString("status").equalsIgnoreCase("Success")){
                        JSONArray array=response.getJSONArray("data");
                        pendinglist=new ArrayList();
                        for (int  i=0;i<array.length();i++) {
                            JSONObject obj = array.getJSONObject(i);
                            PendingListModel listModel=new PendingListModel();
                            listModel.setInvoice(obj.getString("enquiry_no"));
                            listModel.setIssue(obj.getString("enquiry_about"));
                            listModel.setDate(obj.getString("created_date"));
                            listModel.setColor(obj.getString("status"));

                            pendinglist.add(listModel);
                        }
                        LinearLayoutManager lr=new LinearLayoutManager(getActivity());
                        pending_list_rec.setLayoutManager(lr);
                        PendingListAdapter adapter=new PendingListAdapter(getActivity(),pendinglist);
                        pending_list_rec.setAdapter(adapter);
                        serviceList=new ArrayList<>();
                    }
                    else {
                        if (response.getString("status").equalsIgnoreCase("false")) {
                            no_data.setVisibility(View.VISIBLE);
                            pending_list_rec.setVisibility(View.GONE);
                            String tex = response.getString("message");
                            no_data.setText(tex);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
    private void findviewid(View view) {
        cus_home_id=view.findViewById(R.id.cus_home_id);
        pending_list_rec=view.findViewById(R.id.pending_list_rec);
        service_rec=view.findViewById(R.id.service_rec);
        customer_leads=view.findViewById(R.id.customer_leads);
        customer_service=view.findViewById(R.id.customer_service);
        no_data=view.findViewById(R.id.no_data);
        service_no_data=view.findViewById(R.id.service_no_data);
    }
    public void hideKeyboard(View view) {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
