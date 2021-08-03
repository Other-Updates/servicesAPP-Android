package com.f2f.incls.utilitty;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
   private static VolleySingleton singleton;
    RequestQueue requestQueue;
    Context context;

    public VolleySingleton(VolleySingleton singleton,RequestQueue requestQueue,Context context) {
        this.singleton = singleton;
        this.requestQueue=requestQueue;
        this.context=context;
    }

    public VolleySingleton(Context context) {
        this.context=context;
    }


    public static synchronized VolleySingleton getInstance(Context context){
     if (singleton==null){
         singleton = new VolleySingleton(context);
     }
     return singleton;
    }
    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
