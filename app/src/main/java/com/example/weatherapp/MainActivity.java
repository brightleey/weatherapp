package com.example.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherId = pref.getString("weather_id", "");
        String areaName = pref.getString("area_name", "");
        if (!TextUtils.isEmpty(weatherId)){
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("weather_id", weatherId);
            intent.putExtra("area_name", areaName);
            startActivity(intent);
            finish();
        }
    }

}
