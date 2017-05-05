package com.example.weatherapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/24.
 */

public class WeatherDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    private final static String CREATE_PROVINCE = "create table province("
            +"id integer primary key autoincrement, "
            +"province_name text, "
            +"province_code integer)";
    private final static String CREATE_CITY = "create table city("
            +"id integer primary key autoincrement, "
            +"city_name text, "
            +"city_code integer, "
            +"province_id integer"
            +")";
    private final static String CREATE_COUNTY = "create table county("
            +"id integer primary key autoincrement, "
            +"county_name text, "
            +"county_code integer, "
            +"city_id integer," +
            "weather_id text"
            +")";
    public WeatherDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
        db.execSQL("drop table if exists province");
        db.execSQL("drop table if exists city");
        db.execSQL("drop table if exists county");
        onCreate(db);
    }
}
