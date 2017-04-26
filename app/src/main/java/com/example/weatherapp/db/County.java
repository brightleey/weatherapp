package com.example.weatherapp.db;

/**
 * Created by Administrator on 2017/4/25.
 */

public class County {
    private int id,cityId;
    private String countyName,countyCode,weatherId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getId() {
        return id;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public String getWeatherId() {
        return weatherId;
    }
}
