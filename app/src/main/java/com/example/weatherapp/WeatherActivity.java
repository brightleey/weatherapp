package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by brightleey on 2017/5/6.
 */

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    //private final static String WEATHER_API_KEY = "c0225d0fd8cd4b2da8e5dae6c9db6c0f";
    private final static String WEATHER_API_KEY = "bc0418b57b2d4918819d3974ac1285d9";
    private DrawerLayout drawerLayout;
    private TextView weaAddr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明化任务栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        ImageView menuIcon = (ImageView)findViewById(R.id.wea_menu_icon);
        drawerLayout = (DrawerLayout) findViewById(R.id.wea_drawerlayout);
        weaAddr = (TextView) findViewById(R.id.wea_addr);
        Intent intent = getIntent();
        String weatherId = intent.getStringExtra("weather_id");
        String areaName = intent.getStringExtra("area_name");
        menuIcon.setOnClickListener(this);
        //tips.setText("这个是WeatherActivity，weather_id："+weatherId+"，area_name："+ areaName);

        weaAddr.setText(areaName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wea_menu_icon:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }
}
