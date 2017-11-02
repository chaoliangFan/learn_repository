package com.example.fanxh.simpleweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.fanxh.simpleweather.gson.Daily_forecast;
import com.example.fanxh.simpleweather.gson.Weather;
import com.example.fanxh.simpleweather.util.HttpUtil;
import com.example.fanxh.simpleweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView mTitleCity;
    private TextView mTitleNowCond;
    private TextView mTitleNowDegree;
    private TextView mTitleDate;
    private TextView mTitleDegree;
    private RecyclerView mHourlyItem;
    private TextView mWeatherDescribe;

    private TextView mSunriseValue;
    private TextView mSunsetValue;
    private TextView mRainfallProbabilityValue;
    private TextView mHumidityValue;
    private TextView mAirSpeedValue;
    private TextView mSendibleTemperatureValue;
    private TextView mPrecipitationValue;
    private TextView mAirPressureValue;
    private TextView mVisibilityValue;
    private TextView mUltravioletIndexValue;
    private TextView mAQIValue;
    private TextView mAirQualityValue;

    private LinearLayout mDailyForecast;


    private ImageView mWebLeft;
    private ImageView mChooseAreaRight;

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
                Intent intent  = new Intent(WeatherActivity.this,SearchAreaActivity.class);
                startActivity(intent);
            }
        });


        mTitleCity = (TextView) findViewById(R.id.title_city);
        mTitleDate = (TextView) findViewById(R.id.title_date);
        mTitleDegree = (TextView) findViewById(R.id.title_degree);
        mTitleNowCond = (TextView) findViewById(R.id.title_now_cond);
        mTitleNowDegree = (TextView) findViewById(R.id.title_now_degree);

        mHourlyItem = (RecyclerView) findViewById(R.id.hourly_item);

        mWeatherDescribe = (TextView) findViewById(R.id.weather_describe);

        mSunriseValue = (TextView) findViewById(R.id.sunris_value);
        mSunsetValue = (TextView) findViewById(R.id.sunset_value);
        mRainfallProbabilityValue = (TextView) findViewById(R.id.rainfall_probability_value);
        mHumidityValue = (TextView) findViewById(R.id.humidity_value);
        mAirSpeedValue = (TextView) findViewById(R.id.air_speed_value);
        mSendibleTemperatureValue = (TextView) findViewById(R.id.sendible_temperature_value);
        mPrecipitationValue = (TextView) findViewById(R.id.precipitation_value);
        mAirPressureValue = (TextView) findViewById(R.id.air_pressure_value);
        mVisibilityValue = (TextView) findViewById(R.id.visibility_value);
        mUltravioletIndexValue = (TextView) findViewById(R.id.ultraviolet_index_value);
        mAQIValue = (TextView) findViewById(R.id.aqi_value);
        mAirQualityValue = (TextView) findViewById(R.id.air_quality_value);
        mDailyForecast = (LinearLayout) findViewById(R.id.daily_forecast_item);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        String weatherIdString = prefs.getString("weatherId", null);
        String weatherId = getIntent().getStringExtra("weather_id");
        if (TextUtils.isEmpty(weatherId) && weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInformation(weather);
        } else if (!TextUtils.isEmpty(weatherId) && weatherString != null && weatherId.equals(weatherIdString)) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInformation(weather);
        } else {
            requestWeather(weatherId);
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
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.clear();
                            editor.putString("weather", responseText);
                            editor.putString("weatherId", weatherId);
                            editor.apply();
                            showWeatherInformation(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void showWeatherInformation(Weather weather) {

        mTitleCity.setText(weather.basic.city);

        mTitleNowCond.setText(weather.now.cond.txt);
        mTitleNowDegree.setText(weather.now.tmp + "°");

        mTitleDate.setText("星期" + Utility.getWeek(weather.daily_forecast.get(0).date) + "  今天");
        mTitleDegree.setText(weather.daily_forecast.get(0).tmp.max + "  " + weather.daily_forecast.get(0).tmp.min);




        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHourlyItem.setLayoutManager(layoutManager);
        HourlyForecastAdapter mHourlyForecastAdapter = new HourlyForecastAdapter(weather.hourly_forecast);
        mHourlyItem.setAdapter(mHourlyForecastAdapter);

        mDailyForecast.removeAllViews();
        for (int i = 0; i < 3; i++) {
            for (Daily_forecast mDaily_forecast : weather.daily_forecast) {
                View view = LayoutInflater.from(this).inflate(R.layout.daily_forecast_item, mDailyForecast, false);
                TextView mDailyDate = (TextView) view.findViewById(R.id.daily_date);
                ImageView mDailyStatus = (ImageView) view.findViewById(R.id.daily_status);
                TextView mDailyDegree = (TextView) view.findViewById(R.id.daily_degree);
                String mDailyTmpMax = mDaily_forecast.tmp.max;
                mDailyDate.setText("星期" + Utility.getWeek(mDaily_forecast.date));
                switch (mDaily_forecast.cond.txt_d) {

                    case "晴":
                        mDailyStatus.setImageResource(R.drawable.i_sun);
                        break;
                    case "阴":
                        mDailyStatus.setImageResource(R.drawable.i_overcast);
                        break;
                    case "多云":
                        mDailyStatus.setImageResource(R.drawable.i_cloudy);
                        break;
                    case "小雨":
                        mDailyStatus.setImageResource(R.drawable.i_light_rain);
                        break;
                    case "中雨":
                        mDailyStatus.setImageResource(R.drawable.i_moderate_rain);
                        break;
                    case "大雨":
                        mDailyStatus.setImageResource(R.drawable.i_heavy_rain);
                        break;
                    case "阵雨":
                        mDailyStatus.setImageResource(R.drawable.i_shower_rain);
                        break;
                    case "雷阵雨":
                        mDailyStatus.setImageResource(R.drawable.i_thundershower);
                        break;
                    case "小雪":
                        mDailyStatus.setImageResource(R.drawable.i_light_snow);
                        break;
                    default:
                }
                mDailyDegree.setText(mDailyTmpMax + "  " + mDaily_forecast.tmp.min);
                mDailyForecast.addView(view);
            }

        }

        mWeatherDescribe.setText("今天: 当前" + weather.now.cond.txt + "。 最高气温" + weather.daily_forecast.get(0).tmp.max + "°。 今晚" + weather.daily_forecast.get(0).cond.txt_n + "， 最低气温" + weather.daily_forecast.get(0).tmp.min + "。");

        if (Time(weather.daily_forecast.get(0).astro.sr) < 12){
            mSunriseValue.setText("上午"+weather.daily_forecast.get(0).astro.sr.substring(1, 5));
        }
        if (Time(weather.daily_forecast.get(0).astro.ss) > 12) {
            mSunsetValue.setText("下午" + Integer.toString(Time(weather.daily_forecast.get(0).astro.ss) - 12) + weather.daily_forecast.get(0).astro.ss.substring(2, 5));
        }
        mRainfallProbabilityValue.setText(weather.daily_forecast.get(0).pcpn + "%");
        mHumidityValue.setText(weather.daily_forecast.get(0).hum + "%");
        mAirSpeedValue.setText(weather.daily_forecast.get(0).wind.dir + " " + "每秒" + Math.round(Integer.parseInt(weather.daily_forecast.get(0).wind.spd) / 3.6) + "米");
        //体感温度
//        mSendibleTemperatureValue;
        mPrecipitationValue.setText(weather.daily_forecast.get(0).pop + "毫米");
        mAirPressureValue.setText(weather.daily_forecast.get(0).pres + "百帕");
        mVisibilityValue.setText(weather.daily_forecast.get(0).vis + "公里");
        mUltravioletIndexValue.setText(weather.daily_forecast.get(0).uv);
        try {
            mAQIValue.setText(weather.aqi.city.aqi);
            mAirQualityValue.setText(weather.aqi.city.qlty);
        }catch(Exception e){
            mAQIValue.setText("无");
            mAirQualityValue.setText("无");
        }
    }

    public int Time(String string) {
        String str = string;
        String detailedTime = str.substring(0, 2);
        return Integer.parseInt(detailedTime);
    }
}
