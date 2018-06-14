package com.windweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by jiepeng on 2018/6/13.
 */

public class OkHttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
