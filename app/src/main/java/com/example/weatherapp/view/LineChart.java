package com.example.weatherapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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
    private int axisTextSize = 16;
    //xy轴文字颜色
    private String axisTextColor = "#cccccc";
    //x轴图标
    private int[] xAxisIcon = new int[]{};
    //x轴图标与文字间隔
    private static final int XAXIS_INTERVAL_BETWEEN_ICON_AND_TEXT = 5;
    //x轴图标尺寸
    private int xAxisIconWidth = 35;
    //显示y轴
    private boolean showYAxis = true;
    //圆点半径
    private static final int CIRCLE_POINT_RADIUS = 4;
    //线条宽度
    private static final float LINE_WIDTH = 1.0f;
    //paint
    private Paint axisPaint, linePaint;
    //default linepaint color
    private static final String DEFAULT_LINE_PAINT_COLOR= "#97d9ff";
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
        axisHelperLinePaint.setColor(Color.parseColor("#eeeeee"));

        axisPaint = new Paint();
        axisPaint.setColor(Color.parseColor(axisTextColor));
        axisPaint.setTextSize(axisTextSize);

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor(DEFAULT_LINE_PAINT_COLOR));
        linePaint.setTextSize(axisTextSize);
        linePaint.setStrokeWidth(LINE_WIDTH);
        linePaint.setAntiAlias(true);

        //y 轴
        int yLen = yAxisPointsTxt.length;
        //measure text
        axisPaint.getTextBounds(yAxisPointsTxt[0], 0 , yAxisPointsTxt[0].length(), rect);   //return 39 15
        int yAxisTextWidth = showYAxis ? rect.width() : 0;
        int yAxisTextHeight = rect.height();
        yInterval = (mHeight - yAxisTextHeight) / yLen;
        yCoordinate = new HashMap<>();
        for (int i = 0; i < yLen; i++){
            if (showYAxis) {
                canvas.drawText(yAxisPointsTxt[i], 0, (int) (i * yInterval + yAxisTextHeight), axisPaint);
            }
//            canvas.drawLine(0, (int)(i * yInterval + rect.height() / 2), mWidth,
//                    (int)(i * yInterval + rect.height() / 2), axisHelperLinePaint);
            yCoordinate.put(yAxisPointsTxt[i], (int)(i * yInterval));
        }

        //x轴
        int xLen = xAxisPointsTxt.length;
        int xAxisTextWidth = (int) axisPaint.measureText(xAxisPointsTxt[0]);
        int xAxisTextOffset = showYAxis ? 10 : 0;
        xInterval = (mWidth - yAxisTextWidth) / xLen;
        xCoordinate = new HashMap<>();
        if (xAxisIcon.length == 0){
            xAxisIconWidth = 0;
        }
//        Log.d(TAG, xAxisIconWidth + "");
        for (int i = 0; i < xLen; i++){
            int xTxtCoordinate = (int)(i * xInterval + yAxisTextWidth + xAxisTextOffset);
            int yTxtCoordinate = (int)mHeight - xAxisIconWidth;
            canvas.drawText(xAxisPointsTxt[i], xTxtCoordinate, yTxtCoordinate, axisPaint);
//            canvas.drawLine((int)(i * xInterval + rect.width() + xAxisTextWidth / 2), 0,
//                    (int)(i * xInterval + rect.width() + xAxisTextWidth / 2),
//                    mHeight, axisHelperLinePaint);
            for (int a = 0; a < xAxisIcon.length; a++){
                //drawbitmap
                int xBitmapCoordinate = (int)(xTxtCoordinate + xAxisTextWidth / 2 - xAxisIconWidth / 2);
                int yBitmapCoordinate = (int)(mHeight - xAxisIconWidth + XAXIS_INTERVAL_BETWEEN_ICON_AND_TEXT);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), xAxisIcon[a]);
                Rect dst = new Rect(xBitmapCoordinate, yBitmapCoordinate, xBitmapCoordinate + xAxisIconWidth, yBitmapCoordinate + xAxisIconWidth);
                canvas.drawBitmap(bitmap, null, dst, null);
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
                                + rect.height() / 2, CIRCLE_POINT_RADIUS, linePaint);

                //draw txt
                canvas.drawText(hashMap.get(xAxisPointsTxt[i]), xCoordinate.get(xAxisPointsTxt[i]),
                        yCoordinate.get(hashMap.get(xAxisPointsTxt[i])), linePaint);
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
