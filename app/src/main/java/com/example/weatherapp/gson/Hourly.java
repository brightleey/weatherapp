package com.example.weatherapp.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/12.
 */

public class Hourly {
    @SerializedName("cond")
    public WeatherInfo weatherInfo;
    public class WeatherInfo{
        public String code;
        public String txt;
    }
    @SerializedName("tmp")
    public String temperature;    public String date;

}
