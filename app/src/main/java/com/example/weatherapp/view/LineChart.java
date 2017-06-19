package com.example.weatherapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.weatherapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by brightleey on 2017/5/29.
 */

public class LineChart extends View {
    private static final String TAG = "LineChart";

    //要展示的数据
    private List<HashMap<String, String>> dataList = new ArrayList<>();
    //xy轴刻度文字
    private String[] xAxisPointsTxt = new String[]{}, yAxisPointsTxt = new String[]{};
    //xy轴刻度xy坐标集合
    private HashMap<String, Integer> xCoordinate, yCoordinate;
    //xy轴刻度间隔
    private int xInterval, yInterval;
    //xy轴文字大小
    private int axisTextSize;
    //xy轴文字颜色
    private String axisTextColor = "#ffffff";
    //x轴图标
    private int[] xAxisIcon = new int[]{};
    //x轴图标与文字间隔
    private int intervalIconText;
    //x轴图标尺寸
    private int xAxisIconWidth;
    //显示y轴
    private boolean showYAxis = true;
    //圆点半径
    private int circlePointRadius;
    //圆点与标注的间隔
    private int intervalCircleAndFlag;
    //x轴和y轴文字垂直间距
    int intervalBetweenXY;
    //paint
    private Paint axisPaint, linePaint;
    //default linepaint color
    private static final String DEFAULT_LINE_PAINT_COLOR= "#ffffff";
    //linepaint color array
    private String[] linePaintColor = new String[]{DEFAULT_LINE_PAINT_COLOR};
    //view高宽
    private int mWidth, mHeight;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*获取自定义属性*/
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineChart, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.LineChart_textSize:
                    axisTextSize = a.getDimensionPixelOffset(attr, 16);
                    break;
                case R.styleable.LineChart_circleRadius:
                    circlePointRadius = a.getDimensionPixelOffset(attr, 4);
                    break;
                case R.styleable.LineChart_showYAxis:
                    showYAxis = a.getBoolean(attr, true);
                    break;
                case R.styleable.LineChart_axisTextColor:
                    axisTextColor = a.getString(attr);
                    break;
            }
        }
        a.recycle();
        intervalIconText = intervalBetweenXY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                5, context.getResources().getDisplayMetrics());
        intervalCircleAndFlag = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                5, context.getResources().getDisplayMetrics());
        xAxisIconWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                30, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY){
            //match_parent or exactly value
            mWidth = widthSize;
        }else {
            //wrap_content

        }

        if (heightMode == MeasureSpec.EXACTLY){
            //match_parent or exactly value
            mHeight = heightSize;
        }else{

        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Rect rect = new Rect();

        Paint axisHelperLinePaint = new Paint();
        axisHelperLinePaint.setColor(Color.parseColor("#ffffff"));
        axisHelperLinePaint.setAlpha(50);

        axisPaint = new Paint();
        axisPaint.setColor(Color.parseColor(axisTextColor));
        axisPaint.setTextSize(axisTextSize);
        axisPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor(DEFAULT_LINE_PAINT_COLOR));
        linePaint.setTextSize(axisTextSize);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setAntiAlias(true);

        if (xAxisIcon.length == 0){
            xAxisIconWidth = 0;
            intervalIconText = 0;
        }

        //y 轴
        int yLen = yAxisPointsTxt.length;
        //measure text
        axisPaint.getTextBounds(yAxisPointsTxt[0], 0 , yAxisPointsTxt[0].length(), rect);   //return 39 15
        int yAxisTextWidth = showYAxis ? rect.width() : 0;
        int yAxisTextWidth2 = rect.width();
        int yAxisTextHeight = rect.height();
        int yAxisTextOffset = xAxisIconWidth + intervalIconText;

        //为了让y轴顶部文字和折线最大值标注可以正常显示，第一个刻度的起始位置需要空出两个y轴文字的高度
        yInterval = (mHeight - yAxisTextHeight*2 - yAxisTextOffset - intervalBetweenXY - intervalCircleAndFlag) / yLen;
        yCoordinate = new HashMap<>();
        for (int i = 0; i < yLen; i++){
            if (showYAxis) {
                canvas.drawText(yAxisPointsTxt[i], 0, (int) (i * yInterval + yAxisTextHeight*2), axisPaint);
            }
//            canvas.drawLine(0, (int)(i * yInterval + rect.height() / 2), mWidth,
//                    (int)(i * yInterval + rect.height() / 2), axisHelperLinePaint);
            yCoordinate.put(yAxisPointsTxt[i], (int)(i * yInterval + yAxisTextHeight + intervalCircleAndFlag));
        }

        //x轴
        int xLen = xAxisPointsTxt.length;
        //以第一个刻度文字宽度为准
        int xAxisTextWidth = (int) axisPaint.measureText(xAxisPointsTxt[0]);
        int xAxisTextOffset = showYAxis ? 10 : 0;
        xInterval = (mWidth - yAxisTextWidth) / xLen;
        xCoordinate = new HashMap<>();

//        Log.d(TAG, xAxisIconWidth + "");
        for (int i = 0; i < xLen; i++){
            //在间隔内居中对齐
            int xTxtCoordinate = (int)(i * xInterval + yAxisTextWidth + xAxisTextOffset + xInterval/2 - xAxisTextWidth/2);
            int yTxtCoordinate = (int)mHeight - xAxisIconWidth - intervalIconText;
            canvas.drawText(xAxisPointsTxt[i], xTxtCoordinate, yTxtCoordinate, axisPaint);
//            canvas.drawLine((int)(i * xInterval + rect.width() + xAxisTextWidth / 2), 0,
//                    (int)(i * xInterval + rect.width() + xAxisTextWidth / 2),
//                    mHeight, axisHelperLinePaint);
            if (xAxisIcon.length > i){
                //drawbitmap
                int xBitmapCoordinate = (int)(xTxtCoordinate + xAxisTextWidth / 2 - xAxisIconWidth / 2);
                int yBitmapCoordinate = (int)(yTxtCoordinate + intervalIconText);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), xAxisIcon[i]);
                Rect dst = new Rect(xBitmapCoordinate, yBitmapCoordinate, xBitmapCoordinate +
                        xAxisIconWidth, yBitmapCoordinate + xAxisIconWidth);
                canvas.drawBitmap(bitmap, null, dst, null);
                bitmap.recycle();
            }
            //draw vertical separator line
            if(i > 0){
                int nextX = (int)(i*xInterval - yAxisTextWidth - xAxisTextOffset);
                canvas.drawLine(nextX, 0 ,nextX, mHeight, axisHelperLinePaint);
            }
            xCoordinate.put(xAxisPointsTxt[i], xTxtCoordinate);
        }

        int counter = 0;
        for (HashMap<String, String> hashMap : dataList){
            if (linePaintColor.length > counter) linePaint.setColor(Color.parseColor(linePaintColor[counter]));
            for (int i = 0; i < xLen; i++){
                //Log.d(TAG, hashMap.get(xAxisPointsTxt[i]));
                //draw line
                if (i > 0){
                    canvas.drawLine(xCoordinate.get(xAxisPointsTxt[i-1]) + xAxisTextWidth / 2,
                            yCoordinate.get(hashMap.get(xAxisPointsTxt[i-1])) + rect.height() / 2,
                            xCoordinate.get(xAxisPointsTxt[i]) + xAxisTextWidth / 2,
                            yCoordinate.get(hashMap.get(xAxisPointsTxt[i])) + rect.height() / 2, linePaint);
                }
                //draw circle
                canvas.drawCircle(xCoordinate.get(xAxisPointsTxt[i]) + xAxisTextWidth / 2,
                        yCoordinate.get(hashMap.get(xAxisPointsTxt[i]))
                                + rect.height() / 2, circlePointRadius, linePaint);

                //draw txt
                canvas.drawText(hashMap.get(xAxisPointsTxt[i]), xCoordinate.get(xAxisPointsTxt[i]) +
                                xAxisTextWidth / 2 - yAxisTextWidth2 /2,
                        yCoordinate.get(hashMap.get(xAxisPointsTxt[i])) - intervalCircleAndFlag, linePaint);
            }
            counter ++;
        }
}

    public void setxAxisPointsTxt(String[] xAxisText){
        xAxisPointsTxt = xAxisText;
    }
    public void setyAxisPointsTxt(String[] yAxisText){
        yAxisPointsTxt = yAxisText;
    }
    public void setDataList(List<HashMap<String, String>> data){
        dataList = data;
    }
    public void setAxisTextColor(String colorVal){
        axisTextColor = colorVal;
    }
    public void setAxisTextSize(int textSize){
        axisTextSize = textSize;
    }
    public void setLinePaintColor(String[] paintColorVal){
        linePaintColor = paintColorVal;
    }
    public void setxAxisIcon(int[] iconIdArray){
        xAxisIcon = iconIdArray;
    }
    public void setShowYAxis(boolean showyaxis){
        showYAxis = showyaxis;
    }
}
