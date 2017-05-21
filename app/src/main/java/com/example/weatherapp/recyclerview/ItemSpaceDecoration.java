package com.example.weatherapp.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/5/19.
 */

public class ItemSpaceDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public ItemSpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = 0;
        outRect.left = 0;
        outRect.bottom = 0;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == parent.getChildCount() - 1) {
            outRect.right = 0;
        } else {
            outRect.right = space;
        }
    }
}
