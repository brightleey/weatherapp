package com.example.weatherapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;

import java.util.List;

/**
 * Created by brightleey on 2017/6/16.
 */

public class IndexAdapter2 extends ArrayAdapter {
    private List<IndexItem> indexItems;
    private Context mContext;
    private int mResource;

    static class ViewHolder{
        TextView indexName;
        ImageView indexImage;
        TextView indexTxt;
    }

    public IndexAdapter2(@NonNull Context context, @LayoutRes int resource, @NonNull List<IndexItem> objects) {
        super(context, resource, objects);
        indexItems = objects;
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        IndexItem indexItem = indexItems.get(position);
        ViewHolder viewHolder;
        if ( null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.indexImage = (ImageView) convertView.findViewById(R.id.wea_index_image);
            viewHolder.indexName = (TextView) convertView.findViewById(R.id.wea_index_name);
            viewHolder.indexTxt = (TextView) convertView.findViewById(R.id.wea_index_txt);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.indexImage.setImageResource(indexItem.getImageId());
        viewHolder.indexName.setText(indexItem.getIndexName());
        viewHolder.indexTxt.setText(indexItem.getTxt());

        return convertView;
    }
}
