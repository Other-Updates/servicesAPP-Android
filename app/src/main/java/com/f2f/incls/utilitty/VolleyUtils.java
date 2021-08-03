package com.f2f.incls.utilitty;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by employee on 2/26/2018.
 */

public class VolleyUtils {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    public static void makeJsonObjectRequest(final Context context, String url, JSONObject object, final VolleyCallback listener) {

        AppController.getInstance().getRequestQueue().getCache().clear();
        try {
           /* Log.d("success", "URL:: " + url);
            Log.d("success", "object:: " + object);*/
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        listener.onResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                   // listener.onError(error.toString(),error);
                   /* if (error instanceof NetworkError) {
                        Toast.makeText(context, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }*/

                   /* listener.onError(error.toString(),error);
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(context, Constants.TIME_OUT, Toast.LENGTH_SHORT).show();

                    } else if (error instanceof AuthFailureError) {
                       Toast.makeText(context, Constants.AUTH_FAILURE_ERROR, Toast.LENGTH_SHORT).show();


                    } else if (error instanceof ServerError) {
                        Toast.makeText(context, Constants.SERVER_ERROR, Toast.LENGTH_SHORT).show();



                    } else if (error instanceof NetworkError) {
                        Toast.makeText(context, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();


                    } else if (error instanceof ParseError) {
                        Toast.makeText(context, Constants.PARSE_ERROR, Toast.LENGTH_SHORT).show();

                    }*/
                }
            }){
                /* @Override
                 protected Map<String, String> getParams() throws AuthFailureError {
                     Log.v("getparams","Is called");
                     Map<String, String> params = new HashMap<String, String>();
                     return params;
                 }*/
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    String creds = String.format("%s:%s","admin","1234");
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }

            };
            int socketTimeout = 40000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, "json_array_req");
        }catch (Exception e) {
            Log.e("exception", "erro:: " + e);
        }
        }
    public static boolean permission(final Context context) {
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,
                                                Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }
}


