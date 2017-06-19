package com.example.weatherapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weatherapp.adapter.IndexAdapter2;
import com.example.weatherapp.adapter.IndexItem;
import com.example.weatherapp.gson.Daily;
import com.example.weatherapp.gson.Hourly;
import com.example.weatherapp.gson.Weather;
import com.example.weatherapp.util.HttpCallbackListener;
import com.example.weatherapp.util.HttpUtil;
import com.example.weatherapp.view.BorderGridView;
import com.example.weatherapp.view.LineChart;
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
import java.util.HashMap;
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
    private FrameLayout rootLayout;
    private DrawerLayout drawerLayout;
    private TextView weaAddr, weaTemp, weaWeather, weaDate, weaAqi;
    private ImageView menuIcon, weaIcon, weaBg;
    private String weatherId;
    private LineChart dailyLineChart, hourlyLineChart;

    private TextView btnCity, btnAdd, btnMore;

    //weather_index
    private BorderGridView indexGridView;
    private List<IndexItem> indexData = new ArrayList<>();
    private IndexAdapter2 indexAdapter2;

    //global_config
    private static final int DEFAULT_DISPLAY_INDEX_COUNT = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //5.0API21透明化任务栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //隐藏状态栏
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //4.4API19半透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Log.d(TAG, "设备型号：" + Build.MODEL + "，设备SDK版本： " + Build.VERSION.SDK_INT + "，设备系统版本：" + Build.VERSION.RELEASE);
        setContentView(R.layout.activity_weather);

        initViews();
        initEvents();
        initDatas();

    }

    private void initViews(){
        rootLayout = (FrameLayout) findViewById(R.id.root_layout);
        menuIcon = (ImageView)findViewById(R.id.wea_menu_icon);
        weaIcon = (ImageView) findViewById(R.id.wea_icon);
        drawerLayout = (DrawerLayout) findViewById(R.id.wea_drawerlayout);
        weaAddr = (TextView) findViewById(R.id.wea_addr);
        weaTemp = (TextView) findViewById(R.id.wea_temperature);
        weaWeather = (TextView) findViewById(R.id.wea_weather);
        weaDate = (TextView) findViewById(R.id.wea_date);
        weaAqi = (TextView) findViewById(R.id.wea_aqi);
        weaBg = (ImageView) findViewById(R.id.wea_bg);

        btnAdd = (TextView) findViewById(R.id.btn_add);
        btnCity = (TextView) findViewById(R.id.btn_city);
        btnMore = (TextView) findViewById(R.id.btn_more);

        dailyLineChart = (LineChart) findViewById(R.id.daily_linechart);
        hourlyLineChart = (LineChart) findViewById(R.id.hourly_linechart);
        indexGridView = (BorderGridView) findViewById(R.id.grid_view);

        indexAdapter2 = new IndexAdapter2(this, R.layout.index_item, indexData);
        indexGridView.setAdapter(indexAdapter2);
        indexGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //整屏滚动时需要添加paddingTop
//        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        rootLayout.setPadding(0, getResources().getDimensionPixelSize(resId), 0, 0);
    }

    private void initEvents(){
        menuIcon.setOnClickListener(this);
        btnMore.setOnClickListener(this);
    }

    private void initDatas(){
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
//            Glide.with(this).load(weatherBg).centerCrop().into(weaBg);
        }else {
//            loadBackgroundImage();
        }
    }

    private void showWeatherInfo(Weather weather) {
        if (weather != null){

            int weaIconId = getResources().getIdentifier("wea_"
                    + weather.now.weatherInfo.weatherCode, "drawable", getPackageName());
            weaAddr.setText(weather.basic.cityName);
            weaIcon.setImageResource(weaIconId);
            weaTemp.setText(weather.now.wendu);
            weaDate.setText(new SimpleDateFormat("M月dd日 E",
                    Locale.SIMPLIFIED_CHINESE).format(new Date()));
            weaWeather.setText(weather.now.weatherInfo.weatherText + " "
                    + weather.now.windInfo.fengxiang);
            weaAqi.setText("PM2.5 " + weather.aqi.airAquality.pm25 + " (" + weather.aqi.airAquality.aqitxt + ")");

            //daily
            int maxTemp = 0, minTemp = 0;
            String[] yAxisTxt, xAxisTxt = new String[weather.dailyList.size()];
            int[] xAxisIcon = new int[weather.dailyList.size()];
            List<HashMap<String, String>> dailyWeatherData = new ArrayList<>();
            HashMap<String, String> maxTempHashMap = new HashMap<>();
            HashMap<String, String> minTempHashMap = new HashMap<>();


            SimpleDateFormat dailyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dailyDateFormat2 = new SimpleDateFormat("E", Locale.CHINA);
            String today = dailyDateFormat2.format(new Date());
            int counter =0;
            for(Daily daily : weather.dailyList){

                if (counter == 0){
                    maxTemp = Integer.parseInt(daily.temperature.max);
                    minTemp = Integer.parseInt(daily.temperature.min);
                }
                maxTemp = Math.max(Integer.parseInt(daily.temperature.max), maxTemp);
                minTemp = Math.min(Integer.parseInt(daily.temperature.min), minTemp);

                int weaImageId = getResources().getIdentifier("wea_"
                        + daily.weatherInfo.codeDay, "drawable", getPackageName());
                Date dailyDate = null;
                try {
                    dailyDate = dailyDateFormat.parse(daily.date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String dailyDateLocal = dailyDateFormat2.format(dailyDate);
                if (dailyDateLocal.equals(today)){
                    dailyDateLocal = "今天";
                }else {
                    dailyDateLocal = dailyDateLocal.replace("星期", "周");
                }

                maxTempHashMap.put(dailyDateLocal, daily.temperature.max + "°");
                minTempHashMap.put(dailyDateLocal, daily.temperature.min + "°");

                xAxisIcon[counter] = weaImageId;
                xAxisTxt[counter] = dailyDateLocal;

                counter ++;
                //if (counter >= DEFAULT_DISPLAY_DAYS_COUNT)break;;
            }
            dailyWeatherData.add(maxTempHashMap);
            dailyWeatherData.add(minTempHashMap);
            yAxisTxt = new String[maxTemp - minTemp + 1];
            int j = 0;
            for (int i = maxTemp; i >= minTemp; i--){
                yAxisTxt[j] = i + "°";
                j ++;
            }
//            dailyLineChart.setAxisTextSize(30);
            dailyLineChart.setxAxisPointsTxt(xAxisTxt);
            dailyLineChart.setyAxisPointsTxt(yAxisTxt);
            dailyLineChart.setxAxisIcon(xAxisIcon);
            dailyLineChart.setLinePaintColor(new String[]{"#ffffff", "#c7f9ff"});
            dailyLineChart.setDataList(dailyWeatherData);

            //hourly
            counter = 0;
            String[] yAxisTxt2, xAxisTxt2 = new String[weather.hourlyList.size()];
            int[] xAxisIcon2 = new int[weather.hourlyList.size()];
            List<HashMap<String, String>> hourlyWeatherData = new ArrayList<>();
            HashMap<String, String> hourlyData = new HashMap<>();
            int maxTemp2 = 0, minTemp2 = 0;

            SimpleDateFormat hourlyDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat hourlyDateFormat2 = new SimpleDateFormat("HH:mm");
            for (Hourly hourly : weather.hourlyList){
                if (counter == 0){
                    minTemp2 = maxTemp2= Integer.parseInt(hourly.temperature);
                }
                maxTemp2 = Math.max(maxTemp2, Integer.parseInt(hourly.temperature));
                minTemp2 = Math.min(minTemp2, Integer.parseInt(hourly.temperature));

                int hourlyImageId = getResources().getIdentifier("wea_" +
                        hourly.weatherInfo.code, "drawable", getPackageName());
                Date hourlyDate = null;
                try {
                    hourlyDate = hourlyDateFormat.parse(hourly.time);
                }catch (ParseException e){
                    e.printStackTrace();
                }
                String hourlyDateLocal = hourlyDateFormat2.format(hourlyDate);
                xAxisIcon2[counter] = hourlyImageId;
                xAxisTxt2[counter] = hourlyDateLocal;
                hourlyData.put(hourlyDateLocal, hourly.temperature + "°");
                counter ++;
            }
            hourlyWeatherData.add(hourlyData);
            yAxisTxt2 = new String[maxTemp2 - minTemp2 + 1];
            j = 0;
            for (int i = maxTemp2; i >= minTemp2; i--){
                yAxisTxt2[j] = i + "°";
                j ++;
            }
            hourlyLineChart.setxAxisPointsTxt(xAxisTxt2);
            hourlyLineChart.setyAxisPointsTxt(yAxisTxt2);
            hourlyLineChart.setxAxisIcon(xAxisIcon2);
            hourlyLineChart.setDataList(hourlyWeatherData);

            //index
            IndexItem comfortIndex = new IndexItem("舒适度指数",
                    weather.suggestion.comfortIndex.shortDes,
                    weather.suggestion.comfortIndex.detailTxt, R.drawable.indice_ssd);
            IndexItem carWashIndex = new IndexItem("洗车指数",
                    weather.suggestion.carWashIndex.shortDes,
                    weather.suggestion.carWashIndex.detailTxt, R.drawable.indice_xc);
            IndexItem dressIndex = new IndexItem("穿衣指数",
                    weather.suggestion.dressIndex.shortDes,
                    weather.suggestion.dressIndex.detailTxt, R.drawable.indice_cy);
            IndexItem coldIndex = new IndexItem("感冒指数",
                    weather.suggestion.coldIndex.shortDes,
                    weather.suggestion.coldIndex.detailTxt, R.drawable.indice_gm);
            IndexItem sportIndex = new IndexItem("运动指数",
                    weather.suggestion.sportIndex.shortDes,
                    weather.suggestion.sportIndex.detailTxt, R.drawable.indice_yd);
            IndexItem travelIndex = new IndexItem("旅游指数",
                    weather.suggestion.travelIndex.shortDes,
                    weather.suggestion.travelIndex.detailTxt, R.drawable.indice_ls);
            IndexItem uvIndex = new IndexItem("紫外线指数",
                    weather.suggestion.uvIndex.shortDes,
                    weather.suggestion.uvIndex.detailTxt, R.drawable.indice_zwx);
            IndexItem flIndex = new IndexItem("风力指数",
                    weather.now.windInfo.fengsu + " m/s",
                    weather.now.windInfo.fengli, R.drawable.indice_fl);
            indexData.add(comfortIndex);
            indexData.add(carWashIndex);
            indexData.add(dressIndex);
            indexData.add(coldIndex);
            indexData.add(sportIndex);
            indexData.add(travelIndex);
            indexData.add(uvIndex);
            indexData.add(flIndex);
            indexAdapter2.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wea_menu_icon:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_more:
                PopupMenu popupMenu = new PopupMenu(this, v);
                getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(WeatherActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popupMenu.show();
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
            HttpUtil.httpRequest("https://free-api.heweather.com/v5/weather?city="
                    + weatherId + "&key=" + WEATHER_API_KEY, new HttpCallbackListener() {
                @Override
                public void onSuccess(final String responseText) {
                    SharedPreferences.Editor editor = PreferenceManager.
                            getDefaultSharedPreferences(WeatherActivity.this).edit();
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
