package com.example.weatherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.db.City;
import com.example.weatherapp.db.County;
import com.example.weatherapp.db.Province;
import com.example.weatherapp.db.WeatherDB;
import com.example.weatherapp.util.HttpCallbackListener;
import com.example.weatherapp.util.HttpUtil;
import com.example.weatherapp.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class ChooseAreaFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "ChooseAreaFragment";
    private Button backBtn;

    private TextView areaName;

    private ListView areaList;

    private final static int LEVEL_PROVINCE = 0;

    private final static int LEVEL_CITY = 1;

    private final static int LEVEL_COUNTY =2;

    private int currentLevel = 0;

    private List<String> areaDataList = new ArrayList<>();

    private List<Province> provinceList;

    private List<City> cityList;

    private List<County> countyList;

    private Province selectedProvince;

    private City selectedCity;

    private County selectedCounty;

    private ArrayAdapter<String> arrayAdapter;

    private Context mContext;

    private WeatherDB weatherDB;
    private ProgressDialog progressDialog;

    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        mContext = getContext();
        weatherDB = WeatherDB.getInstance(mContext);

        backBtn = (Button)view.findViewById(R.id.back_btn);
        areaName = (TextView)view.findViewById(R.id.area_name);
        areaList = (ListView)view.findViewById(R.id.area_list);
        arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, areaDataList);
        areaList.setAdapter(arrayAdapter);
        backBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                if (currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        areaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince =  provinceList.get(position);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if (currentLevel == LEVEL_COUNTY){
                    County county = countyList.get(position);
                    Intent intent = new Intent(mContext, WeatherActivity.class);
                    intent.putExtra("weather_id", county.getWeatherId());
                    mContext.startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        areaName.setText("China");
        backBtn.setVisibility(View.GONE);
        provinceList = weatherDB.loadProvince();
        if (provinceList.size() > 0){
            areaDataList.clear();
            for (Province province : provinceList){
                areaDataList.add(province.getProvinceName());
            }
            arrayAdapter.notifyDataSetChanged();
            areaList.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            queryFromServer("http://guolin.tech/api/china", LEVEL_PROVINCE);
        }

    }

    private void queryCities(){
        areaName.setText(selectedProvince.getProvinceName());
        backBtn.setVisibility(View.VISIBLE);
        cityList = weatherDB.loadCity(selectedProvince.getId());
        if (cityList.size() > 0){
            areaDataList.clear();
            for (City city : cityList){
                areaDataList.add(city.getCityName());
            }
            arrayAdapter.notifyDataSetChanged();
            areaList.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            queryFromServer("http://guolin.tech/api/china/"+selectedProvince.getProvinceCode(), LEVEL_CITY);
        }
    }

    private void queryCounties(){
        areaName.setText(selectedCity.getCityName());
        backBtn.setVisibility(View.VISIBLE);
        countyList = weatherDB.loadCounty(selectedCity.getId());
        if (countyList.size() > 0){
            areaDataList.clear();
            for (County county : countyList){
                areaDataList.add(county.getCountyName());
            }
            arrayAdapter.notifyDataSetChanged();
            areaList.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else{
            queryFromServer("http://guolin.tech/api/china/"+selectedProvince.getProvinceCode()+"/"+selectedCity.getCityCode(), LEVEL_COUNTY);
        }
    }

    private void queryFromServer(String url, final int level) {
        showProgressDialog();
        HttpUtil.httpRequest(url, new HttpCallbackListener() {
            @Override
            public void onSuccess(final String responseText) {

                Boolean result = false;
                if (level == LEVEL_PROVINCE){
                    result = Utility.handleProvinceResponse(responseText, weatherDB);
                }else if (level == LEVEL_CITY){
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId(), weatherDB);
                }else if (level == LEVEL_COUNTY){
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId(), weatherDB);
                }

                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            areaName.setText(responseText);
                            if (level == LEVEL_PROVINCE){
                                queryProvinces();
                            }else if (level == LEVEL_CITY){
                                queryCities();
                            }else if (level == LEVEL_COUNTY){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFail(final Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Log.d(TAG, e.getMessage());
                        Toast.makeText(mContext, "加载失败:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
