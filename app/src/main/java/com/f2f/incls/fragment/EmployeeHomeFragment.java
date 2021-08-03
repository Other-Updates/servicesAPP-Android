package com.f2f.incls.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.f2f.incls.R;
import com.f2f.incls.activity.EmployeeDashboard;
import com.f2f.incls.activity.LoginActivity;
import com.f2f.incls.adapter.EmpPendinglistAdapter;
import com.f2f.incls.adapter.TodayTaskAdapter;
import com.f2f.incls.model.EmployeePendingServiceModel;
import com.f2f.incls.model.PendinTodayTaskModel;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

//import static com.f2f.incls.utilitty.SessionManager.KEY_LOGINDATE;
//import static com.f2f.incls.utilitty.SessionManager.KEY_LOGINSTATUS;
import static com.f2f.incls.utilitty.SessionManager.IS_LOGIN;

import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class EmployeeHomeFragment extends Fragment implements LoadinInterface {
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    RecyclerView emp_pending_service,emp_pending_task;
    ArrayList<EmployeePendingServiceModel>PendingSeviceList=new ArrayList<>();
    ArrayList<PendinTodayTaskModel>TodataskList=new ArrayList<>();
    String emp_id;
    String type="employee";
    TextView no_data,service_no_data,today_task_date;
    ProgressDialog dialog;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findview(view);

        ((EmployeeDashboard)getActivity()).adsimageget();
        HashMap<String,String>emp=session.getempdetails();
        emp_id=emp.get(KEY_USER_ID);
        try {
            pendinservice(emp_id,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            todaytask(emp_id,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void todaytask(String emp_id, String type) throws JSONException {
        showProgress();
        JSONObject object = new JSONObject();
        object.put("emp_id", emp_id);
        object.put("service_type", type);
        Log.d("Success", "today_task_request::" + object);
        doback(Constants.EMP_GET_PENDING_SERVICE_LIST, object);
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
                    logout(emp_id, type);
                    session.logoutUser();
                }
            }
        }



    }





    private void logout( String emp_id, String type) {
        JSONObject object=new JSONObject();
        try {
            object.put("user_id", this.emp_id);
            object.put("user_type", "1");
            Log.d("Message", "Logout_Request::" + object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyUtils.makeJsonObjectRequest(getActivity(), Constants.EMP_LOGIN_OUT, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Message", "Emp_Logout_Responce::" + response);
                //       editor.putString(KEY_LOGINDATE,"0");
                return null;
            }
        });


    }
    private void pendinservice(String emp_id, String type) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("emp_id", emp_id);
        object.put("service_type", type);
        Log.d("Success","pending_service_request24::"+object);
        dobackground(Constants.EMP_GET_PENDING_SERVICE_LIST,object);
    }

    /*  private void doback(String url, JSONObject object) {
          VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
              @Override
              public void onError(String message, VolleyError error) {

              }

              @Override
              public String onResponse(JSONObject response) throws JSONException {
                  hideProgress();
                  Log.d("Success","today_task_responce::"+response);
                  if (response.getString("status").equalsIgnoreCase("success")){
                      service_no_data.setVisibility(View.GONE);
                      emp_pending_task.setVisibility(View.VISIBLE);
                      JSONArray array=response.getJSONArray("data");
                      for (int i=0;i<array.length();i++){
                          JSONObject obj=array.getJSONObject(i);
                          PendinTodayTaskModel model=new PendinTodayTaskModel();
                          String create = obj.getString("created_date");
                          Log.d("success","createdate"+create);
                          String current_date=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                          Log.d("success","current_date"+current_date);
                          if(create.equals(current_date)){
                              model.setDate(obj.getString("created_date"));
                              model.setIssue(obj.getString("description"));
                              model.setStatus(obj.getString("status"));
                              TodataskList.add(model);
                          }

                      }
                      LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                      emp_pending_task.setLayoutManager(lrt);
                      TodayTaskAdapter adapter=new TodayTaskAdapter(getActivity(),TodataskList);
                      Log.d("Success","today_task_responce_array_size::"+TodataskList.size());
                      emp_pending_task.setAdapter(adapter);
                      Collections.reverse(TodataskList);
                  }else {
                      service_no_data.setText(response.getString("message"));
                      service_no_data.setVisibility(View.VISIBLE);
                      emp_pending_task.setVisibility(View.GONE);
                  }
             //     for (int i=0;i<dat.length;i++){
                      PendinTodayTaskModel model=new PendinTodayTaskModel();
                      model.setDate(dat[i]);
                      model.setIssue(today_issu[i]);
                      TodataskList.add(model);
                      Log.d("Success","arrayList::"+TodataskList.size());
                  }
                  LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                  emp_pending_task.setLayoutManager(lrt);
                  TodayTaskAdapter adapter=new TodayTaskAdapter(getActivity(),TodataskList);
                  emp_pending_task.setAdapter(adapter);*//*
                return null;
            }
        });
    }
*/
    private void doback(String ur, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), ur, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                hideProgress();
                Log.d("Success","today_task_responce::"+response);

                if (response.getString("status").equalsIgnoreCase("success")){
                    service_no_data.setVisibility(View.GONE);
                    emp_pending_task.setVisibility(View.VISIBLE);
                    JSONArray array=response.getJSONArray("data");

                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        PendinTodayTaskModel model=new PendinTodayTaskModel();
                        String create = obj.getString("created_date");
                        Log.d("success","createdate"+create);
                        String current_date=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        Log.d("success","current_date"+current_date);
                        if(create.equals(current_date)){
                            model.setDate(obj.getString("created_date"));
                            model.setIssue(obj.getString("description"));
                            model.setStatus(obj.getString("status"));
                            TodataskList.add(model);
                        }
                    }
                    LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                    emp_pending_task.setLayoutManager(lrt);
                    TodayTaskAdapter adapter=new TodayTaskAdapter(getActivity(),TodataskList);
                    Log.d("Success","today_task_responce_array_size::"+TodataskList.size());
                    emp_pending_task.setAdapter(adapter);
                    Collections.reverse(TodataskList);
                }else {
                    service_no_data.setText(response.getString("message"));
                    service_no_data.setVisibility(View.VISIBLE);
                    emp_pending_task.setVisibility(View.GONE);
                }

              /*  for (int i=0;i<issue.length;i++){
                    EmployeePendingServiceModel model=new EmployeePendingServiceModel();
                    model.setTicket_no(ticket[i]);
                    model.setIssue(issue[i]);
                    model.setDate(date[i]);
                    PendingSeviceList.add(model);

                }
                LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                emp_pending_service.setLayoutManager(lrt);
                EmpPendinglistAdapter adapter=new EmpPendinglistAdapter(getActivity(),PendingSeviceList);
                emp_pending_service.setAdapter(adapter);*/
                return null;
            }
        });
    }



    private void dobackground(String ur, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), ur, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                hideProgress();
                Log.d("Success","emp_pending_service_responce::"+response);

                if (response.getString("status").equalsIgnoreCase("success")){
                    no_data.setVisibility(View.GONE);
                    emp_pending_service.setVisibility(View.VISIBLE);
                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        EmployeePendingServiceModel model=new EmployeePendingServiceModel();
                        model.setTicket_no(obj.getString("ticket_no"));
                        model.setDate(obj.getString("created_date"));
                        model.setIssue(obj.getString("description"));
                        model.setStatus(obj.getString("status"));
                        PendingSeviceList.add(model);
                    }
                    LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                    emp_pending_service.setLayoutManager(lrt);
                    EmpPendinglistAdapter adapter=new EmpPendinglistAdapter(getActivity(),PendingSeviceList);
                    emp_pending_service.setAdapter(adapter);
                    Collections.reverse(PendingSeviceList);
                }else {
                    no_data.setText(response.getString("message"));
                    no_data.setVisibility(View.VISIBLE);
                    emp_pending_service.setVisibility(View.GONE);
                }

              /*  for (int i=0;i<issue.length;i++){
                    EmployeePendingServiceModel model=new EmployeePendingServiceModel();
                    model.setTicket_no(ticket[i]);
                    model.setIssue(issue[i]);
                    model.setDate(date[i]);
                    PendingSeviceList.add(model);

                }
                LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                emp_pending_service.setLayoutManager(lrt);
                EmpPendinglistAdapter adapter=new EmpPendinglistAdapter(getActivity(),PendingSeviceList);
                emp_pending_service.setAdapter(adapter);*/
                return null;
            }
        });
    }

    private void findview(View view) {
        emp_pending_service=view.findViewById(R.id.emp_pending_service);
        emp_pending_task=view.findViewById(R.id.emp_pending_task);
        service_no_data=view.findViewById(R.id.service_noo_data);
        no_data=view.findViewById(R.id.noo_data);
        today_task_date=view.findViewById(R.id.today_task_date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        dialog=new ProgressDialog(getActivity());
        super.onCreate(savedInstanceState);
        session=new SessionManager(getActivity());
        sharpre=this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor=sharpre.edit();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_home, container, false);
    }


    public void showProgress() {
        if (getActivity() instanceof LoadinInterface) {
            ((LoadinInterface) getActivity()).showProgress();
        }
    }

    public void hideProgress() {
        if (getActivity() instanceof LoadinInterface) {
            ((LoadinInterface) getActivity()).hideProgress();
            Log.d("Success","Loading_test::");
        }
    }
}
