package com.example.weatherapp.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/12.
 */

public class AQI {
    @SerializedName("city")
    public AirAquality airAquality;
    public class AirAquality{
        public String aqi;
        public String pm10;
        public String pm25;
        @SerializedName("qlty")
        public String aqitxt;
    }
}
