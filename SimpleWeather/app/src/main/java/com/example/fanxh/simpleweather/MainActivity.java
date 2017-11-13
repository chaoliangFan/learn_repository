package com.example.fanxh.simpleweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.fanxh.simpleweather.db.SWDatabase;

public class MainActivity extends AppCompatActivity {
    private SWDatabase swDatabase;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swDatabase = new SWDatabase(this, "SimpleWeather.db", null, 2);
        db = swDatabase.getWritableDatabase();
        Cursor cursor = db.query("Information",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
