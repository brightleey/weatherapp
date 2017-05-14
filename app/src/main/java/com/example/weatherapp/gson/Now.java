package com.example.weatherapp.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/12.
 */

public class Now {
    @SerializedName("cond")
    public WeatherInfo weatherInfo;
    public class WeatherInfo{
        @SerializedName("code")
        public String weatherCode;
        @SerializedName("txt")
        public String weatherText;
    }
    @SerializedName("fl")
    public String tiganwendu;
    @SerializedName("hum")
    public String xiangduishidu;
    @SerializedName("pcpn")
    public String jiangshuiliang;
    @SerializedName("pres")
    public String qiya;
    @SerializedName("tmp")
    public String wendu;
    @SerializedName("vis")
    public String nengjiandu;
    @SerializedName("wind")
    public WindInfo windInfo;
    public class WindInfo{
        @SerializedName("dir")
        public String fengxiang;
        @SerializedName("sc")
        public String fengli;
    }
}
