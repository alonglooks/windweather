package com.windweather.android.serivce;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.windweather.android.gson.Weather;
import com.windweather.android.util.OkHttpUtil;
import com.windweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int i = 1000*60*60*8;
        long l = SystemClock.elapsedRealtime() + i;
        Intent intent1 = new Intent(this, AutoUpdateService.class);
        PendingIntent pdintent = PendingIntent.getService(this, 0, intent1, 0);
        alarmManager.cancel(pdintent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,l,pdintent);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {
        String bingPicUrl = "http://guolin.tech/api/bing_pic";
        OkHttpUtil.sendOkHttpRequest(bingPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                if (bingPic != null){
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    edit.putString("bing_pic",bingPic);
                    edit.apply();
                }
            }
        });
    }

    private void updateWeather() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weather = preferences.getString("weather", null);
        if (weather != null){
            Weather weather1 = Utility.handleWeatherResponse(weather);
            String weatherId = weather1.basic.weatherId;
            String wetherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=760a94ab0f964515b1ecf73092fa77b2";
            OkHttpUtil.sendOkHttpRequest(wetherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    Weather weather2 = Utility.handleWeatherResponse(string);
                    if(string != null && "ok".equals(weather2.status)){
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("weather", string);
                        edit.apply();
                    }
                }
            });
        }

    }
}
