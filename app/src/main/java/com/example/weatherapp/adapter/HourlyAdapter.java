package com.example.weatherapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.example.weatherapp.gson.Hourly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.WeatherViewHolder> {

    private List<HourlyItem> dataList = new ArrayList<>();

    public HourlyAdapter(List<HourlyItem> data) {
        super();
        dataList = data;
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder{
        TextView weaHourlyTime,weaHourlyTemp, weaHourlyTxt;
        ImageView weaHourlyImage;
        public WeatherViewHolder(View view){
            super(view);
            weaHourlyTime = (TextView) view.findViewById(R.id.wea_hourly_time);
            weaHourlyTemp = (TextView) view.findViewById(R.id.wea_hourly_temp);
            weaHourlyTxt = (TextView) view.findViewById(R.id.wea_hourly_txt);
            weaHourlyImage = (ImageView) view.findViewById(R.id.wea_hourly_image);
        }
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_item, parent, false);
        WeatherViewHolder viewHolder = new WeatherViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        HourlyItem hourlyItem = dataList.get(position);
        holder.weaHourlyImage.setImageResource(hourlyItem.getWeaImgId());
        holder.weaHourlyTime.setText(hourlyItem.getWeaTime());
        holder.weaHourlyTemp.setText(hourlyItem.getWeaTemp() + "℃");
        holder.weaHourlyTxt.setText(hourlyItem.getWeaTxt());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
