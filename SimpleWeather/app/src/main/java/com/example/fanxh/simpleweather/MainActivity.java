package com.example.fanxh.simpleweather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.fanxh.simpleweather.db.DbUtil;
import com.example.fanxh.simpleweather.db.SWDatabase;

public class MainActivity extends AppCompatActivity {
    private static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = DbUtil.getDb(this);
        try {
            Cursor cursor = db.query("Information", null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    Intent intent = new Intent(this, WeatherActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
