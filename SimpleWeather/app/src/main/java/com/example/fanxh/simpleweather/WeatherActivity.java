package com.example.fanxh.simpleweather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.fanxh.simpleweather.db.SWDatabase;
import com.example.fanxh.simpleweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends FragmentActivity {
    private ImageView mWebLeft;
    private ImageView mChooseAreaRight;
    private ViewPager mShowAllWeather;
    private static List<ShowWeatherFragment> fragmentList;
    private SWDatabase swDatabase;
    private static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Utility.ActivityCollector.addActivity(this);
//        swDatabase = new SWDatabase(this, "SimpleWeather.db", null, 2);
        swDatabase = new SWDatabase(this);
        db = swDatabase.getWritableDatabase();
        mWebLeft = (ImageView) findViewById(R.id.web_left);
        mChooseAreaRight = (ImageView) findViewById(R.id.choose_area_right);
        mWebLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://tools.2345.com/m/rili.htm"));
                startActivity(intent);
            }
        });
        mChooseAreaRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, SearchAreaActivity.class);
                startActivity(intent);
            }
        });

        mShowAllWeather = (ViewPager) findViewById(R.id.show_all_weather);
        fragmentList = new ArrayList<>();
        fragmentList.clear();
        Cursor cursor = db.query("Information", null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String weatherId = cursor.getString(cursor.getColumnIndex("county_name"));
                    ShowWeatherFragment sWF = ShowWeatherFragment.newInstance(weatherId);
                    fragmentList.add(sWF);
                } while (cursor.moveToNext());
            }
        }
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mShowAllWeather.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //              setTabSelection(mTabTVs[position]);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
        });
        mShowAllWeather.setOffscreenPageLimit(fragmentList.size());
        mShowAllWeather.setAdapter(new ShowWeatherFragmentAdapter(getSupportFragmentManager(), fragmentList));
        int item = getIntent().getIntExtra("item", 0);
        mShowAllWeather.setCurrentItem(item);
    }

    public class ShowWeatherFragmentAdapter extends FragmentStatePagerAdapter {
        List<ShowWeatherFragment> list;

        public ShowWeatherFragmentAdapter(FragmentManager fm, List<ShowWeatherFragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Fragment getItem(int arg0) {
            if (list.size() > arg0) {
                return list.get(arg0);
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.ActivityCollector.finishAll();
    }
}
