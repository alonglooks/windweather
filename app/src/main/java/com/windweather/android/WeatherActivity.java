package com.windweather.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windweather.android.gson.AQI;
import com.windweather.android.gson.Forecast;
import com.windweather.android.gson.Weather;
import com.windweather.android.util.OkHttpUtil;
import com.windweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
private ScrollView scrollView;
private TextView titleCity,titleUpdataTime,degreeText,weatherInfoText,aqiText,pm25Text,comfortText,carWashText,sportText;
private LinearLayout forecastLayout;
private ImageView bingPicImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化
        bingPicImg = findViewById(R.id.bing_pic_img);
        forecastLayout = findViewById(R.id.forecast_layout);
        scrollView = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdataTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String bing_pic = sharedPreferences.getString("bing_pic", null);
        if (bing_pic !=null){
            loadBingPic(bing_pic);
        }else {
            loadBingPicHttp();
        }

        String weatherString = sharedPreferences.getString("weather", null);
        //优先使用缓存
        if (weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
            //无缓存时到服务器下载
        }else {
            String weather_id = getIntent().getStringExtra("weather_id");
            scrollView.setVisibility(View.INVISIBLE);
            requestWeathe(weather_id);
            
        }
    }

    private void loadBingPic(String binImgUrl) {
        Glide.with(WeatherActivity.this).load(binImgUrl).into(bingPicImg);
    }

    private void loadBingPicHttp() {
        String bingPicUrl = "http://guolin.tech/api/bing_pic";
        OkHttpUtil.sendOkHttpRequest(bingPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingImgUrl = response.body().string();
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                edit.putString("bing_pic",bingImgUrl);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadBingPic(bingImgUrl);

                    }
                });



            }
        });
    }

    private void requestWeathe(String weatherId) {
        String wetherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=760a94ab0f964515b1ecf73092fa77b2";
        OkHttpUtil.sendOkHttpRequest(wetherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(WeatherActivity.this,"网络失败",Toast.LENGTH_SHORT).show();
                   }
               });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                Log.d("-------",string);
                final Weather weather = Utility.handleWeatherResponse(string);
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       if (weather!=null && weather.status.equals("ok")){
                           SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                           edit.putString("weather",string);
                           edit.apply();
                           showWeatherInfo(weather);
                       }else {
                           Toast.makeText(WeatherActivity.this,"犯取天气失败",Toast.LENGTH_SHORT).show();
                       }
                   }
               });

            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdataTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast:weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateTText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minTex = view.findViewById(R.id.min_text);
            dateTText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minTex.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        if (weather.suggestion != null){
            String comfort = weather.suggestion.comfort.info;
            String carWash = weather.suggestion.carWash.info;
            String sport = weather.suggestion.sport.info;
            carWashText.setText(carWash);
            comfortText.setText(comfort);
            sportText.setText(sport);
        }


        scrollView.setVisibility(View.VISIBLE);




    }
}
