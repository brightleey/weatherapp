package com.example.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.util.HttpCallbackListener;
import com.example.weatherapp.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
        requestWeather(weatherId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wea_menu_icon:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }

    private void handleWeatherResponse(String weatherString){
        if (!TextUtils.isEmpty(weatherString)){
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonParser().parse(weatherString).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("HeWeather5").getAsJsonArray();
            String weatherInfo = jsonArray.get(0).toString();
            
        }
    }

    private void requestWeather(final String weatherId){
        final SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = spf.getString(weatherId, "");
        if (!TextUtils.isEmpty(weatherString)){
            handleWeatherResponse(weatherString);
        }else {
            HttpUtil.httpRequest("https://free-api.heweather.com/v5/weather?city="+weatherId+"&key="+WEATHER_API_KEY, new HttpCallbackListener() {
                @Override
                public void onSuccess(String responseText) {
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString(weatherId, responseText);
                    editor.apply();
                    requestWeather(responseText);
                }

                @Override
                public void onFail(Exception e) {
                //获取天气失败
                }
            });
        }
    }
}
