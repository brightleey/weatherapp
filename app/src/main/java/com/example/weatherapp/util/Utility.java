package com.example.weatherapp.util;

import android.text.TextUtils;

import com.example.weatherapp.db.City;
import com.example.weatherapp.db.County;
import com.example.weatherapp.db.Province;
import com.example.weatherapp.db.WeatherDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by Administrator on 2017/5/4.
 */

public class Utility {

    public static  Boolean parseXmlWithPull(String xmlString){
        if (!TextUtils.isEmpty(xmlString)){
            try {
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(new StringReader(xmlString));
                int eventType = xmlPullParser.getEventType();
                while (eventType != xmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case xmlPullParser.START_DOCUMENT:{

                            break;
                        }
                        case xmlPullParser.START_TAG:{

                            break;
                        }
                        case xmlPullParser.END_DOCUMENT:{

                        }
                    }
                    eventType = xmlPullParser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Boolean handleProvinceResponse(String response, WeatherDB weatherDB){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray provinces = new JSONArray(response);
                for (int i = 0; i < provinces.length(); i++){
                    JSONObject jsonProvince = provinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonProvince.getString("name"));
                    province.setProvinceCode(jsonProvince.getInt("id"));
                    weatherDB.saveProvince(province);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Boolean handleCityResponse(String response, int provinceId, WeatherDB weatherDB){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray cities = new JSONArray(response);
                for (int i = 0; i < cities.length(); i++){
                    JSONObject cityJson = cities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityJson.getString("name"));
                    city.setCityCode(cityJson.getInt("id"));
                    city.setProvinceId(provinceId);
                    weatherDB.saveCity(city);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Boolean handleCountyResponse(String response, int cityId, WeatherDB weatherDB){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray counties = new JSONArray(response);
                for (int i = 0; i < counties.length(); i++){
                    JSONObject countyJson = counties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyJson.getString("name"));
                    county.setCountyCode(countyJson.getInt("id"));
                    county.setWeatherId(countyJson.getString("weather_id"));
                    county.setCityId(cityId);
                    weatherDB.saveCounty(county);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
