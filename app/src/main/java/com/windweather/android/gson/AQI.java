package com.windweather.android.gson;

/**
 * Created by jiepeng on 2018/6/16.
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
