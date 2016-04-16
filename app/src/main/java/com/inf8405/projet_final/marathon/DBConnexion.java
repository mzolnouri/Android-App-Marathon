package com.inf8405.projet_final.marathon;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)
public final class DBConnexion {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static String getRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        Log.d("response to get",response.toString());
        String temp=response.body().string();
        Log.d("body",temp);
        return temp;
    }

    public static String postRequest(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Log.d("response to post",response.toString());
        String temp=response.body().string();
        Log.d("body",temp);
        return temp;
    }
}