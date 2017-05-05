package com.example.weatherapp.util;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface HttpCallbackListener {
    void onSuccess(String responseText);
    void onFail(Exception e);
}
