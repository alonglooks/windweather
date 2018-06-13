package com.windweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by jiepeng on 2018/6/13.
 */

public class Province extends DataSupport {
    private int id ;
    private String mProvinceName;
    private int mProivinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmProvinceName() {
        return mProvinceName;
    }

    public void setmProvinceName(String mProvinceName) {
        this.mProvinceName = mProvinceName;
    }

    public int getmProivinceCode() {
        return mProivinceCode;
    }

    public void setmProivinceCode(int mProivinceCode) {
        this.mProivinceCode = mProivinceCode;
    }
}
