package com.f2f.incls.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.f2f.incls.R;
import com.f2f.incls.activity.CustomerDashBoard;
import com.f2f.incls.adapter.AddLeadsAdapter;
import com.f2f.incls.model.AddLeadsModel;
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

import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class AddLeadsFragment extends Fragment implements LoadinInterface {

    Fragment fragment;
    FrameLayout add_leads_frame;
    ImageView lead_power,ad_image,customer_service,customer_leads;
    RelativeLayout ads_img_ly;
    LinearLayout add_leads_ly,add_leads_loader;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    TextView scrollingtext,date,inv_number;
    EditText customer_contact_1,customer_contact_2,add_leads_des;
    ProgressDialog dialog;
    String customer_id,today_date,request_date,d,m,y,leads=null;
    StringBuilder builder=new StringBuilder();
    RecyclerView category_rec;
    Button submit,cancel;


    ArrayList<AddLeadsModel> arrayList;
    int[] default_img={R.drawable.appbar_logo};
     View viewContainer;
     InputMethodManager imm;
    private Object container;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviewid(view);

        session=new SessionManager(getActivity());
        sharpre =this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
        dialog=new ProgressDialog(getActivity());
        ((CustomerDashBoard)getActivity()).adsclose();
        ((CustomerDashBoard)getActivity()).adsimageget();
        AddLeadsMethod();
        leadnumber();

        HashMap<String,String>map=session.getcustomerdetails();
        customer_id=map.get(session.KEY_USER_ID);
        String name=map.get(session.KEY_USER_NAME);

        today_date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        date.setText(today_date);

        String[] split=today_date.split("/");
        d=split[0];
        m=split[1];
        y=split[2];
        builder.append(y).append("-").append(m).append("-").append(d).toString();
        request_date=new String(builder);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cat_id=0;
                final String number_1=(String) customer_contact_1.getText().toString();
                final String number_2=(String) customer_contact_2.getText().toString();
                final String des=add_leads_des.getText().toString();
                for(final AddLeadsModel addLeadsModel:arrayList){
                    if(addLeadsModel.isChecked()){
                        cat_id=addLeadsModel.getProduct_id();
                    }
                }

                if (number_1.isEmpty() || des.isEmpty() || cat_id==0 || !(number_1.length()==10)) {
                    if (number_1.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter contact number",
                                Toast.LENGTH_SHORT).show();
                    }else if (!(number_1.length()==10)){
                        Toast.makeText(getActivity(), "Please enter valid contact number",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (des.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter description",
                                Toast.LENGTH_SHORT).show();
                    }if (cat_id==0){
                        Toast.makeText(getActivity(), "Please select category",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        addleads(customer_id, cat_id, number_1, number_2, des, request_date);
                        Log.d("Method", "cus_id::" + customer_id);
                        Log.d("Method", "des::" + des);
                        Log.d("Method", "date::" + today_date);
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
                ((CustomerDashBoard)getActivity()).addleads();
                loadfragment(fragment);
            }
        });
    }




    private void findviewid(View view) {
        add_leads_frame=view.findViewById(R.id.add_leads_frame);
        lead_power=view.findViewById(R.id.lead_power);
        category_rec=view.findViewById(R.id.category_rec);
        date=view.findViewById(R.id.date);
        customer_contact_1=view.findViewById(R.id.customer_contact_1);
        customer_contact_2=view.findViewById(R.id.customer_contact_2);
        add_leads_des=view.findViewById(R.id.add_leads_des);
        submit=view.findViewById(R.id.submit);
        cancel=view.findViewById(R.id.cancel);
      //  scrollingtext=view.findViewById(R.id.scrollingtext);
        inv_number=view.findViewById(R.id.inv_number);
        add_leads_ly=view.findViewById(R.id.add_leads_ly);
        add_leads_loader=view.findViewById(R.id.add_leads_loader);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    private void leadnumber() {
        VolleyUtils.makeJsonObjectRequest(getActivity(), Constants.CUS_LEADS_NUMBER, null, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                Log.d("Success","messsage_test::"+message);
                Log.d("Success","erroe_test::"+error);

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success","Leads_Number_Reques::"+response);
                if (response.getString("status").equalsIgnoreCase("success")){

                    String leads_no=response.getString("Leads Number");
                    inv_number.setText(leads_no);

                }
                return null;
            }
        });
    }

    private void addleads(String customer_id, int pos, String customer_contact_1,
                          String customer_contact_2, String des, String today_date) throws JSONException {

        JSONObject object=new JSONObject();
        object.put("customer_id",customer_id);
        object.put("cat_id",pos);
        object.put("contact_1",customer_contact_1);
        object.put("contact_2",customer_contact_2);
        object.put("description",des);
        object.put("followup_date",today_date);
        Log.d("Success","possiton::"+pos);
        Log.d("Success","object_req::"+object);
        sendbackground(Constants.CUS_ADD_LEADS,object);
        //  session.addleads(customer_contact_1,customer_contact_2,des);
    }

    private void sendbackground(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success","Add_lead_Responce::"+response);
                if (response.getString("status").equalsIgnoreCase("success")){
                    fragment=new CustomerLeadsFragment();
                    ((CustomerDashBoard)getActivity()).addleads();
                    loadfragment(fragment);
                }
                return null;
            }
        });
    }

    private void loadfragment(Fragment fragment) {
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void AddLeadsMethod() {
       showProgress();
        SendInBackground(Constants.CUS_PRODUCT_CATEGORIES);
    }

    private void SendInBackground(String url) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url,null, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                hideProgress();
            }
            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success","add_leads_responce::"+response);
              //  dialog.cancel();
                hideProgress();
                if (response.getString("status").equalsIgnoreCase("success")){
                    arrayList=new ArrayList<>();
                    JSONArray array=response.getJSONArray("category");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        AddLeadsModel model=new AddLeadsModel();
                        model.setProduct_id(obj.getInt("cat_id"));
                        if (obj.getString("category_image").isEmpty()){
                            model.setProduct_image("");
                         //   arrayList.add(model);
                        }else {
                            model.setProduct_image(obj.getString("category_image"));
                        }
                        model.setProduct_name(obj.getString("categoryName"));
                        model.setChecked(false);
                        arrayList.add(model);
                    }
                    LinearLayoutManager lrt=new LinearLayoutManager(getActivity()
                            ,RecyclerView.HORIZONTAL,false);
                    category_rec.setLayoutManager(lrt);
                    AddLeadsAdapter adapter=new AddLeadsAdapter(getActivity(),arrayList);
                    category_rec.setAdapter(adapter);

                }else {
                    Toast.makeText(getActivity(),response.getString("meassage"),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        });
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_leads,  container, false);
    }

    @Override
    public void showProgress() {
        add_leads_loader.setVisibility(View.VISIBLE);
        add_leads_ly.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        add_leads_loader.setVisibility(View.GONE);
        add_leads_ly.setVisibility(View.VISIBLE);
    }
    private void hideKeyboard(View view) {


    }
}
