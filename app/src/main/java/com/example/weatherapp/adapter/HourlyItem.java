package com.example.weatherapp.adapter;

/**
 * Created by Administrator on 2017/5/19.
 */

public class HourlyItem {
    private String weaTime, weaTemp, weaTxt;
    private int weaImgId;

    public HourlyItem(String weaTime, int weaImgId, String weaTemp, String weaTxt) {
        this.weaTime = weaTime;
        this.weaTemp = weaTemp;
        this.weaImgId = weaImgId;
        this.weaTxt = weaTxt;
    }

    public void setWeaTime(String weaTime) {
        this.weaTime = weaTime;
    }

    public void setWeaTemp(String weaTemp) {
        this.weaTemp = weaTemp;
    }

    public void setWeaTxt(String weaTxt) {
        this.weaTxt = weaTxt;
    }

    public void setWeaImgId(int weaImgId) {
        this.weaImgId = weaImgId;
    }

    public String getWeaTime() {
        return weaTime;
    }

    public String getWeaTemp() {
        return weaTemp;
    }

    public String getWeaTxt() {
        return weaTxt;
    }

    public int getWeaImgId() {
        return weaImgId;
    }
}
