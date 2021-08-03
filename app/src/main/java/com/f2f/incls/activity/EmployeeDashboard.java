package com.f2f.incls.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import pl.droidsonroids.gif.GifImageView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver.PendingResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.f2f.incls.R;
import com.f2f.incls.adapter.AdViewPagerAdapter;
import com.f2f.incls.fragment.EmpServiceFragment;
import com.f2f.incls.fragment.EmployeeHomeFragment;
import com.f2f.incls.fragment.EmployeeProfileFragment;
import com.f2f.incls.fragment.TodayTaskFragment;
import com.f2f.incls.model.AdsScrollTextModel;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.Statusbar;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;

//import static com.f2f.incls.utilitty.SessionManager.KEY_LOGINSTATUS;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class EmployeeDashboard extends AppCompatActivity implements LoadinInterface {
    private static final String TAG = "EmployeeDashboard";
    Toolbar emp_dashboard_tool, emp_edit_service_tool, emp_today_tool, emp_profile_tool, emp_service_tool;
    ImageView emp_dash_power_btn, emp_today_power_btn,
            emp_edit_service_power_btn, emp_profil_power_btn, emp_service_dash, emp_today_task_dash, emp_service_power_btn;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    BottomNavigationView bottom_navigation;
    LinearLayout employee_options, emp_dash_main;
    RelativeLayout ads_img_ly;
    FrameLayout frame_container_emp;
    ImageView ad_img_close;
    GifImageView loader_img;
    private int menu_id;
    Fragment fragment;
    TextView scrollingtext;
    ArrayList<String> adimageList;
    ArrayList<AdsScrollTextModel> scrollList;
    ViewPager ad_view_pager;
    int currentPage = 0;
    Timer timer;
    int close_value;
    Bundle bundle;
    String emp_id;
    String emp_type;
    String callAutoLogout;
    private Runnable runnableLamoveStatus;
    private Handler handler = new Handler();
    Calendar Calendar;
    private Context con;
    TimePicker timePicker;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        findview();
        Statusbar.darkenStatusBar(this, R.color.colorApp);
        adsimageget();
        session = new SessionManager(EmployeeDashboard.this);
        sharpre = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
       /* SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
*/


        HashMap<String, String> user = session.getempdetails();
        emp_id = user.get(SessionManager.KEY_USER_ID);
        emp_type = user.get(SessionManager.KEY_TYPE);
        Log.d("Success", "emp_id::" + emp_id);
        Log.d("Success", "emp_type::" + emp_type);
        bottom_navigation.setOnNavigationItemSelectedListener(selectedId);
        bottom_navigation.getMenu().findItem(R.id.emp_home).setChecked(true);
        //    loginAlarm = new LoginAlarmReceiver();
        //   loginAlarm.setAlarm(EmployeeDashboard.this);
        fragment = new EmployeeHomeFragment();
        //   Toast.makeText(getApplicationContext(),"Development process on going,Designs using for static data",Toast.LENGTH_LONG).show();
        loadfragment(fragment);


        //    sharpre.getString("login_status", null);
        // will delete key name
        // will delete key email


        if (close_value == 1) {
            ads_img_ly.setVisibility(View.GONE);
            Log.d("Success", "true::");
        } else {
            ads_img_ly.setVisibility(View.VISIBLE);
            Log.d("Success", "else::");
        }

        ad_img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_value = 1;
                ads_img_ly.setVisibility(View.GONE);
            }
        });


        emp_dash_power_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EmployeeDashboard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(emp_id, emp_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No", null)
                        .create()
                        .show();
            }
        });
        emp_today_power_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EmployeeDashboard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(emp_id, emp_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No", null)
                        .create()
                        .show();
            }
        });
        emp_profil_power_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EmployeeDashboard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(emp_id, emp_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No", null)
                        .create()
                        .show();
            }
        });
        emp_edit_service_power_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EmployeeDashboard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(emp_id, emp_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No", null)
                        .create()
                        .show();
            }
        });
        emp_service_dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation.getMenu().findItem(R.id.emp_service).setChecked(true);
                fragment = new EmpServiceFragment();
                employee_options.setVisibility(View.GONE);
                emp_dashboard_tool.setVisibility(View.GONE);
                emp_today_tool.setVisibility(View.GONE);
                emp_profile_tool.setVisibility(View.GONE);
                emp_edit_service_tool.setVisibility(View.GONE);
                emp_service_tool.setVisibility(View.VISIBLE);
                //    Toast.makeText(getApplicationContext(),"Development process on going,Designs using for static data",Toast.LENGTH_LONG).show();
                loadfragment(fragment);
            }
        });
        emp_today_task_dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation.getMenu().findItem(R.id.emp_today_task).setChecked(true);
                fragment = new TodayTaskFragment();
                employee_options.setVisibility(View.GONE);
                emp_dashboard_tool.setVisibility(View.GONE);
                emp_edit_service_tool.setVisibility(View.GONE);
                emp_today_tool.setVisibility(View.VISIBLE);
                emp_service_tool.setVisibility(View.GONE);
                emp_profile_tool.setVisibility(View.GONE);
                //     Toast.makeText(getApplicationContext(),"Development process on going,Designs using for static data",Toast.LENGTH_LONG).show();
                loadfragment(fragment);
            }
        });
        emp_service_power_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EmployeeDashboard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(emp_id, emp_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No", null)
                        .create()
                        .show();
            }
        });




       /* private void callAutoLogout(String emp_id,String emp_type) {
            Intent alaramIntent = new Intent(EmployeeDashboard.this, LoginActivity.class);
            alaramIntent.setAction("LogOutAction");
            Log.e("MethodCall", "AutoLogOutCall");
            alaramIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 21);
            calendar.set(Calendar.SECOND, 0);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);


            Log.e("Logout", "Auto Logout set at..!" + calendar.getTime());
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }*/

    }


    private void logout(String emp_id, String emp_type) {
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", emp_id);
            object.put("user_type", "1");
            Log.d("Message", "Logout_Request::" + object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyUtils.makeJsonObjectRequest(getApplicationContext(), Constants.EMP_LOGIN_OUT, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Message", "Emp_Logout_Responce::" + response);
                //          editor.putString(KEY_LOGINSTATUS,"0");
                return null;
            }
        });


    }




  /*  private void CallAutoLogOut(String emp_id, String emp_type) {

        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
                new Intent("myactionalarm"),PendingIntent.FLAG_NO_CREATE) != null);

        Intent alaramIntent = new Intent(EmployeeDashboard.this,LoginActivity.class);
        alaramIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE,48);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        if (alarmUp)
        {
            Log.e("alaram", "Alarm is already active");
        }else {
            Log.e("alaram", "Alarm is set..!"+calendar.getTime());
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
// alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 2*60*1000, pendingIntent);
          *//*  JSONObject object = new JSONObject();
            try {
                object.put("user_id", emp_id);
                object.put("user_type", "1");
                Log.d("Message", "Logout_Request::" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            VolleyUtils.makeJsonObjectRequest(getApplicationContext(), Constants.EMP_LOGIN_OUT, object, new VolleyCallback() {
                @Override
                public void onError(String message, VolleyError error) {

                }

                @Override
                public String onResponse(JSONObject response) throws JSONException {
                    Log.d("Message", "Emp_Logout_Responce::" + response);
                    return null;
                }
            });
*//*
        }

    }


    public void onReceive(Context context, Intent intent) {
        this.con = context;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Log.e("alaram", "just called reciver");
        if (intent.getAction() != null) {
            if (intent.getAction().equals("myactionalarm")) {
                Log.e("alaram", "onReceive: ----->" + intent);

                logout(emp_id,emp_type);


            }
        }
    }
*/


/*private void CallAutoLogout() {
    Intent alaramIntent = new Intent(EmployeeDashboard.this, BootCompletedIntentReceiver.class);
    alaramIntent.setAction("LogOutAction");
    Log.e("MethodCall", "AutoLogOutCall");
    alaramIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, 15);
    calendar.set(Calendar.MINUTE, 07);
    calendar.set(Calendar.SECOND, 0);
    AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
    Log.e("Logout", "Auto Logout set at..!" + calendar.getTime());
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    JSONObject object = new JSONObject();
    try {
        object.put("user_id", emp_id);
        object.put("user_type", "1");
        Log.d("Message", "Logout_Request::" + object);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    VolleyUtils.makeJsonObjectRequest(getApplicationContext(), Constants.EMP_LOGIN_OUT, object, new VolleyCallback() {
        @Override
        public void onError(String message, VolleyError error) {

        }

        @Override
        public String onResponse(JSONObject response) throws JSONException {
            Log.d("Message", "Emp_Logout_Responce::" + response);
            return null;
        }
    });

}*/
   /* loginAlarm = new void LoginAlarmReceiver();
    loginAlarm.setAlarm(EmployeeDashboard.this);
*/
  /* private void loginAlarm(String emp_id,String emp_type){
    //   loginAlarm.setAlarm(EmployeeDashboard.this);
       JSONObject object = new JSONObject();
       try {
           object.put("user_id", emp_id);
           object.put("user_type", "1");
           Log.d("Message", "Logout_Request::" + object);
       } catch (JSONException e) {
           e.printStackTrace();
       }
       VolleyUtils.makeJsonObjectRequest(getApplicationContext(), Constants.EMP_LOGIN_OUT, object, new VolleyCallback() {
           @Override
           public void onError(String message, VolleyError error) {

           }

           @Override
           public String onResponse(JSONObject response) throws JSONException {
               Log.d("Message", "Emp_Logout_Responce::" + response);
               return null;
           }
       });

   }*/

/*    private void callAutoLogout(String emp_id,String emp_type) {
        Intent alaramIntent = new Intent(EmployeeDashboard.this, BootCompletedIntentReceiver.class);
        alaramIntent.setAction("LogOutAction");
        Log.e("MethodCall","AutoLogOutCall");
        alaramIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 44);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);

        Log.e("Logout", "Auto Logout set at..!" + calendar.getTime());
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }*/

    /*private void setCallAutoLogout(String emp_id, String emp_type) {

        Intent alaramIntent = new Intent(EmployeeDashboard.this, LoginActivity.class);
        alaramIntent.setAction("LogOutAction");
        Log.e("MethodCall", "AutoLogOutCall");
        alaramIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 8);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);


        Log.e("Logout", "Auto Logout set at..!" + calendar.getTime());
        alarsponce::" + response);
                return null;mManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", emp_id);
            object.put("user_type", "1");
            Log.d("Message", "Logout_Request::" + object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyUtils.makeJsonObjectRequest(getApplicationContext(), Constants.EMP_LOGIN_OUT, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Message", "Emp_Logout_Re
            }
        });
    }*/


    public void adsimageget() {
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
                    String image;
                    scrollList = new ArrayList<>();
                    adimageList = new ArrayList<>();
                    JSONArray array = response.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);
                        JSONArray details_array = object.getJSONArray("ads_details");
                        for (int j = 1; j < details_array.length(); j++) {
                            JSONObject obj1 = details_array.getJSONObject(j);
                            image = obj1.getString("ads_data_link");

                            if (image.contains("jpeg") || image.contains("jpg") || image.contains("png")) {
                                Log.d("Success", "ads_content_2_conditions::" + image);
                            } else {
                                String text = obj1.getString("ads_data");
                                Log.d("Message", "ScrollText_test::" + text);
                                AdsScrollTextModel model = new AdsScrollTextModel();
                                model.setScrolltext(text);
                                scrollList.add(model);
                            }
                            if (obj1.getString("ads_data_link").equalsIgnoreCase("")) {

                            } else {
                                String path = obj1.getString("ads_data_link");
                                Log.d("Success", "Ads_image_path::" + path);
                                adimageList.add(path);
                            }
                        }
                        for (int j = 0; j < 1; j++) {
                            JSONObject obj1 = details_array.getJSONObject(j);
                            String text = obj1.getString("ads_data");
                            Log.d("Message", "ScrollText_test::" + text);
                            AdsScrollTextModel model = new AdsScrollTextModel();
                            model.setScrolltext(text);
                            scrollList.add(model);


                        }

                    }
                    Log.d("Success", "ImageArraySize::" + adimageList.size());
                    scrollmethod(scrollList);
                    AdViewPagerAdapter adapter = new AdViewPagerAdapter(EmployeeDashboard.this, adimageList);
                    ad_view_pager.setAdapter(adapter);
                    setAutopager();
                }
                return null;
            }
        });
    }

    private void scrollmethod(ArrayList<AdsScrollTextModel> arrayList) {

        Log.d("Success", "ads_arraylist_size::" + arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            AdsScrollTextModel model1 = arrayList.get(i);
            String path = model1.getScrolltext();
            int checklength = 150;
            if (path.length() < checklength) {
                int total = checklength - path.length();
                for (int j = 0; j < total; j++) {
                    path = path + " ";

                }
            }
            scrollingtext.setText(path);
            scrollingtext.setMarqueeRepeatLimit(-1);
            scrollingtext.setSelected(true);
            Log.d("Success", "scroll_text_test::" + path);
        }
    }

    private void setAutopager() {

      /*Runnable  runnableLamoveStatus = new Runnable() {
            @Override
            public void run() {
                Log.d("Success","ads_test_1::"+adimageList.size());
                ad_view_pager.setCurrentItem(currentPage, true);
                if(currentPage == adimageList.size()) {
                    Log.d("Success","ads_test_2::"+currentPage);
                    currentPage = 0;
                    Log.d("Success","ads_test_3::"+currentPage);
                }
                else {
                    ++currentPage ;
                    Log.d("Success","ads_test_4::"+currentPage);
                }

                handler.postDelayed(this, 5000);


            }
        };*/


        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.d("Success", "ads_test_1::" + adimageList.size());
                ad_view_pager.setCurrentItem(currentPage, true);
                //      ad_view_pager.setCurrentItem(ad_view_pager.getCurrentItem()+1);
                if (currentPage == adimageList.size()) {
                    Log.d("Success", "ads_test_2::" + currentPage);
                    currentPage = 0;
                    Log.d("Success", "ads_test_3::" + currentPage);
                } else {
                    ++currentPage;
                    ha.postDelayed(this, 10000);
                    Log.d("Success", "ads_test_4::" + currentPage);
                }


                // ha.postDelayed(this, 5000);
            }
        }, 10000);


        //setAutopager();
       /*final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run()
            {
                ad_view_pager.setCurrentItem(currentPage, true);
                if(currentPage == adimageList.size()) {
                    currentPage = 0;
                }
                else {
                    ++currentPage ;
                }
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 4500);*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedId
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            menu_id = menuItem.getItemId();
            for (int i = 0; i < bottom_navigation.getMenu().size(); i++) {
                MenuItem item = bottom_navigation.getMenu().getItem(i);
                boolean isChecked = menuItem.getItemId() == item.getItemId();
                menuItem.setChecked(isChecked);
            }
            switch (menu_id) {
                case R.id.emp_service:
                    if (close_value == 1) {
                        ads_img_ly.setVisibility(View.GONE);
                        Log.d("Success", "true::");
                    } else {
                        ads_img_ly.setVisibility(View.VISIBLE);
                        Log.d("Success", "else::");
                    }
                    fragment = new EmpServiceFragment();
                    employee_options.setVisibility(View.GONE);
                    emp_dashboard_tool.setVisibility(View.GONE);
                    emp_today_tool.setVisibility(View.GONE);
                    emp_profile_tool.setVisibility(View.GONE);
                    emp_edit_service_tool.setVisibility(View.GONE);
                    emp_service_tool.setVisibility(View.VISIBLE);
                    //    Toast.makeText(getApplicationContext(),"Development process on going,Designs using for static data",Toast.LENGTH_LONG).show();
                    loadfragment(fragment);
                    break;
                case R.id.emp_today_task:
                    if (close_value == 1) {
                        ads_img_ly.setVisibility(View.GONE);
                        Log.d("Success", "true::");
                    } else {
                        ads_img_ly.setVisibility(View.VISIBLE);
                        Log.d("Success", "else::");
                    }
                    fragment = new TodayTaskFragment();
                    employee_options.setVisibility(View.GONE);
                    emp_dashboard_tool.setVisibility(View.GONE);
                    emp_edit_service_tool.setVisibility(View.GONE);
                    emp_today_tool.setVisibility(View.VISIBLE);
                    emp_profile_tool.setVisibility(View.GONE);
                    emp_service_tool.setVisibility(View.GONE);
                    //    Toast.makeText(getApplicationContext(),"Development process on going,Designs using for static data",Toast.LENGTH_LONG).show();
                    loadfragment(fragment);
                    break;
                case R.id.emp_home:
                    if (close_value == 1) {
                        ads_img_ly.setVisibility(View.GONE);
                        Log.d("Success", "true::");
                    } else {
                        ads_img_ly.setVisibility(View.VISIBLE);
                        Log.d("Success", "else::");
                    }
                    fragment = new EmployeeHomeFragment();
                    employee_options.setVisibility(View.VISIBLE);
                    emp_dashboard_tool.setVisibility(View.VISIBLE);
                    emp_edit_service_tool.setVisibility(View.GONE);
                    emp_today_tool.setVisibility(View.GONE);
                    emp_profile_tool.setVisibility(View.GONE);
                    emp_service_tool.setVisibility(View.GONE);
                    //    Toast.makeText(getApplicationContext(),"Development process on going,Designs using for static data",Toast.LENGTH_LONG).show();
                    loadfragment(fragment);
                    break;
                case R.id.emp_profile:
                    ads_img_ly.setVisibility(View.GONE);
                    fragment = new EmployeeProfileFragment();
                    employee_options.setVisibility(View.GONE);
                    emp_dashboard_tool.setVisibility(View.GONE);
                    emp_edit_service_tool.setVisibility(View.GONE);
                    emp_today_tool.setVisibility(View.GONE);
                    emp_service_tool.setVisibility(View.GONE);
                    emp_profile_tool.setVisibility(View.VISIBLE);
                    loadfragment(fragment);
                    break;
            }
            return false;
        }
    };

    private void loadfragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().remove(fragment);
        transaction.replace(R.id.frame_container_emp, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void findview() {
        emp_dash_main = findViewById(R.id.emp_dash_main);
        emp_dash_power_btn = findViewById(R.id.emp_dash_power_btn);
        emp_today_power_btn = findViewById(R.id.emp_today_power_btn);
        emp_edit_service_power_btn = findViewById(R.id.emp_edit_service_power_btn);
        emp_profil_power_btn = findViewById(R.id.emp_profil_power_btn);
        emp_service_dash = findViewById(R.id.emp_service_dash);
        emp_today_task_dash = findViewById(R.id.emp_today_task_dash);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        employee_options = findViewById(R.id.employee_options);
        ad_view_pager = findViewById(R.id.ad_view_pager);
        emp_dashboard_tool = findViewById(R.id.emp_dashboard_tool);
        emp_edit_service_tool = findViewById(R.id.emp_edit_service_tool);
        emp_today_tool = findViewById(R.id.emp_today_tool);
        emp_profile_tool = findViewById(R.id.emp_profile_tool);
        ads_img_ly = findViewById(R.id.ads_img_ly);
        ad_img_close = findViewById(R.id.ad_img_close);
        emp_service_tool = findViewById(R.id.emp_service_tool);
        emp_service_power_btn = findViewById(R.id.emp_service_power_btn);
        loader_img = findViewById(R.id.loader_img);
        frame_container_emp = findViewById(R.id.frame_container_emp);
        scrollingtext = findViewById(R.id.scrollingtext);
    }

    public void empservicelist() {
        employee_options.setVisibility(View.GONE);
        emp_dashboard_tool.setVisibility(View.GONE);
        emp_today_tool.setVisibility(View.GONE);
        emp_profile_tool.setVisibility(View.GONE);
        emp_edit_service_tool.setVisibility(View.VISIBLE);
        emp_service_tool.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        int id = 0;
        String home = "home_id";

        if (menu_id == 0) {
            Log.d("Success", "equal::");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Log.d("Success", "home_this::");
            builder.setTitle("Please confirm");
            builder.setMessage("Are you want to exit the app?");
            builder.setCancelable(true);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EmployeeDashboard.super.finishAffinity();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            switch (menu_id) {
                case R.id.emp_service:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you want to exit the app?");
                    builder.setCancelable(true);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EmployeeDashboard.super.finishAffinity();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    break;
                case R.id.emp_today_task:
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you want to exit the app?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EmployeeDashboard.super.finishAffinity();

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                    break;
                case R.id.emp_home:
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you want to exit the app?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EmployeeDashboard.super.finishAffinity();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                    break;
                case R.id.emp_profile:
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you want to exit the app?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EmployeeDashboard.super.finishAffinity();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                    break;
            }
        }
    }


    @Override
    public void showProgress() {
        findViewById(R.id.frame_container_emp).setVisibility(View.GONE);
        findViewById(R.id.loader_ly).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        findViewById(R.id.frame_container_emp).setVisibility(View.VISIBLE);
        findViewById(R.id.loader_ly).setVisibility(View.GONE);
    }

    public void empservicetool() {
        bottom_navigation.getMenu().findItem(R.id.emp_service).setChecked(true);
        employee_options.setVisibility(View.GONE);
        emp_dashboard_tool.setVisibility(View.GONE);
        emp_today_tool.setVisibility(View.GONE);
        emp_profile_tool.setVisibility(View.GONE);
        emp_edit_service_tool.setVisibility(View.GONE);
        emp_service_tool.setVisibility(View.VISIBLE);
    }

    public void hideads() {
        ads_img_ly.setVisibility(View.GONE);
    }


  /*  @Override
    protected void onStart() {
        super.onStart();
        // LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e(TAG, "OnStart () &&& Starting timer");
    }*/
}
  /*  @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e(TAG, "User interacting with screen");
    }
*//*

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "onResume()");
    }

   // *
   //  * Performing idle time logout

   *//* @Override
    public void doLogout() {
        // write your stuff here
        logout(emp_id,emp_type);
        session.logoutUser();
        finish();
    }*//*
}
*/