package com.f2f.incls.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.f2f.incls.R;
import com.f2f.incls.fragment.EmployeeProfileFragment;
import com.f2f.incls.model.AdsScrollTextModel;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.Statusbar;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import static com.f2f.incls.activity.OTPVerifyActivity.otp_fab;
import static com.f2f.incls.activity.OTPVerifyActivity.otp_verify_fab;
import static com.f2f.incls.fragment.CustomerProfileFragment.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.f2f.incls.utilitty.SessionManager.KEY_EMAIL;
//import static com.f2f.incls.utilitty.SessionManager.KEY_LOGINDATE;
//import static com.f2f.incls.utilitty.SessionManager.KEY_LOGINDATE;
//import static com.f2f.incls.utilitty.SessionManager.KEY_LOGINDATE;
import static com.f2f.incls.utilitty.SessionManager.KEY_MOBILE;
import static com.f2f.incls.utilitty.SessionManager.KEY_NAME;
import static com.f2f.incls.utilitty.SessionManager.KEY_PASSWORD;
import static com.f2f.incls.utilitty.SessionManager.KEY_STORE;
import static com.f2f.incls.utilitty.SessionManager.KEY_TYPE;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_NAME;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_PROFILE;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoadinInterface {
    RadioGroup log_radio;
    RadioButton customer_btn;
    LinearLayout customer_layout,employee_layout,signup_ly,loader_ly,background_ly,dont_acc;
    EditText cus_log_mobile,cus_log_password,emp_mobile,emp_password,emp_location;
    FloatingActionButton cus_log_fab,emp_fab;
    String usertype="Customer" , email;
    TextView signup_tv;
    TextView scroll_txt;
    TextView scrollingtext;
    TextView txtloginresend;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    CardView cardview,loader_card;
    TextInputLayout cus_mobile_in_ly,cus_pass_in_ly,emp_name_in_ly,emp_pass_in_ly,emp_loc_in_ly;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ArrayList<AdsScrollTextModel> scrollList;
    ArrayList<String> adimageList;
    ViewPager ad_view_pager;
    int currentPage = 0;
    Timer timer;
    String email_otp,mobile,name,password,id,type;
    FloatingActionButton otp_fab,otp_verify_fab;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findview();
        adsimageget();
        session=new SessionManager(getApplicationContext());
        sharpre = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
        Intent intent1=getIntent();
        final Bundle bundle=intent1.getExtras();
     //   type = "Employee" ;






        /*if (bundle!=null){
            email_otp= (String) bundle.get("email_id");
             id= String.valueOf(bundle.get("customer_id"));


        }*/


        cus_log_fab.setOnClickListener(this);
        emp_fab.setOnClickListener(this);
        dialog =new ProgressDialog(this);
        if (!LoginActivity.this.isFinishing()){
            dialog.hide();
        }
        Statusbar.darkenStatusBar(this, R.color.colorApp);

        if(sharpre.getBoolean("ISLogIn",false)){
            HashMap<String, String> user = session.getcustomerdetails();
            type = user.get(session.KEY_TYPE);
             id = user.get(session.KEY_USER_ID);
            Log.d("Test","log_in_test::"+id);
            Log.d("Test","log_in_test::"+type);
            try {
                getloginstatus(id,type);
            } catch (JSONException e) {
                e.printStackTrace();
                e.printStackTrace();
            }
            if (type.equalsIgnoreCase("customer")){
                Intent intent=new Intent(LoginActivity.this, CustomerDashBoard.class);
                startActivity(intent);
            }else if (type.equalsIgnoreCase("employee")){

                try {
                    getloginstatus(id,type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


         /*       Intent intent=new Intent(LoginActivity.this,EmployeeDashboard.class);
                intent.putExtra("id",id);
                startActivity(intent);*/

            }else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }

        log_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                customer_btn=findViewById(checkedId);
                usertype =customer_btn.getText().toString();
                if (usertype.equals("Customer")){
                    customer_layout.setVisibility(View.VISIBLE);
                    signup_tv.setVisibility(View.VISIBLE);
                    dont_acc.setVisibility(View.VISIBLE);
                    employee_layout.setVisibility(View.GONE);
                    txtloginresend.setVisibility(View.GONE);
                    Log.d("Test","Customer::");
                }else {
                    employee_layout.setVisibility(View.VISIBLE);
                    customer_layout.setVisibility(View.GONE);
                    signup_tv.setVisibility(View.GONE);
                    dont_acc.setVisibility(View.GONE);
                    txtloginresend.setVisibility(View.GONE);
                    Log.d("Test","Employee::");
                }
            }
        });

        signup_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
        txtloginresend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                //     email_otp = user.get(KEY_EMAIL,"");
                email_otp = sharpre.getString(KEY_EMAIL,"");
                //  email_otp =bundle.getString("email_id");
               /* Bundle bundle = getIntent().getExtras();
                txtloginresend.setText(" "+bundle.getString("email_id"));*/
                Log.d("Success","email_otp11::"+email_otp);
                try {
                    getresendvalue(email_otp);
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        });



        background_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        cus_log_mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("Success","keyboard_test_v::"+v);
                Log.d("Success","keyboard_test_focus::"+hasFocus);
                if (!hasFocus) {
                    hideKeyboard(v);
                    Log.d("Success","keyboard_test::");

                }
            }
        });

        cus_log_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==10){
                    cus_mobile_in_ly.setError(null);
                    cus_log_password.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!cus_log_mobile.getText().toString().isEmpty()){
                    cus_mobile_in_ly.setError(null);
                }
            }
        });

        cus_log_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==3){
                    cus_pass_in_ly.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!cus_log_password.getText().toString().isEmpty()){
                    cus_pass_in_ly.setError(null);
                }
            }
        });


        emp_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==3){
                    emp_name_in_ly.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!emp_mobile.getText().toString().isEmpty()){
                    emp_name_in_ly.setError(null);
                }
            }
        });

        emp_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==3){
                    emp_pass_in_ly.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!emp_password.getText().toString().isEmpty()){
                    emp_pass_in_ly.setError(null);
                }
            }
        });

        emp_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==3){
                    emp_loc_in_ly.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!emp_location.getText().toString().isEmpty()){
                    emp_loc_in_ly.setError(null);
                }
            }
        });

    }

    public void findview(){
        ad_view_pager=findViewById(R.id.ad_view_pager);
        emp_location=findViewById(R.id.emp_location);
        emp_loc_in_ly=findViewById(R.id.emp_loc_in_ly);
        customer_layout=findViewById(R.id.customer_layout);
        cus_log_fab=findViewById(R.id.cus_log_fab);
        cus_log_mobile=findViewById(R.id.cus_log_mobile);
        cus_log_password=findViewById(R.id.cus_log_password);
        employee_layout=findViewById(R.id.employee_layout);
        log_radio=findViewById(R.id.log_radio);
        txtloginresend=findViewById(R.id.txtloginresend);
        customer_btn=findViewById(R.id.customer_btn);
        signup_tv=findViewById(R.id.signup_tv);
        emp_fab=findViewById(R.id.emp_fab);
        emp_mobile=findViewById(R.id.emp_mobile);
        emp_password=findViewById(R.id.emp_password);
        cus_mobile_in_ly=findViewById(R.id.cus_mobile_in_ly);
        cus_pass_in_ly=findViewById(R.id.cus_pass_in_ly);
        emp_name_in_ly=findViewById(R.id.emp_name_in_ly);
        emp_pass_in_ly=findViewById(R.id.emp_pass_in_ly);
        cardview=findViewById(R.id.cardview);
        loader_card=findViewById(R.id.loader_card);
        signup_ly=findViewById(R.id.signup_ly);
        loader_ly=findViewById(R.id.loader_ly);
        background_ly=findViewById(R.id.background_ly);
        dont_acc=findViewById(R.id.dont_acc);
        scrollingtext=findViewById(R.id.scrollingtext);

    }

    private boolean isValidMail(String email) {

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }
    private boolean isValidMobile(String phone) {
        if(!(Pattern.matches("[a-zA-Z]+", phone))) {
            return phone.length() > 6 && phone.length() == 10;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cus_log_fab:
                try {
                    String mobile = cus_log_mobile.getText().toString();
                    String password = cus_log_password.getText().toString();
                    if (mobile.isEmpty() || password.isEmpty() ||
                            !isValidMobile(mobile)) {
                        if (mobile.isEmpty()) {
                            cus_mobile_in_ly.setError("Please enter  mobile number");
                        }else if ( !isValidMobile(mobile)) {
                            cus_mobile_in_ly.setError("Enter valid  mobile number");
                        } if (password.isEmpty()) {
                            cus_pass_in_ly.setError("Please enter password");
                        }
                    }else {
                        getValues(cus_log_mobile.getText().toString(), cus_log_password.getText().toString());
                        Log.d("costomer", "Customer_login:::");
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }

                break;
            case R.id.emp_fab:
                try {
                    String mobile = emp_mobile.getText().toString();
                    String password = emp_password.getText().toString();
                    String location = emp_location.getText().toString();
                    if (mobile.isEmpty() || password.isEmpty() ||location.isEmpty()) {
                        if (mobile.isEmpty()) {
                            emp_name_in_ly.setError("Please enter  employee name");
                        }
                        if (password.isEmpty()) {
                            emp_pass_in_ly.setError("Please enter password");
                        }
                        if (location.isEmpty()){
                            emp_loc_in_ly.setError("Please enter location");
                        }
                    }else {
                        getValuesemp(emp_mobile.getText().toString(), emp_password.getText().toString(),emp_location.getText().toString());
                        Log.d("costomer", "Customer_login:::");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

    }

    public void getValues(String mobile,String password) throws JSONException {
      /*  dialog.setMessage("Login");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();*/
        showProgress();
        JSONObject object=new JSONObject();
        object.put("mobile_number",mobile);
        object.put("password",password);
        Log.d("Succes","Login_Test::"+object);
        sendInBackground(Constants.LOGIN_URL, object);
    }
    public void sendInBackground(String url, final JSONObject loginJsonObject) {
        try {

            VolleyUtils.makeJsonObjectRequest(LoginActivity.this, url,loginJsonObject, new VolleyCallback() {
                @Override
                public void onError(String message,VolleyError error) {
                    //     dialog.cancel();
                    hideProgress();
//                    unlockScreenOrientation();
                    Log.d("error:::::", "error::::: " + message);
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                            Log.d("error:","obj:::"+obj);
                            Toast.makeText(LoginActivity.this,obj.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
//
                }

                @Override
                public String onResponse(JSONObject response) {
                    //  dialog.cancel();
                    hideProgress();
                    Log.d("error:::::", "customer_responce::: " + response);
//                    dialogMigrate.dismiss();
                    try {
//                        Log.d(response.getString("status").equals("error"),"sjdfisjfd");
                        if(response.getString("status").equalsIgnoreCase("Success")){

                            String pass=cus_log_password.getText().toString();
                            editor.remove(KEY_PASSWORD);
                            editor.putString(KEY_PASSWORD,pass);
                            editor.commit();

                            JSONArray jsonArray=response.getJSONArray("data");
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String id = obj.getString("id");
                                Log.d("Success","Customer_id::"+id);
                                String name = obj.getString("name");
                                String store_name = obj.getString("store_name");
                                //  String password = obj.getString("password");
                                String mobile_number = obj.getString("mobil_number");
                                String email = obj.getString("email_id");
                                String cutomer = obj.getString("type");
                                String profile_image=obj.getString("profile_image");
                                Log.d("Success","profile_test_login::"+profile_image);
                                editor.remove(KEY_USER_ID);
                                editor.remove(KEY_NAME);
                                editor.remove(KEY_STORE);
                                editor.remove(KEY_PASSWORD);
                                editor.remove(KEY_MOBILE);
                                editor.remove(KEY_EMAIL);
                                editor.remove(KEY_TYPE);
                                editor.remove(KEY_USER_PROFILE);
                                editor.putString(KEY_USER_ID,id);
                                editor.putString(KEY_USER_NAME,name);
                                editor.putString(KEY_STORE,store_name);
                                editor.putString(KEY_PASSWORD,pass);
                                editor.putString(KEY_MOBILE,mobile_number);
                                editor.putString(KEY_EMAIL,email);
                                editor.putString(KEY_TYPE,cutomer);
                                editor.putString(KEY_USER_PROFILE,profile_image);
                                session.createcustomerLoginSession(id, name, store_name,
                                        pass,"", mobile_number, email, cutomer,profile_image);
                                Log.d("Success","Customer_name_test::"+name);
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), CustomerDashBoard.class);
                                startActivity(intent);
                            }
                        }if(response.getString("status").equalsIgnoreCase("error")){
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            txtloginresend.setVisibility(View.GONE);
                            Log.d("success", "toast4534" + response.getString("message"));
                        }if(response.getString("status").equalsIgnoreCase("error") && response.getString("error_type").equalsIgnoreCase("otp_not_verified")){
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            txtloginresend.setVisibility(View.VISIBLE);
                            String email_id =  response.getString("email_id");
                            Log.d("Success","email_id1231::"+email_id);
                            editor.putString(KEY_EMAIL,email_id);
                            editor.commit();
                            Log.d("success","toastffg4534"+response.getString("message"));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
//                                    dialogMigrate.dismiss();
                        Log.e("excpetion", "error:: " + e);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
//            unlockScreenOrientation();
       /*     if (dialogMigrate.isShowing()) {
                dialogMigrate.dismiss();
            }*/
            e.printStackTrace();
            Log.e("exception", "excepted" + e);
        }
    }

    public void getValuesemp(String mobile,String password,String location) throws JSONException {
       /* dialog.setMessage("Login");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();*/
        showProgress();
        JSONObject object = new JSONObject();
        object.put("username", mobile);
        object.put("password", password);
        object.put("login_location", location);
        Log.d("success","Emplogin"+object);
        dobackemp(Constants.EMP_LOGIN_URL, object);

    }

    private void dobackemp(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getApplicationContext(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                hideProgress();
            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                hideProgress();
                String pass=emp_password.getText().toString();
                String path=null;
                editor.remove(KEY_PASSWORD);
                editor.putString(KEY_PASSWORD,pass);
                editor.commit();
                //    String login_date=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                Log.d("Success","emp_log_responce::"+response);
                if(response.getString("status").equalsIgnoreCase("Success")){
                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        String emp_id=obj.getString("id");
                        String emp_name=obj.getString("username");
                        String emp_mobile=obj.getString("mobile_no");
                        String emp_email=obj.getString("email_id");
                        String profile=obj.getString("admin_image");
                        String login_date=obj.getString("login_date");
                        // login_date=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        String type="employee";

                        Log.d("Success","test_emp_id::"+emp_id);
                        Log.d("Success","tset_name::"+emp_name);
                        editor.remove(KEY_USER_ID);
                        editor.remove(KEY_USER_NAME);
                        editor.remove(KEY_PASSWORD);
                        editor.remove(KEY_MOBILE);
                        editor.remove(KEY_EMAIL);
                        editor.remove(KEY_USER_PROFILE);
                        editor.putString(KEY_USER_ID,emp_id);
                        editor.putString(KEY_USER_NAME,emp_name);
                        editor.putString(KEY_PASSWORD,pass);
                        editor.putString(KEY_MOBILE,emp_mobile);
                        editor.putString(KEY_EMAIL,emp_email);
                        editor.putString(KEY_USER_PROFILE,profile);
                        //        editor.putString(KEY_LOGINDATE,login_date);
                        Log.d("success","LOGIN$$$"+login_date);


                        session.createempLoginSession(emp_id,emp_name,"",pass,emp_mobile,
                                emp_email,"","","",type,profile);
                        editor.commit();
                        Intent intent=new Intent(getApplicationContext(),EmployeeDashboard.class);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(getApplicationContext(),response.getString("message")
                            ,Toast.LENGTH_SHORT).show();
                }
                return pass;
            }
        });
    }

    /*    private void getresendvalue(String email_otp) throws JSONException {
            showProgress();
            JSONObject object=new JSONObject();
            object.put("email_id",email_otp);
            sendInBackgroundResend(Constants.CUS_LOGIN_RESEND_OTP,object);
            Log.d("success","getresendvalue"+object);
        }
        private void sendInBackgroundResend(String url, JSONObject object) {

            VolleyUtils.makeJsonObjectRequest(OTPVerifyActivity.this, url,object, new VolleyCallback() {
                @Override
                public void onError(String message, VolleyError error) {
                    //    dialog.cancel();
                    hideProgress();
                    Log.d("message", "error:: " + message);
                    Log.d("VolleyError", "error:: " + error);
                    NetworkResponse response=error.networkResponse;
                    if (error instanceof ServerError && response!=null){
                        try {
                            String res=new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers));
                            JSONObject obj=new JSONObject(res);
                            Toast.makeText(OTPVerifyActivity.this,obj.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Log.d("excpetion", "error:: " + e);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("excpetion", "error:: " + e);
                        }
                    }
                }
                @Override
                public String onResponse(JSONObject response) {
                    hideProgress();
                    Log.d("Success", "responce:: " + response);
                    try {
                        if (response.getString("status").equalsIgnoreCase("Success")){
                            Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("excpetion", "error:: " + e);
                    }
                    return null;
                }
            });
        }*/
    private void getresendvalue(String email_otp) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("email_id",email_otp);
        sendInBackgroundResend(Constants.CUS_LOGIN_RESEND_OTP,object);
        Log.d("success","getresendvalue"+object);
    }
    private void sendInBackgroundResend(String url, JSONObject object) {
        Log.d("Success:::", "background::");
        try {
            VolleyUtils.makeJsonObjectRequest(LoginActivity.this, url,object, new VolleyCallback() {
                @Override
                public void onError(String message, VolleyError error) {
                    hideProgress();
                    Log.d("message", "error:: " + message);
                    Log.d("VolleyError", "error:: " + error);
                    NetworkResponse response=error.networkResponse;
                    if (error instanceof ServerError && response!=null){
                        try {
                            String res=new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers));
                            JSONObject obj=new JSONObject(res);
                            Toast.makeText(LoginActivity.this,obj.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Log.d("excpetion", "error:: " + e);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("excpetion", "error:: " + e);
                        }
                    }
                }

                @SuppressLint("RestrictedApi")
                @Override
                public String onResponse(JSONObject response) throws JSONException {
                    hideProgress();
                    Log.d("Success", "loginresponce:: " + response);
                    try {
                        String mobile = cus_log_mobile.getText().toString();
                        String pass = cus_log_password.getText().toString();
                        String name="";

                        String id = "";

                        Log.d("Success", "password_test_inside_responce2::" + mobile);
                        Log.d("Success", "password_test_inside_responce3::" + name);
                        if (response.getString("status").equalsIgnoreCase("Success")) {
                            JSONArray array = response.getJSONArray("customer_data");
                            Log.d("Success", "password_test_inside_responce8::" + array);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                mobile = object.getString("mobil_number");
                                pass = object.getString("password");
                                name=object.getString("name");
                                id = object.getString("id");




                                //       otp_pincode = object.getString("otp_pincode");
                            }
                            editor.putString(KEY_USER_ID, id);
                            editor.commit();
                            Log.d("email", "customer_id::" + id);
                            Intent intent = new Intent(LoginActivity.this, OTPVerifyActivity.class);
                            intent.putExtra("email_id", email);
                            intent.putExtra("mobile_number", mobile);
                            intent.putExtra("name", name);
                            intent.putExtra("password", pass);
                            intent.putExtra("customer_id", id);
                            startActivity(intent);
                        }

                        //   Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {

                        Log.d("excpetion", "error:: " + e);
                    }
                    return null;
                }
            });
        }
        catch (Exception e) {
//            unlockScreenOrientation();

            Log.e("exception", "excepted" + e);
        }
    }


    public void getloginstatus(String id,String type) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("user_type","1");
        object.put("user_id",id);
        Log.d("Succes","Login_Test123::"+object);
        sendInBackground1(Constants.LOGIN_STATUS, object);
    }

    public void sendInBackground1(String url, final JSONObject loginJsonObject) {
        try {

            VolleyUtils.makeJsonObjectRequest(LoginActivity.this, url,loginJsonObject, new VolleyCallback() {
                @Override
                public void onError(String message,VolleyError error) {
                    //     dialog.cancel();
                    hideProgress();
//                    unlockScreenOrientation();
                    Log.d("error:::::", "error::::: " + message);
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                            Log.d("error:","obj:::"+obj);
                            Toast.makeText(LoginActivity.this,obj.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
//
                }

                @Override
                public String onResponse(JSONObject response){
                  //    dialog.cancel();
                    hideProgress();
                    Log.d("success", "customerloginstatus::: " + response);
//                    dialogMigrate.dismiss();
                    try {
                     //   if (response.getString("status").equalsIgnoreCase("success")) {
                            if (response.getString("login_status").equalsIgnoreCase("1")) {
                                Intent status = new Intent(getApplicationContext(),EmployeeDashboard.class);
                                status.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(status);

                               // finish();
                            } else {
                                Intent status1 = new Intent(getApplicationContext(), LoginActivity.class);
                                status1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(status1);
                             //   finish();
                            }

                       // }
                    } catch (Exception e) {
                        e.printStackTrace();
//                                    dialogMigrate.dismiss();
                        Log.e("excpetion", "error:: " + e);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("exception", "excepted" + e);
        }
    }
/*    private void getresendvalue(String email_otp) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("email_id",email_otp);
        sendInBackgroundResend(Constants.CUS_LOGIN_RESEND_OTP,object);
        Log.d("success","getresendvalue"+object);
    }
    private void sendInBackgroundResend(String url, JSONObject object) {

        VolleyUtils.makeJsonObjectRequest(LoginActivity.this, url,object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                //    dialog.cancel();
                hideProgress();
                Log.d("message", "error:: " + message);
                Log.d("VolleyError", "error:: " + error);
                NetworkResponse response=error.networkResponse;
                if (error instanceof ServerError && response!=null){
                    try {
                        String res=new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        JSONObject obj=new JSONObject(res);
                        Toast.makeText(LoginActivity.this,obj.getString("message"),Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.d("excpetion", "error:: " + e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("excpetion", "error:: " + e);
                    }
                }
            }
            @Override
            public String onResponse(JSONObject response) {
                hideProgress();
                Log.d("Success", "responce:: " + response);
                try {
                    if (response.getString("status").equalsIgnoreCase("Success")){
                        Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),OTPVerifyActivity.class);


                        String customer="customer";
                        session.createcustomerLoginSession(id, name, "", password,"", mobile, email_otp, customer, "");
                        startActivity(intent);
                       *//* editor.remove(KEY_EMAIL);
                        editor.putString(KEY_EMAIL,email_otp);
                        editor.commit();*//*
                        }else {
                        Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("excpetion", "error:: " + e);
                }
                return null;
            }
        });
    }*/

    private void adsimageget() {
        VolleyUtils.makeJsonObjectRequest(this, Constants.ADS_API, null, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
            }
            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success", "Responce_Ads::" + response);
                if (response.getString("status").equalsIgnoreCase("success")) {
                    adimageList=new ArrayList<>();
                    scrollList=new ArrayList<>();
                    JSONArray array = response.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        JSONArray details_array = object.getJSONArray("ads_details");
                        for (int j = 0; j < details_array.length(); j++) {
                            JSONObject obj1 = details_array.getJSONObject(j);
                            String image = obj1.getString("ads_data_link");

                            if (image.contains("jpg")||image.contains("jpeg") || image.contains("png")) {
                                Log.d("Success", "ads_content_2_conditions::" + image);
                            } else {
                                String text=obj1.getString("ads_data");
                                Log.d("Message","ScrollText_test::"+text);
                                AdsScrollTextModel model = new AdsScrollTextModel();
                                model.setScrolltext(text);
                                scrollList.add(model);

                               /* String text = obj1.getString("ads_data");
                                Log.d("Success", "scrolltext_test::"+text);
                                scrollingtext.setText(text);
                                scrollingtext.setSelected(true);*/
                            }
                            if (obj1.getString("ads_data_link").equalsIgnoreCase("null")) {

                            } else {
                                String path = obj1.getString("ads_data_link");
                                Log.d("Success", "Ads_image_path::" + path);
                                adimageList.add(path);
                            }

                        }
                        scrollmethod(scrollList);

                    }
                }
                return null;
            }
        });
    }

    private void setAutopager() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run()
            {
                ad_view_pager.setCurrentItem(currentPage, true);
                if(currentPage == adimageList.size())
                {
                    currentPage = 0;
                    Log.d("Success","Scroll_if::");
                }
                else
                {
                    ++currentPage ;
                    Log.d("Success","Scroll_else::");
                }
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 4500);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                }).setNegativeButton("No", null)
                .create()
                .show();
    }
    public void showProgress() {
        signup_ly.setVisibility(View.GONE);
     //   cardview.setVisibility(View.GONE);
      //  loader_card.setVisibility(View.VISIBLE);



    }
    public void hideProgress() {
        signup_ly.setVisibility(View.VISIBLE);
      // cardview.setVisibility(View.VISIBLE);
       // loader_card.setVisibility(View.GONE);

    }
    public void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Log.d("Success","keyboard_test_inside::");
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // cameraIntent();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
               /* if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }*/
                break;
        }
    }





    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private void scrollmethod(ArrayList<AdsScrollTextModel> arrayList) {
        Log.d("Success", "ads_arraylist_size::" + arrayList.size());
        for (int i=0;i<arrayList.size();i++){
            AdsScrollTextModel model1=arrayList.get(i);
            String path=model1.getScrolltext();
            int checklength=150;
            if (path.length()<checklength){
                int total=checklength-path.length();
                for (int j=0;j<total;j++) {
                    path =path + " ";

                }
            }
            scrollingtext.setText(path);
            scrollingtext.setMarqueeRepeatLimit(-1);

            scrollingtext.setText(path);
            scrollingtext.setSelected(true);
            Log.d("Success","scroll_text_test::"+path);
        }
    }
}

