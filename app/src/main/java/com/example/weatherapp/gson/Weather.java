package com.example.weatherapp.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

public class Weather {
    public String status;
    public AQI aqi;
    public Basic basic;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Daily> dailyList;
    @SerializedName("hourly_forecast")
    public List<Hourly> hourlyList;
}
