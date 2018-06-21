package com.windweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jiepeng on 2018/6/16.
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
    public Now now;
    public Suggestion suggestion;

}
