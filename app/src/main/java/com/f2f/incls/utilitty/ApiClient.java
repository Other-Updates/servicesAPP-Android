package com.f2f.incls.utilitty;

import com.f2f.incls.model.CustomerLogin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;





  /*  static Gson gson=new GsonBuilder().setLenient().create();



    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", Constants.authToken)
                    .addHeader("username", Constants.user_name)
                    .addHeader("password", Constants.password)
                    .addHeader("Accesstoken", Constants.access)
                    .addHeader("X-API-KEY", Constants.api_key)
                    .addHeader("Content-Type", Constants.con_type)
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();
*/

    public static Retrofit getClient() {
       /* if (retrofit == null) {

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);

//            Request request = requestBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(clientBuilder.build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
*/

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(
                new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request original = chain.request();
                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .addHeader("Authorization", Constants.authToken)
                                .addHeader("username", Constants.user_name)
                                .addHeader("password", Constants.password)
                                .addHeader("Accesstoken", Constants.access)
                                .addHeader("X-API-KEY", Constants.api_key)
                                .addHeader("Content-Type", Constants.con_type)
                                .method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor).connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
          /*  httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", Constants.authToken)
                            .addHeader("username", Constants.user_name)
                            .addHeader("password", Constants.password)
                            .addHeader("Accesstoken", Constants.access)
                            .addHeader("X-API-KEY", Constants.api_key)
                            .addHeader("Content-Type", Constants.con_type)
                            .build();
                    return chain.proceed(request);
                }
            });
             retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                     .baseUrl(Constants.BASE_URL).client(httpClient.build()).build();


            *//*OkHttpClient okHttpClient = new OkHttpClient();
            int TIMEOUT = 2 *60 *1000;
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(Constants.BASE_URL)
//                    .client(okHttpClient.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS).build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();*//*
        }
        return retrofit;
    }*/
}
         /*   return retrofit;
        }*/

