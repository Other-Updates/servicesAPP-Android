package com.f2f.incls.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.f2f.incls.R;
import com.f2f.incls.activity.CustomerDashBoard;
import com.f2f.incls.adapter.FullLeadsAdapter;
import com.f2f.incls.model.AllLeadsListModel;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.OnBackPressed;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class CustomerLeadsFragment extends Fragment implements OnBackPressed , LoadinInterface {

    TextView empty_leads,add_leads_tv;
    FloatingActionButton add_lead_float;
    Fragment fragment;
    ImageView lead_power,add_image_leads,customer_service,customer_leads,add_leads,ad_img_close;
    RelativeLayout ads_img_ly;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    LinearLayout leads_list_ly,leads_list_loader;
    GifImageView loader_img;
    // String id;
    RecyclerView full_lead_rec;

    ArrayList<AllLeadsListModel>LeadsList;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findview(view);
       ((CustomerDashBoard)getActivity()).adsimageget();
        leadnumber();

        session=new SessionManager(getActivity());
        sharpre =this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();

        dialog=new ProgressDialog(getContext());
        HashMap<String, String> user = session.getcustomerdetails();
        String customer_id = user.get(SessionManager.KEY_USER_ID);

        Log.d("Success","Customer_id::"+customer_id);

        try {
            AllLeadsList(customer_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        add_lead_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((CustomerDashBoard)getActivity()).addleadsfloat();
            }
        });
    }

    private void findview(View view) {
        lead_power=view.findViewById(R.id.lead_power);
        customer_service=view.findViewById(R.id.customer_service);
        customer_leads=view.findViewById(R.id.customer_leads);
        loader_img=view.findViewById(R.id.loader_img);
        leads_list_ly=view.findViewById(R.id.leads_list_ly1);
        full_lead_rec=view.findViewById(R.id.full_lead_rec);
        add_leads=view.findViewById(R.id.add_leads);
        empty_leads=view.findViewById(R.id.empty_leads);
        add_lead_float=view.findViewById(R.id.add_lead_float);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void leadnumber() {
        VolleyUtils.makeJsonObjectRequest(getActivity(), Constants.CUS_LEADS_NUMBER, null, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success","Leads_Number_Reques::"+response);
                if (response.getString("status").equalsIgnoreCase("success")){
                    String leads_no=response.getString("Leads Number");
                    fragment=new AddLeadsFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("leads_number",leads_no);
                    fragment.setArguments(bundle);
                    Log.d("Success","Leads_no:"+leads_no);
                }
                return null;
            }
        });

    }
    private void AllLeadsList(String id) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("customer_id",id);
        Log.d("Success","object_id::"+id);
        SendinBackground(Constants.CUS_LEADS,object);
    }
    private void SendinBackground(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url,object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                hideProgress();
                Log.d("error:::::", "error::::: " + message);
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject obj = new JSONObject(res);
                        Log.d("error:","obj:::"+obj);
                        Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                hideProgress();
                Log.d("Success:","responce:::"+response);
                if (response.getString("status").equalsIgnoreCase("Success")){
                    JSONArray array=response.getJSONArray("data");
                    LeadsList=new ArrayList<>();
                    for (int i=0;i<array.length();i++) {
                        JSONObject obj = array.getJSONObject(i);
                        AllLeadsListModel model=new AllLeadsListModel();
                        model.setInvoince_no(obj.getString("enquiry_no"));
                        model.setIssue(obj.getString("enquiry_about"));
                        model.setDate(obj.getString("followup_date"));
                        model.setMobile(obj.getString("contact_number"));
                        model.setMobile2(obj.getString("contact_number_2"));
                        model.setStatus(obj.getString("status"));

                        if (obj.getString("category_image").isEmpty()){
                            model.setImage("");
                        }else {
                            model.setImage(obj.getString("category_image"));
                        }
                        model.setColor(obj.getString("status"));
                        model.setLeads_id(obj.getString("id"));
                        model.setCat_id(obj.getString("cat_id"));
                        LeadsList.add(model);
                    }
                    LinearLayoutManager lr=new LinearLayoutManager(getActivity());
                    full_lead_rec.setLayoutManager(lr);
                    FullLeadsAdapter adapter=new FullLeadsAdapter(getActivity(),LeadsList,null);
                    full_lead_rec.setAdapter(adapter);
                    Collections.reverse(LeadsList);



                }
                else {

                    if (response.getString("status").equalsIgnoreCase("false")) {
                        empty_leads.setVisibility(View.VISIBLE);
                        full_lead_rec.setVisibility(View.GONE);

                        String tex=response.getString("message");
                        empty_leads.setText(tex);

                    }
                }
                return null;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_leads, container, false);
    }

    @Override
    public void OnBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
        Log.d("Success","BackPressed::");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void showProgress() {
        full_lead_rec.setVisibility(View.GONE);
        loader_img.setVisibility(View.VISIBLE);
        add_lead_float.setVisibility(View.GONE);
        Log.d("Success","Show_progress");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void hideProgress() {
        full_lead_rec.setVisibility(View.VISIBLE);
        loader_img.setVisibility(View.GONE);
        add_lead_float.setVisibility(View.VISIBLE);
        Log.d("Success","Hide_progress");
    }
}
