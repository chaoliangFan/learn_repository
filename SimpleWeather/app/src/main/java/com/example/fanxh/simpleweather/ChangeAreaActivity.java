package com.example.fanxh.simpleweather;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import static com.example.fanxh.simpleweather.ChooseAreaFragment.LEVEL_CITY;
import static com.example.fanxh.simpleweather.ChooseAreaFragment.LEVEL_COUNTY;

public class ChangeAreaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
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
            Intent intent = new Intent(ChangeAreaActivity.this, SearchAreaActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
