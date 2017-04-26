package com.example.weatherapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/24.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    private final static String CREATE_PROVINCE = "create table province("
            +"id integer primary key autoincrement, "
            +"provinceNmae text, "
            +"provinceCode text)";
    private final static String CREATE_CITY = "create table city("
            +"id integer primary key autoincrement, "
            +"cityName text, "
            +"cityCode text, "
            +"provinceId integer"
            +")";
    private final static String CREATE_COUNTY = "create table county("
            +"id integer primary key autoincrement, "
            +"countyName text, "
            +"countyCode text, "
            +"cityId integer"
            +")";
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
        Toast.makeText(mContext, "create database successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
}
