package com.f2f.incls.fragment;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.VolleyError;
import com.f2f.incls.R;
import com.f2f.incls.activity.CustomerDashBoard;
import com.f2f.incls.adapter.EditLeadsAdapter;
import com.f2f.incls.model.EditLeadsModel;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class CustomerEditLeadsFragment extends Fragment implements LoadinInterface {
    Fragment fragment;
    ImageView lead_power,ad_image,customer_service,customer_leads;
    RelativeLayout status_layout;
    RelativeLayout ads_img_ly;
    Toolbar dashboard_tool,leads_list_tool,add_leads_tool,service_tool,warranty_tool,edit_tool;
    LinearLayout customer_options,edit_leads_loader,edit_leads_ly;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    TextView date,inv_number,Status_id;
    ProgressDialog dialog;
    String id,today_date,request_date,d,m,y;
    EditText  contact_1, contact_2,description;
    RecyclerView category_rec;
    ArrayList<EditLeadsModel> arrayList;
    ArrayList<EditLeadsModel>EditList;
    StringBuilder builder=new StringBuilder();
    Button update,cancel;
    String con_1=null;
    String con_2=null;
    String des=null;
    String pos=null;
    String cat_id=null;
    String cus_id=null;
    String lead_id=null;
    String invoice=null;
    String status=null;
    private Object container;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviewid(view);
         ((CustomerDashBoard)getActivity()).adsimageget();
        //Ads close method
        ((CustomerDashBoard)getActivity()).adsclose();
        EditList = new ArrayList<>();
        arrayList=new ArrayList<>();

        session = new SessionManager(getActivity());
        sharpre = this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
        dialog = new ProgressDialog(getActivity());
        today_date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Log.d("Success", "date::" + today_date);
        String[] split=today_date.split("/");
        d=split[0];
        m=split[1];
        y=split[2];
        builder.append(y).append("-").append(m).append("-").append(d).toString();
        request_date=new String(builder);
        Log.d("Success", "date::" + request_date);

        HashMap<String, String> user = session.getcustomerdetails();
        cus_id = user.get(KEY_USER_ID);

        pos = getArguments().getString("possion");
        con_1 = getArguments().getString("contact_1");
        con_2 = getArguments().getString("contact_2");
        des = getArguments().getString("issue");
        cat_id = getArguments().getString("cat_id");
        lead_id = getArguments().getString("leads_id");
        invoice=getArguments().getString("invoice_no");
        status=getArguments().getString("status");

        Log.d("Success","Edit_pos::"+pos);
        Log.d("Success","Edit_contact_1::"+con_1);
        Log.d("Success","Edit_contact_2::"+con_2);
        Log.d("Success","Edit_des::"+des);
        Log.d("Success","Edit_cat_id::"+cat_id);
        Log.d("Success","Edit_leads_id::"+lead_id);
        Log.d("Success","Edit_invoice::"+invoice);
        Log.d("Success","Edit_status::"+status);



        try {
            AddLeadsMethod(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int new_cat_id = 0;
                con_1 = (String) contact_1.getText().toString();
                con_2 = (String) contact_2.getText().toString();
                des = description.getText().toString();
                for (final EditLeadsModel model : arrayList) {
                    if (model.isChecked()) {
                        new_cat_id = model.getProduct_id();
                    }
                }
                Log.d("Success","new_cat_id:::"+new_cat_id);
                if (con_1.isEmpty() || des.isEmpty() || new_cat_id == 0 ||!(con_1.length()==10)) {
                    if (con_1.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter contact number",
                                Toast.LENGTH_SHORT).show();
                    }else if (!(con_1.length()==10)){
                        Toast.makeText(getActivity(), "Please enter valid contact number",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (des.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter description",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (new_cat_id == 0) {
                        Toast.makeText(getActivity(), "Please select category",
                                Toast.LENGTH_SHORT).show();
                    }

                }else {
                    try {
                        EditLeadMethod(String.valueOf(cus_id), String.valueOf(new_cat_id),con_1,con_2,des,request_date,lead_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new CustomerLeadsFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                ((CustomerDashBoard)getActivity()).edittoleads();
            }
        });
    }
    private void findviewid(View view) {
        lead_power=view.findViewById(R.id.lead_power);
        category_rec=view.findViewById(R.id.category_rec);
        contact_1 = view.findViewById(R.id.contact_1);
        contact_2 = view.findViewById(R.id.contact_2);
        description=view.findViewById(R.id.issue);
        cancel=view.findViewById(R.id.cancel);
        update=view.findViewById(R.id.update);
        date=view.findViewById(R.id.date);
        inv_number=view.findViewById(R.id.inv_number);
        Status_id=view.findViewById(R.id.Status_id);
        dashboard_tool=view.findViewById(R.id.dashboard_tool);
        customer_options=view.findViewById(R.id.customer_options);
        warranty_tool=view.findViewById(R.id.warranty_tool);
        service_tool=view.findViewById(R.id.service_tool);
        leads_list_tool=view.findViewById(R.id.leads_list_tool);
        add_leads_tool=view.findViewById(R.id.add_leads_tool);
        edit_tool=view.findViewById(R.id.edit_tool);
        edit_leads_loader=view.findViewById(R.id.edit_leads_loader);
        edit_leads_ly=view.findViewById(R.id.edit_leads_ly);
        status_layout=view.findViewById(R.id.status_layout);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomerDashBoard)getActivity()).editlistMethod();
    }
    private void EditLeadMethod(String cus_id, String cat_id, String con_1, String con_2,
                                String des, String today_date, String lead_id) throws JSONException {
           showProgress();
        JSONObject object=new JSONObject();
        object.put("customer_id",cus_id);
        object.put("cat_id",cat_id);
        object.put("contact_1",con_1);
        object.put("contact_2",con_2);
        object.put("description",des);
        object.put("followup_date",today_date);
        object.put("leads_id",lead_id);
        Log.d("Success","Request_Object::"+object);
        sendinBacground(Constants.CUS_EDIT_LEADS,object);
    }
    private void sendinBacground(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                hideProgress();
                Log.d("Success","Edit_leads_Responce::"+response);
                if (response.getString("status").equalsIgnoreCase("success")){
                    fragment=new CustomerLeadsFragment();
                    FragmentTransaction transaction=getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container,fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    ((CustomerDashBoard)getActivity()).edittoleads();
                }
                return null;
            }
        });
    }

    private void AddLeadsMethod(String id) throws JSONException {
           showProgress();
        SendInBackground(Constants.CUS_PRODUCT_CATEGORIES,null);
    }

    private void SendInBackground(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url,object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                hideProgress();
            }
            @Override
            public String onResponse(JSONObject response) throws JSONException {
                   hideProgress();
                if (response.getString("status").equalsIgnoreCase("success")){
                    Log.d("Success","Edit_category_Responce::"+response);
                    JSONArray array=response.getJSONArray("category");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        EditLeadsModel model=new EditLeadsModel();
                        model.setProduct_id(obj.getInt("cat_id"));
                        if (obj.getString("category_image").isEmpty()){
                            model.setImage("");
                        }else {
                            model.setImage(obj.getString("category_image"));
                        }
                        model.setProduct_name(obj.getString("categoryName"));
                        model.setOld_id(((cat_id)));
                        model.setChecked(false);
                        arrayList.add(model);
                    }
                    LinearLayoutManager lrt=new LinearLayoutManager(getActivity()
                            ,RecyclerView.HORIZONTAL,false);
                    category_rec.setLayoutManager(lrt);
                    EditLeadsAdapter adapter=new EditLeadsAdapter(getActivity(),arrayList);
                    category_rec.setAdapter(adapter);
                    contact_1.setText(con_1);
                    Log.d("Success","edit_test::"+con_1);
                    contact_2.setText(con_2);
                    description.setText(des);
                    date.setText(today_date);
                    inv_number.setText(invoice);

                    if (status.equals("order_conform")){
                        Status_id.setText("Order Confirm");
                        Status_id.setTextColor(Color.parseColor("#06801a"));
                    }
                    if (status.equals("leads_rejected") || status.equals("quotation_rejected")){
                        if (status.equals("leads_rejected")){
                            Status_id.setText("Leads Rejected");
                            Status_id.setTextColor(Color.parseColor("#fd0606"));
                        }else {
                            Status_id.setText("Quotation Rejected ");
                            Status_id.setTextColor(Color.parseColor("#fd0606"));
                        }
                    }
                    if (status.equals("leads_follow_up") || status.equals("leads")
                            || status.equals("quotation") || status.equals("quotation_follow_up")){
                        if (status.equals("leads_follow_up")){
                            Status_id.setText("Leads Follow Up");
                            Status_id.setTextColor(Color.parseColor("#ff7e00"));
                        }else if (status.equals("leads")){
                            Status_id.setText("Leads");
                            Status_id.setTextColor(Color.parseColor("#ff7e00"));
                        }else if (status.equals("quotation")){
                            Status_id.setText("Quotation");
                            Status_id.setTextColor(Color.parseColor("#ff7e00"));
                        }else if (status.equals("quotation_follow_up")) {
                            Status_id.setText("Quotation Follow Up");
                            Status_id.setTextColor(Color.parseColor("#ff7e00"));
                        }
                    }

                }else {

                }
                return null;
            }
        });
    }

    public void editlistMethod() {
        dashboard_tool.setVisibility(View.GONE);
        customer_options.setVisibility(View.GONE);
        warranty_tool.setVisibility(View.GONE);
        service_tool.setVisibility(View.GONE);
        leads_list_tool.setVisibility(View.GONE);
        add_leads_tool.setVisibility(View.GONE);
        edit_tool.setVisibility(View.VISIBLE);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_leads,  container, false);
    }

    @Override
    public void showProgress() {
        edit_leads_loader.setVisibility(View.VISIBLE);
        status_layout.setVisibility(View.GONE);
        edit_leads_ly.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        edit_leads_loader.setVisibility(View.GONE);
        edit_leads_ly.setVisibility(View.VISIBLE);
        status_layout.setVisibility(View.VISIBLE);

    }
}
