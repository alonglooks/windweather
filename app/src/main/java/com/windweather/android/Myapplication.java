package com.windweather.android;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.litepal.LitePal;

/**
 * Created by jiepeng on 2018/6/13.
 */

public class Myapplication extends Application {
   public static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePal.initialize(context);
        super.onCreate();
    }

    public static Context getMyapplication() {
        return context;
    }
}
