package com.example.weatherapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.example.weatherapp.R;

/**
 * Created by Administrator on 2017/6/13.
 */

public class BorderGridView extends GridView {
    private Paint linePaint;
    private String lineColor = "#cccccc";
    public BorderGridView(Context context) {
        this(context, null);
    }

    public BorderGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BorderGridView, defStyleAttr, 0);
        lineColor = a.getString(R.styleable.BorderGridView_borderColor);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        int childCount = getChildCount();
        int columnCount = getNumColumns();
        int extraCells = childCount % columnCount;
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor(lineColor));
        linePaint.setStyle(Paint.Style.STROKE);
//        linePaint.setAntiAlias(true);

        for (int i = 0; i < childCount; i++){
            View childView = getChildAt(i);
            if ((i + 1) % columnCount == 0 && (i + 1) < childCount){
                canvas.drawLine(childView.getLeft(), childView.getBottom(),
                        childView.getRight(), childView.getBottom(), linePaint);
            }else if((i + 1) > (childCount - extraCells)) {
                canvas.drawLine(childView.getRight(), childView.getTop(),
                        childView.getRight(), childView.getBottom(), linePaint);
            }else if(extraCells == 0 && (i + 1) > (childCount/columnCount - 1)*columnCount){
                if((i + 1) < childCount){
                    canvas.drawLine(childView.getRight(), childView.getTop(),
                            childView.getRight(), childView.getBottom(), linePaint);
                }
            }else{
                canvas.drawLine(childView.getLeft(), childView.getBottom(),
                        childView.getRight(), childView.getBottom(), linePaint);
                canvas.drawLine(childView.getRight(), childView.getTop(),
                        childView.getRight(), childView.getBottom(), linePaint);
            }
        }

    }
}
