package com.windweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by jiepeng on 2018/6/13.
 */

public class County extends DataSupport {
    private int id;
    private String mCountyName;
    private String mWeatherId;
    private  int mCityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmCountyName() {
        return mCountyName;
    }

    public void setmCountyName(String mCountyName) {
        this.mCountyName = mCountyName;
    }

    public String getmWeatherId() {
        return mWeatherId;
    }

    public void setmWeatherId(String mWeatherId) {
        this.mWeatherId = mWeatherId;
    }

    public int getmCityId() {
        return mCityId;
    }

    public void setmCityId(int mCityId) {
        this.mCityId = mCityId;
    }
}
