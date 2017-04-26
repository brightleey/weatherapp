package com.example.weatherapp.db;

/**
 * Created by Administrator on 2017/4/25.
 */

public class City {
    private int id,provinceId;
    private String cityName;
    private String cityCode;

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getId() {
        return id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityCode() {
        return cityCode;
    }
}
