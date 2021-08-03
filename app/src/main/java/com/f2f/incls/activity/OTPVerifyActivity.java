package com.f2f.incls.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.f2f.incls.R;
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
import java.util.ArrayList;

import static com.f2f.incls.utilitty.SessionManager.KEY_EMAIL;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class OTPVerifyActivity extends AppCompatActivity implements View.OnClickListener, LoadinInterface {
    TextInputLayout otp_in_ly,email_in_ly;
    EditText otp_verify,otp_1,otp_2,otp_3,otp_4;
    public static FloatingActionButton otp_fab,otp_verify_fab;
    String email_otp,mobile,name,password,id,id1,id2;
    Button otp_resend;
    ProgressDialog dialog;
    AlertDialog aler;
    LinearLayout otp_ly;
    TextView scrollingtext,email_tv;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    CardView reg_card,loader_card;


    @Override
    public void onBackPressed() {

        if (!otp_1.getText().toString().isEmpty() ||!otp_2.getText().toString().isEmpty() ||
                !otp_3.getText().toString().isEmpty() || !otp_4.getText().toString().isEmpty()) {

            if (otp_2.getText().toString().isEmpty()&&
                    otp_3.getText().toString().isEmpty() &&
                    otp_4.getText().toString().isEmpty()){
                otp_1.getText().clear();
                //  otp_1.requestFocus();
                otp_1.clearFocus();
                otp_2.clearFocus();
                otp_3.clearFocus();
                otp_4.clearFocus();
            }
            if (otp_3.getText().toString().isEmpty() && otp_4.getText().toString().isEmpty()){
                otp_2.getText().clear();
                // otp_2.requestFocus();
                otp_1.clearFocus();
                otp_2.clearFocus();
                otp_3.clearFocus();
                otp_4.clearFocus();
            }
            if (otp_4.getText().toString().isEmpty()){
                otp_3.getText().clear();
                otp_1.clearFocus();
                otp_2.clearFocus();
                otp_3.clearFocus();
                otp_4.clearFocus();
                Log.d("Success","otp_true::");
            }
            if (!otp_4.getText().toString().isEmpty()){
                otp_4.getText().clear();
                otp_1.clearFocus();
                otp_2.clearFocus();
                otp_3.clearFocus();
                otp_4.clearFocus();

            }
            if (otp_1.length()==1&&otp_2.length()==1&&otp_3.length()==1&&otp_4.length()==0){
                otp_3.requestFocus();
                Log.d("Success","3::");

            }else if (otp_1.length()==1&&otp_2.length()==1
                    &&otp_3.length()==0&&otp_4.length()==0){
                otp_2.requestFocus();
                Log.d("Success","2::");

            }else if (otp_1.length()==1&&otp_2.length()==0
                    &&otp_3.length()==0&&otp_4.length()==0){
                otp_1.requestFocus();
                Log.d("Success","1::");

            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Please confirm");
            builder.setMessage("Are you want to exit the app?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    OTPVerifyActivity.super.onBackPressed();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        findview();
        Statusbar.darkenStatusBar(this, R.color.colorApp);
        adsimageget();
        session=new SessionManager(getApplicationContext());
        sharpre = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        dialog=new ProgressDialog(this);
        scrollingtext.setSelected(true);
        otp_fab.setOnClickListener(this);
        otp_resend.setOnClickListener(this);
        otp_verify_fab.setOnClickListener(this);



        Log.d("Success", "oncreate::");
        if (bundle!=null){
            email_otp= (String) bundle.get("email_id");
            mobile= String.valueOf(bundle.get("mobile_number"));
            name= String.valueOf(bundle.get("name"));
            password= String.valueOf(bundle.get("password"));
            id= String.valueOf(bundle.get("customer_id"));

        }
        Log.d("email_id","mobile::"+mobile);
        Log.d("email_id","pass::"+password);

        otp_resend.setEnabled(false);
        otp_resend.postDelayed(new Runnable() {
            @Override
            public void run() {
                otp_resend.setEnabled(true);
            }
        }, 30000);
        email_tv.setText(mobile);

        otp_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        otp_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp_1.getText().toString().length()==1&&otp_2.getText().toString().length()==0
                        &&otp_3.getText().toString().length()==0 &&otp_4.getText().toString().length()==0) {
                    otp_2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp_1.getText().toString().length()==1&&otp_2.getText().toString().length()==1
                        &&otp_3.getText().toString().length()==0 &&otp_4.getText().toString().length()==0)  {
                    otp_3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp_1.getText().toString().length()==1&&otp_2.getText().toString().length()==1
                        &&otp_3.getText().toString().length()==1 &&otp_4.getText().toString().length()==0)  {
                    otp_4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp_1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                }
                return false;
            }
        });
        otp_2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otp_2.length()==0) {
                        otp_1.requestFocus();
                    }
                }
                return false;
            }
        });
        otp_3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otp_3.length()==0) {
                        otp_2.requestFocus();
                    }
                }
                return false;
            }
        });
        otp_4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    otp_3.requestFocus();

                }
                return false;
            }
        });
    }

    private void findview() {
        email_in_ly=findViewById(R.id.email_in_ly);
        otp_fab=findViewById(R.id.otp_fab);
        email_tv=findViewById(R.id.email_tv);
        otp_resend=findViewById(R.id.otp_resend);
        otp_verify_fab=findViewById(R.id.otp_verify_fab);
        otp_1=findViewById(R.id.otp_1);
        otp_2=findViewById(R.id.otp_2);
        otp_3=findViewById(R.id.otp_3);
        otp_4=findViewById(R.id.otp_4);
        scrollingtext=findViewById(R.id.scrollingtext);
        reg_card=findViewById(R.id.reg_card1);
        loader_card=findViewById(R.id.loader_card);
        otp_ly=findViewById(R.id.otp_ly);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.otp_fab:
                Log.d("Success", "click::" +otp_fab);
                String one=otp_1.getText().toString();
                String two=otp_2.getText().toString();
                String three=otp_3.getText().toString();
                String four=otp_4.getText().toString();
                String  otp=one + two + three + four;
                Log.d("otp","otp_new::"+otp.toString());
                if (otp!=null && otp.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please enter OTP",Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        getvalue(email_otp,otp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.otp_resend:
                try {
                    String  email_otp_check =  sharpre.getString(KEY_EMAIL,"");
                    if (email_otp_check!=null && email_otp_check!="") {
                        email_otp = sharpre.getString(KEY_EMAIL,"");
                        Log.d("otp","email_otp1ss2121::"+email_otp.toString());
                    }
                    otp_fab.setVisibility(View.GONE);
                    otp_verify_fab.setVisibility(View.VISIBLE);
                    otp_1.getText().clear();
                    otp_2.getText().clear();
                    otp_3.getText().clear();
                    otp_4.getText().clear();
                    getresendvalue(email_otp);
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
                break;
            case R.id.otp_verify_fab:
                String a=otp_1.getText().toString();
                String b=otp_2.getText().toString();
                String c=otp_3.getText().toString();
                String d=otp_4.getText().toString();
                String  otp_resend=a + b + c + d;
                if (otp_resend.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter OTP",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                       String  email_otp_check =  sharpre.getString(KEY_EMAIL,"");
                        if (email_otp_check!=null && email_otp_check!="") {
                            email_otp = sharpre.getString(KEY_EMAIL,"");
                            Log.d("otp","email_otp12121::"+email_otp.toString());
                        }

                        resendotpvarify(email_otp, otp_resend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }

    }

    private void resendotpvarify(String email_otp, String otp) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("email_id",email_otp);
        object.put("otp",otp);
        sendInBackgroundResendOtpVerify(Constants.CUS_LOGIN_RESEND_VERIFY,object);
        Log.d("success","resendotpvarify"+object);

    }
    private void sendInBackgroundResendOtpVerify(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(OTPVerifyActivity.this, url,object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                hideProgress();
                Log.d("message", "resend:: " + message);
                Log.d("VolleyError", "resendotp:: " + error);
                NetworkResponse response=error.networkResponse;
                if (error instanceof ServerError && response!=null){
                    try {
                        String res=new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        JSONObject obj=new JSONObject(res);
                        Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String onResponse(JSONObject response) {
                hideProgress();
                Log.d("Success", "resendotp_responce::"+ response);
                try {
                    if (response.getString("status").equalsIgnoreCase("success")){
                      //  id2=String.valueOf(bundle.get("customerid"));
                        Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent1=new Intent(OTPVerifyActivity.this,LoginActivity.class);
                        String customer="customer";
                        Log.d("Success","register_session::"+customer);
                        Log.d("Success","customer_id::"+id);
                        session.createcustomerLoginSession(id, name, "", password,"", mobile, email_otp, customer, "");
                        Log.d("Success","register_session::"+name);
                        Log.d("Success","register_session::"+mobile);
                        Log.d("Success","password::"+password);
                        startActivity(intent1);

                    }else {
                        Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("VolleyError", "error:: " + e);
                }
                return null;
            }
        });
    }

    private void getresendvalue(String email_otp) throws JSONException {
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
    }


    private void getvalue(String email,String otp) throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        object.put("email_id",email);
        object.put("otp",otp);
        Log.d("Success", "otp::" + otp);
        sendInBackground(Constants.CUS_LOGIN_OTP,object);
    }
    private void sendInBackground(String url, JSONObject object) {

        VolleyUtils.makeJsonObjectRequest(OTPVerifyActivity.this, url,object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                hideProgress();
                NetworkResponse response=error.networkResponse;
                Log.d("message", "message::" + message);
                Log.d("Success", "error::" + error);
                if (error instanceof ServerError && response!=null){
                    try {
                        String res=new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        JSONObject obj = new JSONObject(res);
                        Toast.makeText(OTPVerifyActivity.this,obj.getString("message"),Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.e("excpetion", "error:: " + e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("excpetion", "error2535:: " + e);
                    }
                }
            }

            @Override
            public String onResponse(JSONObject response) {
                hideProgress();
                Log.d("Success", "Responce::" + response);
                try {
                    if (response.getString("status").equalsIgnoreCase("success")){
                      //  id2=response.getString("customerid");
                        Toast.makeText(OTPVerifyActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(OTPVerifyActivity.this,LoginActivity.class);
                        String customer="customer";
                        Log.d("Success","customer_id::");
                        session.createcustomerLoginSession(id, name, "", password,"", mobile, email_otp, customer, "");
                        Log.d("Success","register_session::"+name);
                        Log.d("Success","register_session::"+mobile);
                        Log.d("Success","otp_password::"+password);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("excpetion", "error:: " + e);
                }

                return null;
            }
        });
    }
    private void adsimageget() {
        VolleyUtils.makeJsonObjectRequest(this, Constants.ADS_API, null, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                Log.d("Success", "error_message::" + message);
                Log.d("Success", "error::" + error);
            }
            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success", "Responce_Ads::" + response);
                if (response.getString("status").equalsIgnoreCase("success")) {
                    // JSONArray array = response.getJSONArray("data");

                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);

                        String ad_content=object.getString("ads_data");
                        if (ad_content.contains("jpg") || ad_content.contains("png")) {
                            Log.d("Success", "ads_content_2_conditions::" + ad_content);
                        }else {
                            ArrayList arrayList=new ArrayList();
                            arrayList.add(ad_content);
                            Log.d("Success", "ads_content::" + ad_content);
                            for (int j=0;j<arrayList.size();j++){
                                String text= String.valueOf(arrayList.get(j));
                                scrollingtext.setText(text);
                                scrollingtext.setSelected(true);
                                Log.d("Success", "ads_content_inside::" + arrayList.get(j));
                            }

                        }
                        if (object.getString("ads_data_link").equalsIgnoreCase("null")) {

                        }else {
                            String path = object.getString("ads_data_link");
                            Log.d("Success", "Ads_image_path::" + path);
                        }
                    }
                }
                return null;
            }
        });
    }

    @Override
    public void showProgress() {
        loader_card.setVisibility(View.VISIBLE);
        reg_card.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        loader_card.setVisibility(View.GONE);
        reg_card.setVisibility(View.VISIBLE);
    }
    public void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Log.d("Success","keyboard_test_inside::");
    }

}
