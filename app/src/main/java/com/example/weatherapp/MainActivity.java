package com.example.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.weatherapp.db.MyDatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabaseHelper = new MyDatabaseHelper(this, "weatherapp.db", null, 1);
        myDatabaseHelper.getWritableDatabase();
    }
}
