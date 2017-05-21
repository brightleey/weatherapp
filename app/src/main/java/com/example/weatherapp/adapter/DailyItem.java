package com.example.weatherapp.adapter;

/**
 * Created by Administrator on 2017/5/19.
 */

public class DailyItem {
    private String weaMax, weaMin, weaDate, weaTxt;
    private int weaImgId;

    public DailyItem(String weaMax, String weaMin, String weaDate, int weaImgId, String weaTxt) {
        this.weaMax = weaMax;
        this.weaMin = weaMin;
        this.weaDate = weaDate;
        this.weaImgId = weaImgId;
        this.weaTxt = weaTxt;
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

    public void setWeaTxt(String weaTxt) {
        this.weaTxt = weaTxt;
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

    public String getWeaTxt() {
        return weaTxt;
    }
}
