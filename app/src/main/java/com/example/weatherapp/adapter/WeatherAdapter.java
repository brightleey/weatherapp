package com.example.weatherapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<WeatherItem> dataList = new ArrayList<>();

    public WeatherAdapter(List<WeatherItem> data) {
        super();
        dataList = data;
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder{
        TextView weaMax,weaMin,weaDate;
        ImageView weaImage;
        public WeatherViewHolder(View view){
            super(view);
            weaMax = (TextView) view.findViewById(R.id.wea_max);
            weaMin = (TextView) view.findViewById(R.id.wea_min);
            weaDate = (TextView) view.findViewById(R.id.wea_time);
            weaImage = (ImageView) view.findViewById(R.id.wea_image);
        }
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        WeatherViewHolder viewHolder = new WeatherViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        WeatherItem weatherItem = dataList.get(position);
        holder.weaImage.setImageResource(weatherItem.getWeaImgId());
        holder.weaMax.setText(weatherItem.getWeaMax());
        holder.weaMin.setText(weatherItem.getWeaMin());
        holder.weaDate.setText(weatherItem.getWeaDate());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
