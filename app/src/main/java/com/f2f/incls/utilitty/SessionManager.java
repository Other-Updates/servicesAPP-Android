package com.f2f.incls.utilitty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.f2f.incls.activity.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharepre;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "Incredible_Solution";
    public static final String IS_LOGIN = "ISLogIn";

    public static final String KEY_EMP_ID = "emp_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_STORE = "store_name";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_PROFILE = "user_profile";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
  //  public static final String KEY_LOGINSTATUS = "login_status";
 //  public static final String KEY_LOGINDATE = "logindate";
    public static final String KEY_TEST = "password";
    public static final String KEY_SIGNATURE = "signature";
    public static final String KEY_PRODUCTS = "products";
    public static final String KEY_EMP_CODE = "emp_code";
    public static final String KEY_ROLE = "role";
    public static final String KEY_TYPE = "type";
    public static final String KEY_CUS_PRO_PATH = "customer_profile_path";

    public static final String KEY_INV_USER = "inv_user";
    public static final String KEY_INV_NO = "inv_no";
    public static final String KEY_TK_NO = "tk_no";
    public static final String KEY_WARRANTY = "warranty";
    public static final String KEY_ATN_ID = "atn_id";
    public static final String KEY_ATN_NAME = "atn_name";
    public static final String KEY_ISSUE = "issue";
    public static final String KEY_SERVICE_TYPE = "service_type";

    public static final String KEY_YOUTUBE_ID = "youtube_id";
    public static final String KEY_LINKDATA = "link";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_THUMBNAIL = "thumbnail";


    public SessionManager(Context context) {
        this.context = context;
        sharepre = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharepre.edit();
    }

    public void createcustomerLoginSession(String id, String name, String store, String password, String new_password, String mobile, String email, String customer, String profile) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_STORE, store);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        //  editor.putString(KEY_NEW_PASSWORD,new_password);
        editor.putString(KEY_TYPE, customer);
        editor.putString(KEY_USER_PROFILE, profile);
        editor.commit();
    }

    public void createempLoginSession(String id, String name, String user_name, String password, String mobile_number, String email, String signature, String emp_code, String role, String employee, String emp_profile){
        Log.d("Success","session_test_profile::"+emp_profile);
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_ID,id);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_USER_NAME,user_name);
        editor.putString(KEY_MOBILE,mobile_number);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_SIGNATURE,signature);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_EMP_CODE,emp_code);
        editor.putString(KEY_ROLE,role);
        editor.putString(KEY_TYPE,employee);
        editor.putString(KEY_USER_PROFILE,emp_profile);
      //  editor.putString(KEY_LOGINDATE,login_date);
        editor.commit();
    }





    public void createService(String id, String inv_no, String tk_no, String warranty,
                              String atn_id, String atn_name, String issue, String service_type) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_INV_USER, id);
        editor.putString(KEY_INV_NO, inv_no);
        editor.putString(KEY_TK_NO, tk_no);
        editor.putString(KEY_WARRANTY, warranty);
        editor.putString(KEY_ATN_ID, atn_id);
        editor.putString(KEY_ATN_NAME, atn_name);
        editor.putString(KEY_ISSUE, issue);
        editor.putString(KEY_SERVICE_TYPE, service_type);
    }

    /*public void createYoutube(String id, String description, String link_data,String customer){
        editor.putString(KEY_USER_ID,id);
        editor.putString(KEY_LINKDATA,link_data);
        editor.putString(KEY_DESCRIPTION,description);
    //    editor.putString(KEY_THUMBNAIL,thumbnail);
    //    editor.putString(KEY_TYPE,customer);
        editor.commit();
    }
*/
    public void createYoutube(String cus_id, String link_data, String description) {
        editor.putString(KEY_YOUTUBE_ID, cus_id);
        editor.putString(KEY_LINKDATA, link_data);
        editor.putString(KEY_DESCRIPTION, description);
        //    editor.putString(KEY_THUMBNAIL,thumbnail);
        //    editor.putString(KEY_TYPE,customer);
        editor.commit();

    }

    public HashMap<String, String> getYoutube() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(KEY_LINKDATA, sharepre.getString(KEY_LINKDATA, null));
        hashMap.put(KEY_DESCRIPTION, sharepre.getString(KEY_DESCRIPTION, null));
        //      hashMap.put(KEY_THUMBNAIL,sharepre.getString(KEY_THUMBNAIL,null));
        hashMap.put(KEY_USER_ID, sharepre.getString(KEY_USER_ID, null));
        //    hashMap.put(KEY_TYPE,sharepre.getString(KEY_TYPE,null));
        return hashMap;

    }

    public HashMap<String, String> getService() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(KEY_INV_USER, sharepre.getString(KEY_USER_ID, null));
        hashMap.put(KEY_INV_NO, sharepre.getString(KEY_INV_NO, null));
        hashMap.put(KEY_TK_NO, sharepre.getString(KEY_TK_NO, null));
        hashMap.put(KEY_WARRANTY, sharepre.getString(KEY_WARRANTY, null));
        hashMap.put(KEY_ATN_ID, sharepre.getString(KEY_ATN_ID, null));
        hashMap.put(KEY_ATN_NAME, sharepre.getString(KEY_ATN_NAME, null));
        hashMap.put(KEY_ISSUE, sharepre.getString(KEY_ISSUE, null));
        hashMap.put(KEY_SERVICE_TYPE, sharepre.getString(KEY_SERVICE_TYPE, null));
        return hashMap;
    }

    public HashMap<String, String> getcustomerdetails() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(KEY_USER_ID, sharepre.getString(KEY_USER_ID, null));
        hashMap.put(KEY_USER_NAME, sharepre.getString(KEY_USER_NAME, null));
        hashMap.put(KEY_STORE, sharepre.getString(KEY_STORE, null));
        hashMap.put(KEY_MOBILE, sharepre.getString(KEY_MOBILE, null));
        hashMap.put(KEY_EMAIL, sharepre.getString(KEY_EMAIL, null));
        hashMap.put(KEY_PASSWORD, sharepre.getString(KEY_PASSWORD, null));
        //  hashMap.put(KEY_NEW_PASSWORD,sharepre.getString(KEY_NEW_PASSWORD,null));
        hashMap.put(KEY_TYPE, sharepre.getString(KEY_TYPE, null));
        hashMap.put(KEY_USER_PROFILE, sharepre.getString(KEY_USER_PROFILE, null));
        return hashMap;
    }


    public HashMap<String, String> getempdetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USER_ID, sharepre.getString(KEY_USER_ID, null));
        user.put(KEY_NAME, sharepre.getString(KEY_NAME, null));
        user.put(KEY_USER_NAME, sharepre.getString(KEY_USER_NAME, null));
        user.put(KEY_MOBILE, sharepre.getString(KEY_MOBILE, null));
        user.put(KEY_EMAIL, sharepre.getString(KEY_EMAIL, null));
        user.put(KEY_SIGNATURE, sharepre.getString(KEY_SIGNATURE, null));
        user.put(KEY_PASSWORD, sharepre.getString(KEY_PASSWORD, null));
        user.put(KEY_EMP_CODE, sharepre.getString(KEY_EMP_CODE, null));
        user.put(KEY_TYPE, sharepre.getString(KEY_TYPE, null));
     //   user.put(KEY_LOGINDATE,sharepre.getString(KEY_LOGINDATE,null));
        user.put(KEY_USER_PROFILE, sharepre.getString(KEY_USER_PROFILE, null));
        Log.d("Success", "emp_session_test_get::" + user.get(KEY_USER_PROFILE));
        return user;
    }

    public boolean checkLogin() {
        // Check login status
        if (!this.ISLogIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);

            return true;
        }
        return false;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean ISLogIn() {
        sharepre.getBoolean(IS_LOGIN, false);
        return true;
    }



}




