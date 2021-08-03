package com.f2f.incls.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.VolleyError;
import com.f2f.incls.R;
import com.f2f.incls.adapter.AdViewPagerAdapter;
import com.f2f.incls.fragment.AddLeadsFragment;
import com.f2f.incls.fragment.CustomerEditServiceFragment;
import com.f2f.incls.fragment.CustomeryoutubeFragment;
import com.f2f.incls.fragment.CustomerHomeFragment;
import com.f2f.incls.fragment.CustomerLeadsFragment;
import com.f2f.incls.fragment.CustomerProfileFragment;
import com.f2f.incls.fragment.CustomerServiceFragment;
import com.f2f.incls.fragment.CustomerWarrantyFragment;
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
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class CustomerDashBoard extends AppCompatActivity implements LoadinInterface  {

    Fragment fragment;
    ImageView customer_leads, customer_service,ad_img_close,customer_edit_service_power_btn,
            add_leads_power,add_leads,  serice_power,raise_ticket,lead_power,edit_power
            ,dash_power_btn,profile_power_btn,paid_service_power,apps_power,warranty_power_btn;
    RelativeLayout ads_img_ly;
    Toolbar dashboard_tool,leads_list_tool,add_leads_tool,service_tool,
            warranty_tool,edit_tool,profile_tool,paid_service_tool,app_tool,cus_edit_service_tool;
    LinearLayout customer_options,loader_ly,total_view_ly,cus_dash_ly;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    TextView add_leads_tv,raise_ticket_tv,scrollingtext,scrolltext;
    ArrayList<String> adimageList;
    ViewPager ad_view_pager;
    int close_value;
    Integer[] imageId = {R.drawable.camera, R.drawable.camera1, R.drawable.camera2, R.drawable.camera_logo};
    String[] imagesName = {"image1","image2","image3","image4"};
    int currentPage = 0;
    int numberpage=0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    BottomNavigationView bottom_navigation;
    private int menu_id;
    String customer_id,customer_type;
    ArrayList<AdsScrollTextModel> arrayList;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5*1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        findview();
        Statusbar.darkenStatusBar(this, R.color.colorApp);
        adsimageget();
        session=new SessionManager(CustomerDashBoard.this);
        sharpre =this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();




        scrolltext=(TextView)findViewById(R.id.scrollingtext);
        scrolltext.setSelected(true);


        HashMap<String, String> user = session.getcustomerdetails();
         customer_id = user.get(SessionManager.KEY_USER_ID);
         customer_type=user.get(SessionManager.KEY_TYPE);
        Log.d("Success","cus_ser_id::"+customer_id);
        Log.d("Success","cus_ser_type::"+customer_type);

        bottom_navigation.setOnNavigationItemSelectedListener(selectedId);
        bottom_navigation.getMenu().findItem(R.id.home_id).setChecked(true);
        fragment = new CustomerHomeFragment();
        loadfragment(fragment);
        if (close_value==1){
            ads_img_ly.setVisibility(View.GONE);
        }else {
            ads_img_ly.setVisibility(View.VISIBLE);
        }

        ad_img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_value=1;
                ads_img_ly.setVisibility(View.GONE);
            }
        });

        add_leads_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CustomerDashBoard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(customer_id,customer_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No",null)
                        .create()
                        .show();
            }
        });

        dash_power_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CustomerDashBoard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(customer_id,customer_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No",null)
                        .create()
                        .show();
            }
        });


        serice_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CustomerDashBoard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(customer_id,customer_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No",null)
                        .create()
                        .show();
            }
        });

        lead_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CustomerDashBoard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(customer_id,customer_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No",null)
                        .create()
                        .show();
            }
        });

        edit_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CustomerDashBoard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(customer_id,customer_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No",null)
                        .create()
                        .show();
            }
        });
        profile_power_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CustomerDashBoard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(customer_id,customer_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No",null)
                        .create()
                        .show();
            }
        });

       /* raise_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new CustomerWarrantyFragment();
                loadfragment(fragment);
                warranty_tool.setVisibility(View.VISIBLE);
                fragment = new CustomerLeadsFragment();
                dashboard_tool.setVisibility(View.GONE);
                customer_options.setVisibility(View.GONE);
                service_tool.setVisibility(View.GONE);
                leads_list_tool.setVisibility(View.GONE);
                add_leads_tool.setVisibility(View.GONE);
                edit_tool.setVisibility(View.GONE);
                profile_tool.setVisibility(View.GONE);
                paid_service_tool.setVisibility(View.GONE);
                app_tool.setVisibility(View.GONE);
                }
        });*/


        paid_service_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CustomerDashBoard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(customer_id,customer_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No",null)
                        .create()
                        .show();
            }
        });
        apps_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CustomerDashBoard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(customer_id,customer_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No",null)
                        .create()
                        .show();
            }
        });
        warranty_power_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CustomerDashBoard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(customer_id,customer_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No",null)
                        .create()
                        .show();
            }
        });

        customer_edit_service_power_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CustomerDashBoard.this)
                        .setCancelable(true)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout(customer_id,customer_type);
                                session.logoutUser();
                                finishAffinity();

                            }
                        }).setNegativeButton("No",null)
                        .create()
                        .show();
            }
        });


        customer_leads.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                bottom_navigation.getMenu().findItem(R.id.lead_id).setChecked(true);
                fragment=new CustomerLeadsFragment();
                dashboard_tool.setVisibility(View.GONE);
                customer_options.setVisibility(View.GONE);
                warranty_tool.setVisibility(View.GONE);
                service_tool.setVisibility(View.GONE);
                leads_list_tool.setVisibility(View.VISIBLE);
                add_leads_tool.setVisibility(View.GONE);
                edit_tool.setVisibility(View.GONE);
                profile_tool.setVisibility(View.GONE);
                paid_service_tool.setVisibility(View.GONE);
                app_tool.setVisibility(View.GONE);
                cus_edit_service_tool.setVisibility(View.GONE);
                loadfragment(fragment);

            }
        });


        customer_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation.getMenu().findItem(R.id.service_id).setChecked(true);
                fragment=new CustomerServiceFragment();
                dashboard_tool.setVisibility(View.GONE);
                customer_options.setVisibility(View.GONE);
                warranty_tool.setVisibility(View.GONE);
                add_leads_tool.setVisibility(View.GONE);
                leads_list_tool.setVisibility(View.GONE);
                service_tool.setVisibility(View.VISIBLE);
                edit_tool.setVisibility(View.GONE);
                profile_tool.setVisibility(View.GONE);
                paid_service_tool.setVisibility(View.GONE);
                app_tool.setVisibility(View.GONE);
                cus_edit_service_tool.setVisibility(View.GONE);
              //  Toast.makeText(getApplicationContext(),"Development process on going,Designs using for static data",Toast.LENGTH_LONG).show();
                loadfragment(fragment);

            }
        });
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
        VolleyUtils.makeJsonObjectRequest(getApplicationContext(), Constants.LOGIN_OUT, object, new VolleyCallback() {
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
                    arrayList=new ArrayList<>();
                    adimageList=new ArrayList<>();
                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++){

                        JSONObject object=array.getJSONObject(i);
                        JSONArray details_array=object.getJSONArray("ads_details");
                        for (int j=1;j<details_array.length();j++) {
                            JSONObject obj1 = details_array.getJSONObject(j);
                            image = obj1.getString("ads_data_link");

                            if (image.contains("jpeg") ||image.contains("jpg") || image.contains("png")) {
                                Log.d("Success", "ads_content_2_conditions::" + image);
                            } else {
                                String text=obj1.getString("ads_data");
                                Log.d("Message","ScrollText_test::"+text);
                                AdsScrollTextModel model = new AdsScrollTextModel();
                                model.setScrolltext(text);
                                arrayList.add(model);
                            }
                            if (obj1.getString("ads_data_link").equalsIgnoreCase("")) {

                            }else {
                                String path = obj1.getString("ads_data_link");
                                    Log.d("Success", "Ads_image_path::" + path);
                                    adimageList.add(path);

                            }



                        }
                        for (int j=0;j<1;j++) {
                            JSONObject obj1 = details_array.getJSONObject(j);
                            String text=obj1.getString("ads_data");
                            Log.d("Message","ScrollText_test::"+text);
                            AdsScrollTextModel model = new AdsScrollTextModel();
                            model.setScrolltext(text);
                            arrayList.add(model);



                        }
                      //  adsimageget();
                    }
                    Log.d("Success", "ImageArraySize::" + adimageList.size());
                    scrollmethod(arrayList);
                    AdViewPagerAdapter adapter = new AdViewPagerAdapter(CustomerDashBoard.this, adimageList);
                    ad_view_pager.setAdapter(adapter);
                    setAutopager();
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
            scrollingtext.setSelected(true);
            Log.d("Success","scroll_text_test::"+path);


            }
        }





    private void setAutopager() {

        /*Runnable runnable = new Runnable() {
            public void run() {
                if (adimageList.size() == currentPage) {
                    currentPage = 0;
                } else {
                    currentPage++;
                }
                ad_view_pager.setCurrentItem(currentPage, true);
                handler.postDelayed(this, 5000);
            }
        };
    }*/



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
                       //  ha.postDelayed(this,10000);
                    Log.d("Success", "ads_test_2::" + currentPage);
                    currentPage = 0;
                    Log.d("Success", "ads_test_3::" + currentPage);
                } else {
                    ++currentPage;
                       ha.postDelayed(this, 10000);
                    Log.d("Success", "ads_test_4::" + currentPage);
                }


              //  ha.postDelayed(this, 5000);
            }
        }, 10000);

    }
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

                case R.id.lead_id:
                    if (close_value==1){

                        ads_img_ly.setVisibility(View.GONE);
                    }else {
                        ads_img_ly.setVisibility(View.VISIBLE);
                    }
                        dashboard_tool.setVisibility(View.GONE);
                        customer_options.setVisibility(View.GONE);
                        fragment = new CustomerLeadsFragment();
                        dashboard_tool.setVisibility(View.GONE);
                        customer_options.setVisibility(View.GONE);
                        warranty_tool.setVisibility(View.GONE);
                        service_tool.setVisibility(View.GONE);
                        leads_list_tool.setVisibility(View.VISIBLE);
                        add_leads_tool.setVisibility(View.GONE);
                        edit_tool.setVisibility(View.GONE);
                        profile_tool.setVisibility(View.GONE);
                        paid_service_tool.setVisibility(View.GONE);
                        app_tool.setVisibility(View.GONE);
                        loadfragment(fragment);
                        break;

                case R.id.home_id:
                    Log.d("Success","home_inside_test::");
                    if (close_value==1){
                        ads_img_ly.setVisibility(View.GONE);
                    }else {
                        ads_img_ly.setVisibility(View.VISIBLE);
                    }
                    fragment = new CustomerHomeFragment();
                    dashboard_tool.setVisibility(View.VISIBLE);
                    customer_options.setVisibility(View.VISIBLE);
                    service_tool.setVisibility(View.GONE);
                    warranty_tool.setVisibility(View.GONE);
                    add_leads_tool.setVisibility(View.GONE);
                    leads_list_tool.setVisibility(View.GONE);
                    edit_tool.setVisibility(View.GONE);
                    profile_tool.setVisibility(View.GONE);
                    paid_service_tool.setVisibility(View.GONE);
                    app_tool.setVisibility(View.GONE);
                    loadfragment(fragment);
                    break;
                case R.id.service_id:
                    if (close_value==1){
                        ads_img_ly.setVisibility(View.GONE);
                    }else {
                        ads_img_ly.setVisibility(View.VISIBLE);
                    }
                    fragment = new CustomerServiceFragment();
                    dashboard_tool.setVisibility(View.GONE);
                    customer_options.setVisibility(View.GONE);
                    warranty_tool.setVisibility(View.GONE);
                    add_leads_tool.setVisibility(View.GONE);
                    leads_list_tool.setVisibility(View.GONE);
                    service_tool.setVisibility(View.VISIBLE);
                    edit_tool.setVisibility(View.GONE);
                    profile_tool.setVisibility(View.GONE);
                    paid_service_tool.setVisibility(View.GONE);
                    app_tool.setVisibility(View.GONE);
              //      Toast.makeText(getApplicationContext(),"Development process on going,Designs using for static data",Toast.LENGTH_LONG).show();
                    loadfragment(fragment);
                    break;
                case R.id.profile_id:
                    if (close_value==1){
                        ads_img_ly.setVisibility(View.GONE);
                    }else {
                        ads_img_ly.setVisibility(View.VISIBLE);
                    }
                    fragment = new CustomerProfileFragment();
                    dashboard_tool.setVisibility(View.GONE);
                    customer_options.setVisibility(View.GONE);
                    warranty_tool.setVisibility(View.GONE);
                    add_leads_tool.setVisibility(View.GONE);
                    service_tool.setVisibility(View.GONE);
                    leads_list_tool.setVisibility(View.GONE);
                    edit_tool.setVisibility(View.GONE);
                    profile_tool.setVisibility(View.GONE);
                    paid_service_tool.setVisibility(View.GONE);
                    app_tool.setVisibility(View.GONE);
                    loadfragment(fragment);
                    break;
                case R.id.apps_id:
                    if (close_value==1){
                        ads_img_ly.setVisibility(View.GONE);
                    }else {
                        ads_img_ly.setVisibility(View.VISIBLE);
                    }
                    fragment = new CustomeryoutubeFragment();
                    dashboard_tool.setVisibility(View.GONE);
                    customer_options.setVisibility(View.GONE);
                    warranty_tool.setVisibility(View.GONE);
                    add_leads_tool.setVisibility(View.GONE);
                    service_tool.setVisibility(View.GONE);
                    leads_list_tool.setVisibility(View.GONE);
                    edit_tool.setVisibility(View.GONE);
                    profile_tool.setVisibility(View.GONE);
                    paid_service_tool.setVisibility(View.GONE);
                    app_tool.setVisibility(View.VISIBLE);
               //     Toast.makeText(getApplicationContext(),"Development process on going,Designs using for static data",Toast.LENGTH_LONG).show();
                    loadfragment(fragment);
                    break;
            }
            return false;
        }
    };

    public void findview() {
        customer_leads = findViewById(R.id.customer_leads);
        customer_service = findViewById(R.id.customer_service);
        customer_options=findViewById(R.id.customer_options);
        loader_ly=findViewById(R.id.loader_ly);
        cus_dash_ly=findViewById(R.id.cus_dash_ly);
        dashboard_tool=findViewById(R.id.dashboard_tool);
        add_leads_tool=findViewById(R.id.add_leads_tool);
        service_tool=findViewById(R.id.service_tool);
        add_leads_power=findViewById(R.id.add_leads_power);
        leads_list_tool=findViewById(R.id.leads_list_tool);
        customer_leads = findViewById(R.id.customer_leads);
        customer_service = findViewById(R.id.customer_service);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        ad_img_close = findViewById(R.id.ad_img_close);
        ads_img_ly = findViewById(R.id.ads_img_ly);
      //  add_leads_tv=findViewById(R.id.add_leads_tv);
        add_leads=findViewById(R.id.add_leads);
        serice_power=findViewById(R.id.serice_power);
        raise_ticket=findViewById(R.id.raise_ticket);
      //  raise_ticket_tv=findViewById(R.id.raise_ticket_tv);
        warranty_tool=findViewById(R.id.warranty_tool);
        lead_power=findViewById(R.id.lead_power);
        edit_power=findViewById(R.id.edit_power);
        edit_tool=findViewById(R.id.edit_tool);
        dash_power_btn=findViewById(R.id.dash_power_btn);
        profile_tool=findViewById(R.id.profile_tool);
        profile_power_btn=findViewById(R.id.profile_power_btn);
        ad_view_pager=findViewById(R.id.ad_view_pager);
        paid_service_power=findViewById(R.id.paid_service_power);
        apps_power=findViewById(R.id.apps_power);
        warranty_power_btn=findViewById(R.id.warranty_power_btn);
        paid_service_tool=findViewById(R.id.paid_service_tool);
        app_tool=findViewById(R.id.app_tool);
        scrollingtext=findViewById(R.id.scrollingtext);
        cus_edit_service_tool=findViewById(R.id.cus_edit_service_tool);
        customer_edit_service_power_btn=findViewById(R.id.cus_edit_service_power_btn);
    }

    public void allservicelist() {
        dashboard_tool.setVisibility(View.GONE);
        customer_options.setVisibility(View.GONE);
        fragment = new CustomerLeadsFragment();
        dashboard_tool.setVisibility(View.GONE);
        customer_options.setVisibility(View.GONE);
        warranty_tool.setVisibility(View.GONE);
        service_tool.setVisibility(View.GONE);
        leads_list_tool.setVisibility(View.GONE);
        cus_edit_service_tool.setVisibility(View.VISIBLE);
        add_leads_tool.setVisibility(View.GONE);
        edit_tool.setVisibility(View.GONE);
        profile_tool.setVisibility(View.GONE);
        paid_service_tool.setVisibility(View.GONE);
        app_tool.setVisibility(View.GONE);
    }

    private void loadfragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().remove(fragment);
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }

    @Override
    public void onBackPressed() {

        Log.d("Success", "menu_id::"+menu_id);
        int id =0;
        String home="home_id";

        if (menu_id==0) {
            Log.d("Success","equal::");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Log.d("Success", "home_this::");
            builder.setTitle("Please confirm");
            builder.setMessage("Are you want to exit the app?");
            builder.setCancelable(true);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d("Success", "home_on_click::");
                    CustomerDashBoard.super.finishAffinity();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d("Success", "home_load::");
                    /*fragment = new CustomerHomeFragment();
                    loadfragment(fragment);*/
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {

            switch (menu_id) {
                case R.id.home_id:
                    Log.d("Success", "home_out_side::");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    Log.d("Success", "home_this::");
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you want to exit the app?");
                    builder.setCancelable(true);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d("Success", "home_on_click::");
                            CustomerDashBoard.super.finishAffinity();
                            //  onBackPressed();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d("Success", "home_load::");
                           /* fragment = new CustomerHomeFragment();
                            loadfragment(fragment);*/
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    break;
                case R.id.lead_id:
                    Log.d("Success","leads_id::"+menu_id);
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you want to exit the app?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CustomerDashBoard.super.finishAffinity();

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           /* fragment = new CustomerLeadsFragment();
                            loadfragment(fragment);*/
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                    break;
                case R.id.service_id:
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you want to exit the app?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CustomerDashBoard.super.finishAffinity();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           /* fragment = new CustomerServiceFragment();
                            loadfragment(fragment);*/
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                    break;
                case R.id.profile_id:
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you want to exit the app?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CustomerDashBoard.super.finishAffinity();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           /* fragment = new CustomerProfileFragment();
                            loadfragment(fragment);*/
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                    break;
                case R.id.apps_id:
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you want to exit the app?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CustomerDashBoard.super.finishAffinity();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                          /*  fragment = new CustomeryoutubeFragment();
                            loadfragment(fragment);*/
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                    break;
                case R.id.customer_service:
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you want to exit the app?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CustomerDashBoard.super.finishAffinity();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           /* fragment = new CustomerServiceFragment();
                            loadfragment(fragment);*/
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                    break;

            }

        }
    }

    public void editlistMethod() {
//        FragmentManager fm = getSupportFragmentManager();
        Log.d("Success","fragment_method::");
        dashboard_tool.setVisibility(View.GONE);
        customer_options.setVisibility(View.GONE);
        warranty_tool.setVisibility(View.GONE);
        service_tool.setVisibility(View.GONE);
        leads_list_tool.setVisibility(View.GONE);
        add_leads_tool.setVisibility(View.GONE);
        edit_tool=findViewById(R.id.edit_tool);
        edit_tool.setVisibility(View.VISIBLE);
        profile_tool.setVisibility(View.GONE);
        paid_service_tool.setVisibility(View.GONE);
        cus_edit_service_tool.setVisibility(View.GONE);
    }

    public void profileMethod() {
        Log.d("Success","Profile_test::");
        dashboard_tool.setVisibility(View.GONE);
        customer_options.setVisibility(View.GONE);
        warranty_tool.setVisibility(View.GONE);
        service_tool.setVisibility(View.GONE);
        leads_list_tool.setVisibility(View.GONE);
        add_leads_tool.setVisibility(View.GONE);
        edit_tool=findViewById(R.id.edit_tool);
        edit_tool.setVisibility(View.GONE);
        ads_img_ly.setVisibility(View.GONE);
        profile_tool.setVisibility(View.VISIBLE);
        paid_service_tool.setVisibility(View.GONE);
        cus_edit_service_tool.setVisibility(View.GONE);
    }

  /*  public void showProgress() {
        findViewById(R.id.frame_container).setVisibility(View.GONE);
        findViewById(R.id.loader_ly).setVisibility(View.VISIBLE);
        dashboard_tool.setVisibility(View.VISIBLE);
        customer_options.setVisibility(View.GONE);
        bottom_navigation.setVisibility(View.VISIBLE);
        //  ads_img_ly.setVisibility(View.GONE);
    }

    public void hideProgress() {
        findViewById(R.id.frame_container).setVisibility(View.VISIBLE);
        findViewById(R.id.loader_ly).setVisibility(View.GONE);
        dashboard_tool.setVisibility(View.VISIBLE);
        customer_options.setVisibility(View.VISIBLE);
        bottom_navigation.setVisibility(View.VISIBLE);
        //  ads_img_ly.setVisibility(View.VISIBLE);
    }*/

    public void paidserviceMethod() {
        dashboard_tool.setVisibility(View.GONE);
        customer_options.setVisibility(View.GONE);
        warranty_tool.setVisibility(View.GONE);
        service_tool.setVisibility(View.GONE);
        leads_list_tool.setVisibility(View.GONE);
        add_leads_tool.setVisibility(View.GONE);
        edit_tool=findViewById(R.id.edit_tool);
        edit_tool.setVisibility(View.GONE);
        profile_tool.setVisibility(View.GONE);
        paid_service_tool.setVisibility(View.VISIBLE);
        cus_edit_service_tool.setVisibility(View.GONE);
    }



    public void edittoleads() {
        edit_tool=findViewById(R.id.edit_tool);
        edit_tool.setVisibility(View.GONE);
        leads_list_tool.setVisibility(View.VISIBLE);
    }






    @Override
    public void showProgress() {
        findViewById(R.id.frame_container).setVisibility(View.GONE);
        findViewById(R.id.loader_ly).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        findViewById(R.id.frame_container).setVisibility(View.VISIBLE);
        findViewById(R.id.loader_ly).setVisibility(View.GONE);
    }

    public void addleads() {
        dashboard_tool.setVisibility(View.GONE);
        customer_options.setVisibility(View.GONE);
        fragment = new CustomerLeadsFragment();
        dashboard_tool.setVisibility(View.GONE);
        customer_options.setVisibility(View.GONE);
        warranty_tool.setVisibility(View.GONE);
        service_tool.setVisibility(View.GONE);
        leads_list_tool.setVisibility(View.VISIBLE);
        add_leads_tool.setVisibility(View.GONE);
        edit_tool.setVisibility(View.GONE);
        profile_tool.setVisibility(View.GONE);
        paid_service_tool.setVisibility(View.GONE);
        app_tool.setVisibility(View.GONE);
        cus_edit_service_tool.setVisibility(View.GONE);
    }
public void addservice(){
    dashboard_tool.setVisibility(View.GONE);
    customer_options.setVisibility(View.GONE);
    warranty_tool.setVisibility(View.GONE);
    add_leads_tool.setVisibility(View.GONE);
    leads_list_tool.setVisibility(View.GONE);
    service_tool.setVisibility(View.VISIBLE);
    edit_tool.setVisibility(View.GONE);
    profile_tool.setVisibility(View.GONE);
    paid_service_tool.setVisibility(View.GONE);
    app_tool.setVisibility(View.GONE);
    cus_edit_service_tool.setVisibility(View.GONE);
}
public void addleadsfloat(){
    fragment=new AddLeadsFragment();
    dashboard_tool.setVisibility(View.GONE);
    customer_options.setVisibility(View.GONE);
    service_tool.setVisibility(View.GONE);
    add_leads_tool.setVisibility(View.VISIBLE);
    leads_list_tool.setVisibility(View.GONE);
    edit_tool.setVisibility(View.GONE);
    profile_tool.setVisibility(View.GONE);
    paid_service_tool.setVisibility(View.GONE);
    app_tool.setVisibility(View.GONE);
    cus_edit_service_tool.setVisibility(View.GONE);
    loadfragment(fragment);
}
public void addservicefloat(){
    fragment=new CustomerWarrantyFragment();
    loadfragment(fragment);
    warranty_tool.setVisibility(View.VISIBLE);
    fragment = new CustomerLeadsFragment();
    dashboard_tool.setVisibility(View.GONE);
    customer_options.setVisibility(View.GONE);
    service_tool.setVisibility(View.GONE);
    leads_list_tool.setVisibility(View.GONE);
    add_leads_tool.setVisibility(View.GONE);
    edit_tool.setVisibility(View.GONE);
    profile_tool.setVisibility(View.GONE);
    paid_service_tool.setVisibility(View.GONE);
    app_tool.setVisibility(View.GONE);
    cus_edit_service_tool.setVisibility(View.GONE);
}



    public void adsclose() {
        int close=1;
        ads_img_ly.setVisibility(View.GONE);
    }


  /*  public void customerfullservice() {
        loadfragment(fragment);
        warranty_tool.setVisibility(View.GONE);
        fragment = new CustomerLeadsFragment();
        dashboard_tool.setVisibility(View.GONE);
        customer_options.setVisibility(View.GONE);
        service_tool.setVisibility(View.GONE);
        leads_list_tool.setVisibility(View.GONE);
        add_leads_tool.setVisibility(View.GONE);
        edit_tool.setVisibility(View.GONE);
        profile_tool.setVisibility(View.GONE);
        paid_service_tool.setVisibility(View.GONE);
        app_tool.setVisibility(View.GONE);
        cus_edit_service_tool.setVisibility(View.VISIBLE);


    }*/
}


