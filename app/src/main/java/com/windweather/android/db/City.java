package com.windweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by jiepeng on 2018/6/13.
 */

public class City extends DataSupport {
    private int id;
    private String mCityName;
    private int mCityCode;
    private int mProvinceid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmCityName() {
        return mCityName;
    }

    public void setmCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public int getmCityCode() {
        return mCityCode;
    }

    public void setmCityCode(int mCityCode) {
        this.mCityCode = mCityCode;
    }

    public int getmProvinceid() {
        return mProvinceid;
    }

    public void setmProvinceid(int mProvinceid) {
        this.mProvinceid = mProvinceid;
    }
}
