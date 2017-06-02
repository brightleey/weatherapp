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

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.WeatherViewHolder> {

    private List<DailyItem> dataList = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        public void onItemClicked(DailyItem dailyItem, int position);
    }
    private View mView;
    public DailyAdapter(List<DailyItem> data, OnItemClickListener onItemClickListener) {
        super();
        this.dataList = data;
        this.onItemClickListener = onItemClickListener;
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
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_item, parent, false);
        WeatherViewHolder viewHolder = new WeatherViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, final int position) {
        final DailyItem dailyItem = dataList.get(position);
        holder.weaImage.setImageResource(dailyItem.getWeaImgId());
        holder.weaMax.setText(dailyItem.getWeaMin() + "℃/" + dailyItem.getWeaMax() + "℃");
        holder.weaMin.setText(dailyItem.getWeaTxt());
        holder.weaDate.setText(dailyItem.getWeaDate());
        if (onItemClickListener != null){
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(dailyItem, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
