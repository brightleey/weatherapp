package com.example.weatherapp.adapter;

/**
 * Created by Administrator on 2017/5/19.
 */

public class WeatherItem {
    private String weaMax, weaMin, weaDate;
    private int weaImgId;

    public WeatherItem(String weaMax, String weaMin, String weaDate, int weaImgId) {
        this.weaMax = weaMax;
        this.weaMin = weaMin;
        this.weaDate = weaDate;
        this.weaImgId = weaImgId;
    }

    public void setWeaMax(String weaMax) {
        this.weaMax = weaMax;
    }

    public void setWeaMin(String weaMin) {
        this.weaMin = weaMin;
    }

    public void setWeaDate(String weaDate) {
        this.weaDate = weaDate;
    }

    public void setWeaImgId(int weaImgId) {
        this.weaImgId = weaImgId;
    }

    public String getWeaMax() {
        return weaMax;
    }

    public String getWeaMin() {
        return weaMin;
    }

    public String getWeaDate() {
        return weaDate;
    }

    public int getWeaImgId() {
        return weaImgId;
    }
}
