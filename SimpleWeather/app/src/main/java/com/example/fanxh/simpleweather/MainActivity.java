package com.example.fanxh.simpleweather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.fanxh.simpleweather.db.DbUtil;

import static com.example.fanxh.simpleweather.ChooseAreaFragment.LEVEL_CITY;
import static com.example.fanxh.simpleweather.ChooseAreaFragment.LEVEL_COUNTY;

public class MainActivity extends AppCompatActivity {
    private static SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CopyDB.copy(MainActivity.this);

        setContentView(R.layout.activity_main);
        db = DbUtil.getDb(this);

        try {
            cursor = db.query("Information", null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    Intent intent = new Intent(this, WeatherActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void onBackPressed() {
        ChooseAreaFragment mCAFragment = (ChooseAreaFragment) getFragmentManager().findFragmentById(R.id.choose_area_fragment);
        if (mCAFragment.currentLevel == LEVEL_COUNTY) {
            int i = mCAFragment.dataList.size();
            mCAFragment.queryCities();
            if (mCAFragment.dataList.size() == i ) {
                mCAFragment.queryProvinces();
            }
        } else if (mCAFragment.currentLevel == LEVEL_CITY) {
            mCAFragment.queryProvinces();
        } else {
            super.onBackPressed();
        }
    }
}
