package com.example.weatherapp.adapter;

import android.media.Image;
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
 * Created by brightleey on 2017/5/21.
 */

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder> {
    private List<IndexItem> dataList = new ArrayList<>();
    public IndexAdapter(List<IndexItem> dataList) {
        super();
        this.dataList = dataList;
    }

    static class IndexViewHolder extends RecyclerView.ViewHolder{
        TextView weaIndexTxt;
        ImageView weaIndexImage;
        public IndexViewHolder(View view){
            super(view);
            weaIndexTxt = (TextView) view.findViewById(R.id.wea_index_txt);
            weaIndexImage = (ImageView) view.findViewById(R.id.wea_index_image);
        }
    }

    @Override
    public IndexViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.index_item, viewGroup, false);
        IndexViewHolder indexViewHolder = new IndexViewHolder(view);
        return indexViewHolder;
    }

    @Override
    public void onBindViewHolder(IndexViewHolder viewHolder, int i) {
        IndexItem indexItem = dataList.get(i);
        viewHolder.weaIndexImage.setImageResource(indexItem.getImageId());
        viewHolder.weaIndexTxt.setText(indexItem.getTxt());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
