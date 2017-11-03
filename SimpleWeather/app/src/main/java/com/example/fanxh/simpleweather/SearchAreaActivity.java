package com.example.fanxh.simpleweather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

public class SearchAreaActivity extends AppCompatActivity {
    private static List<InformationBean> mInformationBeanList = new ArrayList<>();
    private InformationBean mInformationBean;
    private Button mChooseArea;
    private ImageView mOfficialWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_area);
        mChooseArea = (Button) findViewById(R.id.choose_area);
        mChooseArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchAreaActivity.this, ChangeArea.class);
                startActivity(intent);
                finish();
            }
        });
        mOfficialWebsite = (ImageView)findViewById(R.id.official_website);
        mOfficialWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://tools.2345.com/m/rili.htm"));
                startActivity(intent);
                finish();
            }
        });
        String weatherId = getIntent().getStringExtra("weather_id");
        if (weatherId != null) {
            requestWeather(weatherId);
        } else {
            final List<InformationBean> informationBeans = DataSupport.findAll(InformationBean.class);
//            mInformationBeanList.addAll(informationBeans);
            SearchAreaAdapter mSearchAreaAdapter = new SearchAreaAdapter(SearchAreaActivity.this, R.layout.search_area_item, informationBeans);
            ListView mSearchArea = (ListView) findViewById(R.id.search_area);
            mSearchArea.setAdapter(mSearchAreaAdapter);
            mSearchArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    InformationBean mIBean = informationBeans.get(position);
                    Intent intent = new Intent(SearchAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("weather_id", mIBean.getCity());
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    public void requestWeather(final String weatherId) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" +
                weatherId + "&key=168d59faf85840c0b262b671067367e1";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DataSupport.deleteAll(InformationBean.class,"city = ?",weather.basic.city);
                        if (weather != null && "ok".equals(weather.status)) {
                            mInformationBean = new InformationBean();
                            mInformationBean.setCity(weather.basic.city);
                            mInformationBean.setDegrees(weather.now.tmp);
                            mInformationBean.setStatus(weather.now.cond.txt);
                            mInformationBean.save();
                            final List<InformationBean> informationBeans = DataSupport.findAll(InformationBean.class);
                            mInformationBeanList.add(mInformationBean);
                            SearchAreaAdapter mSearchAreaAdapter = new SearchAreaAdapter(SearchAreaActivity.this, R.layout.search_area_item, informationBeans);
                            ListView mSearchArea = (ListView) findViewById(R.id.search_area);
                            mSearchArea.setAdapter(mSearchAreaAdapter);
                            mSearchArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    InformationBean mIBean = informationBeans.get(position);
                                    Intent intent = new Intent(SearchAreaActivity.this, WeatherActivity.class);
                                    intent.putExtra("weather_id", mIBean.getCity());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            Toast.makeText(SearchAreaActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchAreaActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
