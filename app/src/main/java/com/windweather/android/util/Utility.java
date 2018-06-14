package com.windweather.android.util;

import android.text.TextUtils;

import com.windweather.android.db.City;
import com.windweather.android.db.County;
import com.windweather.android.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiepeng on 2018/6/13.
 */

public class Utility {
    /*
    解析和处理服务器返回的省数据
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            JSONArray allProvinces = null;
            try {
                allProvinces = new JSONArray(response);
                for (int i = 0;i < allProvinces.length();i++){
                    JSONObject jsonObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setmProvinceName(jsonObject.getString("name"));
                    province.setmProivinceCode(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return false;
    }
    /*
    解析服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0 ; i < jsonArray.length() ; i ++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setmCityName(jsonObject.getString("name"));
                    city.setmCityCode(jsonObject.getInt("id"));
                    city.setmProvinceid(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0 ; i < jsonArray.length() ; i ++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setmCountyName(jsonObject.getString("name"));
                    county.setmCityId(cityId);
                    county.setmWeatherId(jsonObject.getString("weather_id"));
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
