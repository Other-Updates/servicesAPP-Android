package com.f2f.incls.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.f2f.incls.R;
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
import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.f2f.incls.activity.OTPVerifyActivity.otp_fab;
import static com.f2f.incls.activity.OTPVerifyActivity.otp_verify_fab;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, LoadinInterface {
    EditText reg_mobile,reg_password,reg_name,reg_email;
    FloatingActionButton reg_fab;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    SessionManager session;
    ProgressDialog prog;
    Dialog dialogMigrate;
    LinearLayout register_main_ly,loader_ly3;
    TextInputLayout email_ip_ly,phone_in_ly,username_ip_ly,password_in_ly;
    TextView scrollingtext;
    CardView reg_card;
    ArrayList<AdsScrollTextModel> scrollList;
    ArrayList<String> adimageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Statusbar.darkenStatusBar(this,R.color.colorStatusbar);

        session = new SessionManager(getApplicationContext());
        sharpre = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
        findview();
        adsimageget();
       // loader_card.setVisibility(View.GONE);
        reg_card.setVisibility(View.VISIBLE);
        prog=new ProgressDialog(this);
        scrollingtext.setSelected(true);
        reg_fab.setOnClickListener(this);

        register_main_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });

        reg_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>=3 && isValidMail(reg_email.getText().toString())){
                    Log.d("Success","onchange_email::"+s);
                    email_ip_ly.setError(null);
                    reg_email.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!reg_email.getText().toString().isEmpty() && isValidMail(String.valueOf(reg_email))){
                    email_ip_ly.setError(null);
                    Log.d("Success","afterchange_email::"+s);
                }
            }
        });

        reg_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("Success","befforchange_mobile::"+s.length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Success","onchange_mobile_out_side::"+s.length());
                if (s.length()==10){
                    Log.d("Success","onchange_mobile::"+s.length());
                    phone_in_ly.setError(null);
                    reg_mobile.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (!(reg_mobile.getText().toString().isEmpty())
                        && isValidMobile(String.valueOf(reg_mobile))){
                    Log.d("Success","afterchange_mobile::");
                    phone_in_ly.setError(null);
                }*/

            }
        });
        reg_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==3){
                    username_ip_ly.setError(null);
                    reg_name.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!reg_name.getText().toString().isEmpty()){
                    username_ip_ly.setError(null);
                }
            }
        });
        reg_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==3){
                    password_in_ly.setError(null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!reg_password.getText().toString().isEmpty()){
                    password_in_ly.setError(null);
                }
            }
        });

    }

    private void findview() {
        reg_fab=findViewById(R.id.reg_fab);
        reg_mobile=findViewById(R.id.reg_mobile);
        reg_name=findViewById(R.id.reg_name);
        reg_email=findViewById(R.id.reg_email);
        reg_password=findViewById(R.id.reg_password);
        email_ip_ly=findViewById(R.id.email_ip_ly);
        phone_in_ly=findViewById(R.id.phone_in_ly);
        username_ip_ly=findViewById(R.id.username_ip_ly);
        password_in_ly=findViewById(R.id.password_in_ly);
        scrollingtext=findViewById(R.id.scrollingtext);
        reg_card=findViewById(R.id.reg_card);
        loader_ly3=findViewById(R.id.loader_ly3);
        register_main_ly=findViewById(R.id.register_main_ly);

    }
    private boolean isValidMail(String email) {
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

  /*  public static boolean isValidMobile(String phone) {
        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
    }*/

    /*private boolean isValidMobile(String phone) {
        if(!(Pattern.matches("[a-zA-Z]+", phone))) {
            return phone.length() > 6 && phone.length() ==10;
        }
        return false;
    }*/


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.reg_fab) {
            //   registerdata();
            String name_validate = reg_name.getText().toString();
            String email_validate = reg_email.getText().toString();
            String mobile_validate = reg_mobile.getText().toString();
            String pass_validate = reg_password.getText().toString();

            if (name_validate.isEmpty() || email_validate.isEmpty()
                    || mobile_validate.isEmpty() || pass_validate.isEmpty()
                    || !isValidMail(email_validate) || !isValidMobile(mobile_validate) ||!(mobile_validate.length()==10)) {

                Log.d("esle", "if::");
                if (email_validate.isEmpty()) {
                    email_ip_ly.setError("Please enter email ID");
                    reg_email.requestFocus();
                }else if (!isValidMail(email_validate)) {
                    Log.d("Success","email_error::");
                    email_ip_ly.setError("Enter valid email ID");
                    reg_email.requestFocus();
                }
                if (mobile_validate.isEmpty()) {
                    Log.d("Success","emp_mobile_error");
                    phone_in_ly.setError("Please enter mobile number");
                    reg_mobile.requestFocus();
                } else if(!(mobile_validate.length() ==10)) {
                    Log.d("Success","mobile_error::");
                    phone_in_ly.setError("Enter valid mobile number");
                    reg_mobile.requestFocus();
                }else if (!isValidMobile(mobile_validate)){
                    phone_in_ly.setError("Enter valid mobile number");
                    reg_mobile.requestFocus();
                }
                if (name_validate.isEmpty()) {
                    username_ip_ly.setError("Please enter username");
                    reg_name.requestFocus();
                }
                if (pass_validate.isEmpty()) {
                    password_in_ly.setError("Please enter password");
                    reg_password.requestFocus();
                }
            } else {
                Log.d("esle", "else::");


                try {
                    getvalues(name_validate, email_validate, mobile_validate, pass_validate);
                    Log.d("esle", "else_getvalue::");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
    public void getvalues(String name, String email, String mobile, String password) throws JSONException {
        showProgress();
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("mobile_number", mobile);
        object.put("email_id", email);
        object.put("password", password);
        sendInBackground(Constants.REGISTER_URL, object);
        Log.d("Success","getvalue_object::"+object);

    }
    public void sendInBackground(String url, JSONObject loginJsonObject) {
        Log.d("Success:::", "background::");
    /*    try {
            VolleyUtils.makeJsonObjectRequest(RegisterActivity.this, url,loginJsonObject, new VolleyCallback() {
                @Override
                public void onError(String message,VolleyError error) {
                    //  prog.cancel();
                    hideProgress();
                    Log.d("error:::::", "volley_error:::" + error.getMessage());
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                            Log.d("error:","obj:::"+obj);
                            Toast.makeText(RegisterActivity.this,obj.getString("message"),Toast.LENGTH_SHORT).show();
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
*/                 VolleyUtils.makeJsonObjectRequest(RegisterActivity.this, url, loginJsonObject, new VolleyCallback() {
                    @Override
                    public void onError(String message, VolleyError error) {
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
                                Toast.makeText(RegisterActivity.this,obj.getString("message"),Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                    }

                @SuppressLint("RestrictedApi")
                @Override
                public String onResponse(JSONObject response) throws JSONException {
                    hideProgress();
                    Log.d("error:::::", "Register_response:::"+response);
                    String email=reg_email.getText().toString();
                        String mobile=reg_mobile.getText().toString();
                        String name=reg_name.getText().toString();
                       String otp_pincode= "" ;

                     //   String password=null;
                       String id = "";

                        String pass=reg_password.getText().toString();
                        Log.d("Success","password_test_inside_responce::"+pass);
                        Log.d("Success","password_test_inside_responce1::"+email);
                        Log.d("Success","password_test_inside_responce2::"+mobile);
                        Log.d("Success","password_test_inside_responce3::"+name);
                        Log.d("Success","password_test_inside_responce8::"+response);
                        if(response.getString("status").equalsIgnoreCase("Success")){

                            JSONArray array=response.getJSONArray("customer_data");

                            Log.d("Success","fhhf::"+array);
                            for (int i = 0; i<array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                email = object.getString("email_id");
                                mobile = object.getString("mobil_number");
                                name = object.getString("name");
                                pass = object.getString("password");
                                id = object.getString("id");
                                //   otp_pincode = object.getString("otp_pincode");
                                editor.putString(KEY_USER_ID, id);
                                editor.commit();
                            }
                                Log.d("email", "customer_id::" + id);
                                Intent intent = new Intent(RegisterActivity.this, CustomerDashBoard.class);
                          /*  otp_fab.setVisibility(View.VISIBLE);
                            otp_verify_fab.setVisibility(View.GONE);*/
                                intent.putExtra("email_id", email);
                                intent.putExtra("mobile_number", mobile);
                                intent.putExtra("name", name);
                                intent.putExtra("password", pass);
                                intent.putExtra("customer_id", id);
                                //  intent.putExtra("otp_pincode",otp_pincode);
                                startActivity(intent);
                             }else {

                            Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                         }
                  /*  } catch (Exception e) {
//                                    dialogMigrate.dismiss();
                        Log.e("excpetion", "error:: " + e);
                        e.printStackTrace();
                    }*/
                    return null;
                }
            });

    }
    /*private void adsimageget() {
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
                    adimageList=new ArrayList<>();
                    scrollList=new ArrayList<>();

                    JSONArray array = response.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        JSONArray details_array = object.getJSONArray("ads_details");
                        for (int j = 0; j < details_array.length(); j++) {
                            JSONObject obj1 = details_array.getJSONObject(j);
                            String image = obj1.getString("ads_data_link");

                            if (image.contains("jpg") || image.contains("png")) {
                                Log.d("Success", "ads_content_2_conditions::" + image);
                            } else {
                                String text = obj1.getString("ads_data");
                                Log.d("Success", "scrolltext_test::"+text);
                                scrollingtext.setText(text);
                                scrollingtext.setSelected(true);
                            }
                            if (obj1.getString("ads_data_link").equalsIgnoreCase("null")) {

                            } else {
                                String path = obj1.getString("ads_data_link");
                                Log.d("Success", "Ads_image_path::" + path);
                            }

                        }
                    }
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

    @Override
    public void showProgress() {
        loader_ly3.setVisibility(View.VISIBLE);
        reg_card.setVisibility(View.VISIBLE);
        Log.d("Success", "Register_Show");
    }

    @Override
    public void hideProgress() {
        loader_ly3.setVisibility(View.GONE);
        reg_card.setVisibility(View.VISIBLE);
        Log.d("Success", "Register_hide");
    }
    public void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Log.d("Success","keyboard_test_inside::");
    }

}

