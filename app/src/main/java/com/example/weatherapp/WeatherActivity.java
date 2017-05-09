package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by brightleey on 2017/5/6.
 */

public class WeatherActivity extends AppCompatActivity {
    private final static String WEATHER_API_KEY = "c0225d0fd8cd4b2da8e5dae6c9db6c0f";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        TextView tips = (TextView)findViewById(R.id.tips);
        Intent intent = getIntent();
        String weatherId = intent.getStringExtra("weather_id");
        String areaName = intent.getStringExtra("area_name");
        tips.setText("这里是WeatherActivity，weather_id："+weatherId+"，area_name："+ areaName);
    }
}
