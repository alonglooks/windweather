package com.windweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jiepeng on 2018/6/16.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}
