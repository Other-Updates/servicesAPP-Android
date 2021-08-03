package com.f2f.incls.utilitty;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by employee on 2/26/2018.
 */

public interface VolleyCallback {
    void onError(String message, VolleyError error);
    String onResponse(JSONObject response) throws JSONException;
}
