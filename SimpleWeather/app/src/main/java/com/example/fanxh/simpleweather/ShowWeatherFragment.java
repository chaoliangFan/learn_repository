package com.example.fanxh.simpleweather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanxh.simpleweather.gson.DailyForecast;
import com.example.fanxh.simpleweather.gson.Weather;
import com.example.fanxh.simpleweather.util.HttpUtil;
import com.example.fanxh.simpleweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by fanxh on 2017/11/7.
 */


public class ShowWeatherFragment extends Fragment {
    private static final String URLSTART = "https://free-api.heweather.com/s6/weather?location=";
    private static final String URLEND = "&key=168d59faf85840c0b262b671067367e1";
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
    private Activity mAcitvity;
    private static HourlyForecastAdapter mHourlyForecastAdapter;
    public String weatherId;

    public static ShowWeatherFragment newInstance(String weatherId) {
        ShowWeatherFragment sWF = new ShowWeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("weatherId", weatherId);
        sWF.setArguments(bundle);
        return sWF;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mAcitvity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_weather_fragment, container, false);

        mTitleCity = (TextView) view.findViewById(R.id.title_city);
        mTitleDate = (TextView) view.findViewById(R.id.title_date);
        mTitleDegree = (TextView) view.findViewById(R.id.title_degree);
        mTitleNowCond = (TextView) view.findViewById(R.id.title_now_cond);
        mTitleNowDegree = (TextView) view.findViewById(R.id.title_now_degree);
        mHourlyItem = (RecyclerView) view.findViewById(R.id.hourly_item);
        mWeatherDescribe = (TextView) view.findViewById(R.id.weather_describe);
        mSunriseValue = (TextView) view.findViewById(R.id.sunris_value);
        mSunsetValue = (TextView) view.findViewById(R.id.sunset_value);
        mRainfallProbabilityValue = (TextView) view.findViewById(R.id.rainfall_probability_value);
        mHumidityValue = (TextView) view.findViewById(R.id.humidity_value);
        mAirSpeedValue = (TextView) view.findViewById(R.id.air_speed_value);
        mSendibleTemperatureValue = (TextView) view.findViewById(R.id.sendible_temperature_value);
        mPrecipitationValue = (TextView) view.findViewById(R.id.precipitation_value);
        mAirPressureValue = (TextView) view.findViewById(R.id.air_pressure_value);
        mVisibilityValue = (TextView) view.findViewById(R.id.visibility_value);
        mUltravioletIndexValue = (TextView) view.findViewById(R.id.ultraviolet_index_value);
        mAQIValue = (TextView) view.findViewById(R.id.aqi_value);
        mAirQualityValue = (TextView) view.findViewById(R.id.air_quality_value);
        mDailyForecast = (LinearLayout) view.findViewById(R.id.daily_forecast_item);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAcitvity);
        String weatherString = prefs.getString(weatherId, null);
        if (!TextUtils.isEmpty(weatherString)) {
            final Weather weather = Utility.handleWeatherResponse(weatherString);
            if (weather != null && TextUtils.equals("ok", weather.status)) {
                showWeatherInformation(weather);
            }
        } else {
            requestWeather(weatherId, weatherId);
            prefs.edit().clear();
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            weatherId = args.getString("weatherId");
        }
    }

    public void requestWeather(final String weatherId, final String countyName) {
        if (!TextUtils.isEmpty(weatherId)) {
            String weatherUrl = URLSTART + weatherId + URLEND;
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = Utility.handleWeatherResponse(responseText);
                    mAcitvity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (weather != null && TextUtils.equals("ok", weather.status)) {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mAcitvity).edit();
                                editor.putString(weatherId, responseText);
                                editor.apply();
                                showWeatherInformation(weather);
                            } else {
                                Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    mAcitvity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTitleCity.setText(countyName);
                            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public void showWeatherInformation(Weather weather) {
        if (weather != null) {
            if (weather.basic != null && !TextUtils.isEmpty(weather.basic.parent_city)) {
                mTitleCity.setText(weather.basic.parent_city);
            }
            if (weather.now != null && !TextUtils.isEmpty(weather.now.cond_txt)) {
                mTitleNowCond.setText(weather.now.cond_txt);
            }
            if (weather.now != null && TextUtils.isEmpty(weather.now.tmp)) {
                mTitleNowDegree.setText(weather.now.tmp + "°");
            }
            if (weather.daily_forecast != null) {
                if (weather.daily_forecast.size() != 0 && weather.daily_forecast.get(0) != null) {
                    if (!TextUtils.isEmpty(weather.daily_forecast.get(0).date)) {
                        mTitleDate.setText("星期" + Utility.getWeek(weather.daily_forecast.get(0).date) + "  今天");
                    }
                    if (!TextUtils.isEmpty(weather.daily_forecast.get(0).tmp_max) && !TextUtils.isEmpty(weather.daily_forecast.get(0).tmp_min)) {
                        mTitleDegree.setText(weather.daily_forecast.get(0).tmp_max + "  " + weather.daily_forecast.get(0).tmp_min);
                    }
                }
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mHourlyItem.setLayoutManager(layoutManager);
            if (weather.hourly != null) {
                mHourlyForecastAdapter = new HourlyForecastAdapter(weather.hourly);
                mHourlyItem.setAdapter(mHourlyForecastAdapter);
            }
            mDailyForecast.removeAllViews();
            if (weather.daily_forecast != null) {
                for (int i = 0; i < 3; i++) {
                    for (DailyForecast mDaily_forecast : weather.daily_forecast) {
                        View view = LayoutInflater.from(mAcitvity).inflate(R.layout.daily_forecast_item, mDailyForecast, false);
                        TextView mDailyDate = (TextView) view.findViewById(R.id.daily_date);
                        ImageView mDailyStatus = (ImageView) view.findViewById(R.id.daily_status);
                        TextView mDailyDegree = (TextView) view.findViewById(R.id.daily_degree);
                        String mDailyTmpMax = mDaily_forecast.tmp_max;
                        if (!TextUtils.isEmpty(mDaily_forecast.date)) {
                            mDailyDate.setText("星期" + Utility.getWeek(mDaily_forecast.date));
                        }
                        if (!TextUtils.isEmpty(mDaily_forecast.cond_txt_d)) {
                            Utility.setImage(mDaily_forecast.cond_txt_d, mDailyStatus);
                        }
                        if (!TextUtils.isEmpty(mDailyTmpMax) && !TextUtils.isEmpty(mDaily_forecast.tmp_min)) {
                            mDailyDegree.setText(mDailyTmpMax + "  " + mDaily_forecast.tmp_min);
                        }
                        mDailyForecast.addView(view);
                    }
                }
            }
            if (weather.now != null && !TextUtils.isEmpty(weather.now.cond_txt)) {
                if (weather.daily_forecast != null && weather.daily_forecast.get(0) != null && !TextUtils.isEmpty(weather.daily_forecast.get(0).tmp_max)) {
                    mWeatherDescribe.setText("今天: 当前" + weather.now.cond_txt + "。 最高气温" + weather.daily_forecast.get(0).tmp_max + "°。 今晚" + weather.daily_forecast.get(0).cond_txt_n + "， 最低气温" + weather.daily_forecast.get(0).tmp_min + "。");
                }
            }
            if (weather.daily_forecast.get(0).sr.length() == 5 && weather.daily_forecast.get(0).ss.length() == 5) {
                if (Time(weather.daily_forecast.get(0).sr) < 12) {
                    mSunriseValue.setText("上午" + weather.daily_forecast.get(0).sr.substring(1, 5));
                }
                if (Time(weather.daily_forecast.get(0).ss) > 12) {
                    mSunsetValue.setText("下午" + Integer.toString(Time(weather.daily_forecast.get(0).ss) - 12) + weather.daily_forecast.get(0).ss.substring(2, 5));
                }
            }
            if (weather.daily_forecast != null && weather.daily_forecast.get(0) != null) {
                if (!TextUtils.isEmpty(weather.daily_forecast.get(0).pcpn)) {
                    mRainfallProbabilityValue.setText(weather.daily_forecast.get(0).pcpn + "%");
                }
                if (!TextUtils.isEmpty(weather.daily_forecast.get(0).hum)) {
                    mHumidityValue.setText(weather.daily_forecast.get(0).hum + "%");
                }
                if (!TextUtils.isEmpty(weather.daily_forecast.get(0).wind_dir) && !TextUtils.isEmpty(weather.daily_forecast.get(0).wind_spd)) {
                    mAirSpeedValue.setText(weather.daily_forecast.get(0).wind_dir + " " + "每秒" + Math.round(Integer.parseInt(weather.daily_forecast.get(0).wind_spd) / 3.6) + "米");
                }
                //体感温度
//        mSendibleTemperatureValue;
                if (!TextUtils.isEmpty(weather.daily_forecast.get(0).pop)) {
                    mPrecipitationValue.setText(weather.daily_forecast.get(0).pop + "毫米");
                }
                if (!TextUtils.isEmpty(weather.daily_forecast.get(0).pres)) {
                    mAirPressureValue.setText(weather.daily_forecast.get(0).pres + "百帕");
                }
                if (!TextUtils.isEmpty(weather.daily_forecast.get(0).vis)) {
                    mVisibilityValue.setText(weather.daily_forecast.get(0).vis + "公里");
                }
                if (!TextUtils.isEmpty(weather.daily_forecast.get(0).uv_index)) {
                    mUltravioletIndexValue.setText(weather.daily_forecast.get(0).uv_index);
                }
            }
            try {
                mAQIValue.setText(weather.aqi.city.aqi);
                mAirQualityValue.setText(weather.aqi.city.qlty);
            } catch (Exception e) {
                mAQIValue.setText("无");
                mAirQualityValue.setText("无");
            }
        }
    }

    public int Time(String string) {
        if (!TextUtils.isEmpty(string)) {
            if (string.length() > 4) {
                try {
                    String detailedTime = string.substring(0, 2);
                    return Integer.parseInt(detailedTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}
