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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weatherapp.adapter.DailyAdapter;
import com.example.weatherapp.adapter.DailyItem;
import com.example.weatherapp.adapter.HourlyAdapter;
import com.example.weatherapp.adapter.HourlyItem;
import com.example.weatherapp.adapter.IndexAdapter;
import com.example.weatherapp.adapter.IndexItem;
import com.example.weatherapp.gson.Daily;
import com.example.weatherapp.gson.Hourly;
import com.example.weatherapp.gson.Suggestion;
import com.example.weatherapp.gson.Weather;
import com.example.weatherapp.util.HttpCallbackListener;
import com.example.weatherapp.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by brightleey on 2017/5/6.
 */

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "WeatherActivity";
    //private final static String WEATHER_API_KEY = "c0225d0fd8cd4b2da8e5dae6c9db6c0f";
    private final static String WEATHER_API_KEY = "bc0418b57b2d4918819d3974ac1285d9";
    private DrawerLayout drawerLayout;
    private TextView weaAddr, weaTemp, weaWeather, weaDate, weaAqi;
    private ImageView menuIcon, weaIcon, weaBg;

    private String weatherId;

    //weather_daily
    private RecyclerView weatherDailyRecyclerView;
    private List<DailyItem> dailyData = new ArrayList<>();
    private DailyAdapter dailyAdapter;

    //weather_hourly
    private RecyclerView weatherHourlyRecyclerView;
    private List<HourlyItem> hourlyData = new ArrayList<>();
    private HourlyAdapter hourlyAdapter;

    //weather_index
    private RecyclerView weatherIndexRecyclerView;
    private List<IndexItem> indexData = new ArrayList<>();
    private IndexAdapter indexAdapter;

    //global_config
    private static final int DEFAULT_DISPLAY_DAYS_COUNT = 5;
    private static final int DEFAULT_DISPLAY_HOURS_COUNT = 8;
    private static final int DEFAULT_DISPLAY_INDEX_COUNT = 2;

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

        //init components
        menuIcon = (ImageView)findViewById(R.id.wea_menu_icon);
        weaIcon = (ImageView) findViewById(R.id.wea_icon);
        drawerLayout = (DrawerLayout) findViewById(R.id.wea_drawerlayout);
        weaAddr = (TextView) findViewById(R.id.wea_addr);
        weaTemp = (TextView) findViewById(R.id.wea_temperature);
        weaWeather = (TextView) findViewById(R.id.wea_weather);
        weaDate = (TextView) findViewById(R.id.wea_date);
        weaAqi = (TextView) findViewById(R.id.wea_aqi);
        weaBg = (ImageView) findViewById(R.id.wea_bg);
        //daily
        weatherDailyRecyclerView = (RecyclerView) findViewById(R.id.daily_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, DEFAULT_DISPLAY_DAYS_COUNT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        weatherDailyRecyclerView.setLayoutManager(layoutManager);
        dailyAdapter = new DailyAdapter(dailyData);
//        weatherDailyRecyclerView.addItemDecoration(new ItemSpaceDecoration(25));
        weatherDailyRecyclerView.setAdapter(dailyAdapter);

        //hourly
        weatherHourlyRecyclerView = (RecyclerView) findViewById(R.id.hourly_recyclerview);
        GridLayoutManager horulyLayoutManager = new GridLayoutManager(this, DEFAULT_DISPLAY_HOURS_COUNT);
        weatherHourlyRecyclerView.setLayoutManager(horulyLayoutManager);
        hourlyAdapter = new HourlyAdapter(hourlyData);
        weatherHourlyRecyclerView.setAdapter(hourlyAdapter);

        //index
        weatherIndexRecyclerView = (RecyclerView) findViewById(R.id.index_recyclerview);
        GridLayoutManager indexLayoutManager = new GridLayoutManager(this, DEFAULT_DISPLAY_INDEX_COUNT);
        weatherIndexRecyclerView.setLayoutManager(indexLayoutManager);
        indexAdapter = new IndexAdapter(indexData);

        //add listener
        menuIcon.setOnClickListener(this);
        //get para
        Intent intent = getIntent();
        weatherId = intent.getStringExtra("weather_id");
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = spf.getString(weatherId, "");
        if (!TextUtils.isEmpty(weatherString)){
            Weather weather = handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else {
            requestWeather(weatherId);
        }
        String weatherBg = spf.getString("weather_bg", "");
        if (!TextUtils.isEmpty(weatherBg)){
            Glide.with(this).load(weatherBg).centerCrop().into(weaBg);
        }else {
            loadBackgroundImage();
        }
    }

    private void showWeatherInfo(Weather weather) {
        if (weather != null){

            int weaIconId = getResources().getIdentifier("wea_"+weather.now.weatherInfo.weatherCode, "drawable", getPackageName());
            weaAddr.setText(weather.basic.cityName);
            weaIcon.setImageResource(weaIconId);
            weaTemp.setText(weather.now.wendu);
            weaDate.setText(new SimpleDateFormat("M月dd日 E", Locale.SIMPLIFIED_CHINESE).format(new Date()));
            weaWeather.setText(weather.now.weatherInfo.weatherText + " " + weather.now.windInfo.fengxiang);
            weaAqi.setText("空气" + weather.aqi.airAquality.aqitxt + "  PM2.5 " + weather.aqi.airAquality.pm25);

            //daily
            SimpleDateFormat dailyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dailyDateFormat2 = new SimpleDateFormat("E", Locale.CHINA);
            String today = dailyDateFormat2.format(new Date());
            int counter =0;
            for(Daily daily : weather.dailyList){
                int weaImageId = getResources().getIdentifier("wea_" + daily.weatherInfo.codeDay, "drawable", getPackageName());
                Date dailyDate = null;
                try {
                    dailyDate = dailyDateFormat.parse(daily.date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String dailyDateLocal = dailyDateFormat2.format(dailyDate);
                if (dailyDateLocal.equals(today)){
                    dailyDateLocal = "今天";
                }
                DailyItem dailyItem = new DailyItem(daily.temperature.max, daily.temperature.min, dailyDateLocal, weaImageId, daily.weatherInfo.txtDay);
                dailyData.add(dailyItem);
                counter ++;
                if (counter >= DEFAULT_DISPLAY_DAYS_COUNT)break;;
            }
            if (dailyData.size() > 0){
                dailyAdapter.notifyDataSetChanged();
            }
            //hourly
            SimpleDateFormat hourlyDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat hourlyDateFormat2 = new SimpleDateFormat("HH:mm");
            for (Hourly hourly : weather.hourlyList){
                int hourlyImageId = getResources().getIdentifier("wea_" + hourly.weatherInfo.code, "drawable", getPackageName());
                Date hourlyDate = null;
                try {
                    hourlyDate = hourlyDateFormat.parse(hourly.time);
                }catch (ParseException e){
                    e.printStackTrace();
                }
                HourlyItem hourlyItem= new HourlyItem(hourlyDateFormat2.format(hourlyDate), hourlyImageId, hourly.temperature, hourly.weatherInfo.txt);
                hourlyData.add(hourlyItem);
            }
            if (hourlyData.size() > 0){
                hourlyAdapter.notifyDataSetChanged();
            }
            //index
//            for (Suggestion suggestion : weather.suggestion.)
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wea_menu_icon:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }

    private void loadBackgroundImage(){
        final String baseURL = "http://cn.bing.com";
        HttpUtil.httpRequest(baseURL + "/HPImageArchive.aspx?idx=0&n=1", new HttpCallbackListener() {
            @Override
            public void onSuccess(String responseText) {
                if (!TextUtils.isEmpty(responseText)){
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document document = builder.parse(new InputSource(new StringReader(responseText)));
                        Element root = document.getDocumentElement();

                        NodeList nodeList = root.getElementsByTagName("image");
                        Node node = nodeList.item(0);
                        //Log.d(TAG, String.valueOf(node.getChildNodes().item(3).getTextContent()));

                        final String bgPath = baseURL + node.getChildNodes().item(3).getTextContent();
                        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                        SharedPreferences.Editor editor = spf.edit();
                        editor.putString("weather_bg", bgPath);
                        editor.apply();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(WeatherActivity.this).load(bgPath).centerCrop().into(weaBg);
                            }
                        });
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        Toast.makeText(WeatherActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private Weather handleWeatherResponse(String weatherString){
        if (!TextUtils.isEmpty(weatherString)){
            JsonObject jsonObject = new JsonParser().parse(weatherString).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("HeWeather5").getAsJsonArray();
            String weatherInfo = jsonArray.get(0).toString();
            Weather weather = new Gson().fromJson(weatherInfo, Weather.class);
            return weather;
        }
        return null;
    }


    private void requestWeather(final String weatherId){
        if (!TextUtils.isEmpty(weatherId)) {
            HttpUtil.httpRequest("https://free-api.heweather.com/v5/weather?city=" + weatherId + "&key=" + WEATHER_API_KEY, new HttpCallbackListener() {
                @Override
                public void onSuccess(final String responseText) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putString("weather_id", weatherId);
                    editor.putString(weatherId, responseText);
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Weather weather = handleWeatherResponse(responseText);
                            showWeatherInfo(weather);
                        }
                    });
                }

                @Override
                public void onFail(Exception e) {
                    Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
