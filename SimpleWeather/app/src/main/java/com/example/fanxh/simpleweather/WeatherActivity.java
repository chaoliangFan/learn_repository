package com.example.fanxh.simpleweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanxh.simpleweather.gson.DailyForecast;
import com.example.fanxh.simpleweather.gson.Weather;
import com.example.fanxh.simpleweather.util.HttpUtil;
import com.example.fanxh.simpleweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ImageView mWebLeft;
    private ImageView mChooseAreaRight;

    private ViewPager mShowAllWeather;
    private static List<ShowWeatherFragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
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
                finish();
            }
        });

        mShowAllWeather = (ViewPager)findViewById(R.id.show_all_weather);
        fragmentList = new ArrayList<>();
        final List<InformationBean> informationBeans = DataSupport.findAll(InformationBean.class);
        for (InformationBean informationBean : informationBeans){
            String weatherId = informationBean.getCity();
            ShowWeatherFragment sWF = ShowWeatherFragment.newInstance(weatherId);
            fragmentList.add(sWF);
        }

        mShowAllWeather.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
 //               setTabSelection(mTabTVs[position]);
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
        });
        mShowAllWeather.setAdapter(new ShowWeatherFragmentAdapter(getSupportFragmentManager(), fragmentList));
        int item = getIntent().getIntExtra("item",0);
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
            return list.size();
        }
        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
    }
}
