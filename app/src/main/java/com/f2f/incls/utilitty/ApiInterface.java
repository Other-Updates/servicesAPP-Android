package com.f2f.incls.utilitty;

import com.f2f.incls.model.CustomerLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
   // @Headers({"Authorization:Basic YWRtaW46MTIzNA==","username:admin","password:1234","Accesstoken:JHFTyhf68GY9nhk","X-API-KEY:admin@123","Content-Type:application/x-www-form-urlencoded"})
    @POST("cust_login")
    Call<CustomerLogin> customerLogin(@Body String body);
}
