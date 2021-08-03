package com.f2f.incls.utilitty;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class InternetConnection {
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("TAG", "Internet Connection Not Present");
            return false;
        }
    }
}
