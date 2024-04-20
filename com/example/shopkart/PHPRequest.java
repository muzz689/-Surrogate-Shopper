package com.example.shopkart;

import android.app.Activity;
import android.content.ContentValues;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PHPRequest {
    String base_url = "";
    public PHPRequest(String prefix){
        this.base_url = prefix;
    }

    public void doRequest(Activity a, String method, ContentValues params , RequestHandler rh){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        for(String key : params.keySet()){
            builder.add(key, params.getAsString(key));
        }
        Request request = new Request.Builder().url(base_url + method).post(builder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseData = response.body().string();
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rh.processResponse(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public void doRequest(Activity a, String method, ContentValues params, String json, String instruct, RequestHandler rh){
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(String key : params.keySet()){
            builder.addFormDataPart(key, params.getAsString(key));
        }
        builder.addFormDataPart("Items", json);
        builder.addFormDataPart("Instruction", instruct);
        Request request = new Request.Builder().url(base_url + method).post(builder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseData = response.body().string();
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rh.processResponse(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
