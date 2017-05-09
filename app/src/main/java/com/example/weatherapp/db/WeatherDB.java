package com.example.weatherapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */

public class WeatherDB {
    private final static String DB_NAME = "weatherapp.db";
    private final static int DB_VERSION = 2;

    private static WeatherDB weatherDB;
    private SQLiteDatabase db;

    private WeatherDB(Context context){
        WeatherDatabaseHelper dbHelper = new WeatherDatabaseHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static WeatherDB getInstance(Context context){
        if (weatherDB == null){
            weatherDB = new WeatherDB(context);
        }
        return weatherDB;
    }

    public void saveProvince(Province province){
        if (province != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name", province.getProvinceName());
            contentValues.put("province_code", province.getProvinceCode());
            db.insert("province", null, contentValues);
        }
    }

    public List<Province> loadProvince(){
        List<Province> provinceList = new ArrayList<>();
        Cursor cursor = db.query("province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setProvinceCode(cursor.getInt(cursor.getColumnIndex("province_code")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                provinceList.add(province);
            } while (cursor.moveToNext());
        }
        return  provinceList;
    }

    public void saveCity(City city){
        if (city != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("city_name", city.getCityName());
            contentValues.put("city_code", city.getCityCode());
            contentValues.put("province_id", city.getProvinceId());
            db.insert("city", null, contentValues);
        }
    }

    public List<City> loadCity(int provinceId){
        List<City> cityList = new ArrayList<>();
        Cursor cursor = db.query("city", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setCityCode(cursor.getInt(cursor.getColumnIndex("city_code")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                cityList.add(city);
            } while (cursor.moveToNext());
        }
        return cityList;
    }

    public void saveCounty(County county){
        if (county != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("county_name", county.getCountyName());
            contentValues.put("county_code", county.getCountyCode());
            contentValues.put("city_id", county.getCityId());
            contentValues.put("weather_id", county.getWeatherId());
            db.insert("county", null, contentValues);
        }
    }

    public List<County> loadCounty(int cityId){
        List<County> countyList = new ArrayList<>();
        Cursor cursor = db.query("county", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setCountyCode(cursor.getInt(cursor.getColumnIndex("county_code")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                county.setWeatherId(cursor.getString(cursor.getColumnIndex("weather_id")));
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                countyList.add(county);
            } while (cursor.moveToNext());
        }
        return countyList;
    }
}
