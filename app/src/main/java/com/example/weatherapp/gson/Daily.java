package com.example.weatherapp.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/12.
 */

public class Daily {
    @SerializedName("tmp")
    public Temperature temperature;
    public class Temperature{
        public String max;
        public String min;
    }
    @SerializedName("cond")
    public WeatherInfo weatherInfo;
    public class WeatherInfo{
        @SerializedName("code_d")
        public String codeDay;
        @SerializedName("code_n")
        public String codeNight;
        @SerializedName("txt_d")
        public String txtDay;
        @SerializedName("txt_n")
        public String txtNight;
    }
    public String date;
}
