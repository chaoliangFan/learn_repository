package com.example.fanxh.simpleweather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanxh.simpleweather.db.DbUtil;
import com.example.fanxh.simpleweather.gson.DailyForecast;
import com.example.fanxh.simpleweather.gson.Weather;
import com.example.fanxh.simpleweather.util.HttpUtil;
import com.example.fanxh.simpleweather.util.Utility;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by fanxh on 2017/11/7.
 */


public class ShowWeatherFragment extends Fragment {
    private static final String URLSTART = "https://free-api.heweather.com/s6/weather?location=";
    private static final String URLEND = "&key=168d59faf85840c0b262b671067367e1";
    private static final int SUCCESS = 1;
    private static final int UNSUCCESS = 2;
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

    private Weather weather;
    private ProgressDialog progressDidog;
    private Button mWeatherReflesh;
    private LinearLayout mFragmentView;
    private LinearLayout mWeatherFragment;
    private static SQLiteDatabase db;
    private Cursor cursor;
    private Activity mAcitvity;
    private static HourlyForecastAdapter mHourlyForecastAdapter;
    public String weatherId;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    setWeatherInformation(weather);
                    break;
                case UNSUCCESS:
                    mTitleCity.setText(weatherId);
                    mWeatherFragment.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

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

        mWeatherReflesh = (Button) view.findViewById(R.id.weather_reflesh);
        mFragmentView = (LinearLayout) view.findViewById(R.id.fragment_view);
        mWeatherFragment = (LinearLayout) view.findViewById(R.id.weather_fragment);
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
        mWeatherFragment.setVisibility(View.GONE);
        mWeatherReflesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                requestWeather(weatherId, weatherId);
            }
        });
        showWeatherInformation();
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

    public void showWeatherInformation() {
        db = DbUtil.getDb(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    cursor = db.query("Information", new String[]{"weatherString"}, "county_name = ?", new String[]{weatherId}, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            String weatherString = cursor.getString(cursor.getColumnIndex("weatherString"));
                            if (!TextUtils.isEmpty(weatherString)) {
                                Weather weather = Utility.handleWeatherResponse(weatherString);
                                if (weather != null && TextUtils.equals("ok", weather.status)) {
                                    setWeatherInformation(weather);
                                }
                            } else {
                                requestWeather(weatherId, weatherId);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void requestWeather(final String weatherId, final String countyName) {
        if (!TextUtils.isEmpty(weatherId)) {
            String weatherUrl = URLSTART + weatherId + URLEND;
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    weather = new Weather();
                    weather = Utility.handleWeatherResponse(responseText);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (weather != null && TextUtils.equals("ok", weather.status)) {
                                ContentValues values = new ContentValues();
                                values.clear();
                                values.put("status", weather.now.cond_txt);
                                values.put("degree", weather.now.tmp);
                                values.put("weatherString", responseText);
                                try {
                                    db.update("Information", values, "county_name = ?", new String[]{countyName});
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                closeProgressDialog();
                                Message message = new Message();
                                message.what = SUCCESS;
                                handler.sendMessage(message);
                            } else {
                                Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                                closeProgressDialog();
                            }
                        }
                    }).start();
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            mTitleCity.setText(countyName);
                            try {
                                cursor = db.query("Information", new String[]{"weatherString"}, "county_name = ?", new String[]{weatherId}, null, null, null);
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        String weatherString = cursor.getString(cursor.getColumnIndex("weatherString"));
                                        if (!TextUtils.isEmpty(weatherString)) {
                                            weather = new Weather();
                                            weather = Utility.handleWeatherResponse(weatherString);
                                            if (weather != null && TextUtils.equals("ok", weather.status)) {
                                                Message message = new Message();
                                                message.what = SUCCESS;
                                                handler.sendMessage(message);
                                                closeProgressDialog();
                                                Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Message message = new Message();
                                            message.what = UNSUCCESS;
                                            handler.sendMessage(message);
                                            closeProgressDialog();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    cursor.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            });
        }
    }

    public void setWeatherInformation(Weather weather) {
        mWeatherFragment.setVisibility(View.VISIBLE);
        if (weather != null) {
            mAQIValue.setText("无");
            mAirQualityValue.setText("无");
            if (weather.basic != null) {
                mTitleCity.setText(weather.basic.location);
            }
            if (weather.now != null) {
                mTitleNowCond.setText(weather.now.cond_txt);
                if (!TextUtils.isEmpty(weather.now.tmp)) {
                    mTitleNowDegree.setText(weather.now.tmp + "°");
                }
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mHourlyItem.setLayoutManager(layoutManager);
            if (weather.hourly != null) {
                mHourlyForecastAdapter = new HourlyForecastAdapter(weather.hourly);
                mHourlyItem.setAdapter(mHourlyForecastAdapter);
            }
            if (weather.daily_forecast != null && weather.daily_forecast.size() != 0) {
                DailyForecast dailyForecast = weather.daily_forecast.get(0);
                mDailyForecast.removeAllViews();
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
                mTitleDegree.setText(dailyForecast.tmp_max + "  " + dailyForecast.tmp_min);
                if (!TextUtils.isEmpty(dailyForecast.date)) {
                    mTitleDate.setText("星期" + Utility.getWeek(dailyForecast.date) + "  今天");
                }
                if (weather.now != null && !TextUtils.isEmpty(weather.now.cond_txt) && !TextUtils.isEmpty(dailyForecast.tmp_max)) {
                    mWeatherDescribe.setText("今天: 当前" + weather.now.cond_txt + "。 最高气温" + dailyForecast.tmp_max + "°。 今晚" + dailyForecast.cond_txt_n + "， 最低气温" + dailyForecast.tmp_min + "。");
                }

                setTime(dailyForecast.sr, dailyForecast.ss);

                if (!TextUtils.isEmpty(dailyForecast.pcpn)) {
                    mRainfallProbabilityValue.setText(weather.daily_forecast.get(0).pcpn + "%");
                }
                if (!TextUtils.isEmpty(dailyForecast.hum)) {
                    mHumidityValue.setText(dailyForecast.hum + "%");
                }
                if (!TextUtils.isEmpty(dailyForecast.wind_dir) && !TextUtils.isEmpty(dailyForecast.wind_spd)) {
                    mAirSpeedValue.setText(dailyForecast.wind_dir + " " + "每秒" + Math.round(Integer.parseInt(dailyForecast.wind_spd) / 3.6) + "米");
                }
                //         体感温度
                //        mSendibleTemperatureValue;
                if (!TextUtils.isEmpty(dailyForecast.pop)) {
                    mPrecipitationValue.setText(dailyForecast.pop + "毫米");
                }
                if (!TextUtils.isEmpty(dailyForecast.pres)) {
                    mAirPressureValue.setText(dailyForecast.pres + "百帕");
                }
                if (!TextUtils.isEmpty(dailyForecast.vis)) {
                    mVisibilityValue.setText(dailyForecast.vis + "公里");
                }
                if (!TextUtils.isEmpty(dailyForecast.uv_index)) {
                    mUltravioletIndexValue.setText(dailyForecast.uv_index);
                }
            }
        }
    }

    public void setTime(String sr, String ss) {
        if (!TextUtils.isEmpty(sr) && !TextUtils.isEmpty(ss)) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            try {
                Date dateSr = format.parse(sr);
                Date dateSs = format.parse(ss);
                Calendar calendarSr = Calendar.getInstance();
                Calendar calendarSs = Calendar.getInstance();
                calendarSr.setTime(dateSr);
                calendarSs.setTime(dateSs);
                int hourSr = calendarSr.get(Calendar.HOUR_OF_DAY);
                int minuteSr = calendarSr.get(Calendar.MINUTE);
                String setMinuteSr = "";
                if (minuteSr < 10) {
                    setMinuteSr = "0" + minuteSr;
                } else {
                    setMinuteSr = "" + minuteSr;
                }
                if (hourSr < 12) {
                    mSunriseValue.setText("上午" + hourSr + ":" + setMinuteSr);
                } else if (hourSr == 12) {
                    mSunriseValue.setText("下午" + hourSr + ":" + setMinuteSr);
                } else {
                    mSunriseValue.setText("下午" + String.valueOf(hourSr - 12) + ":" + setMinuteSr);
                }
                int hourSs = calendarSs.get(Calendar.HOUR_OF_DAY);
                int minuteSs = calendarSs.get(Calendar.MINUTE);
                String setMinuteSs = "";
                if (minuteSs < 10) {
                    setMinuteSs = "0" + minuteSs;
                } else {
                    setMinuteSs = "" + minuteSs;
                }
                if (hourSs < 12) {
                    mSunsetValue.setText("上午" + hourSs + ":" + setMinuteSs);
                } else if (hourSs == 12) {
                    mSunsetValue.setText("下午" + hourSs + ":" + setMinuteSs);
                } else {
                    mSunsetValue.setText("下午" + String.valueOf(hourSs - 12) + ":" + setMinuteSs);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void showProgressDialog() {
        if (progressDidog == null) {
            progressDidog = new ProgressDialog(getActivity());
            progressDidog.setMessage("正在刷新...");
            progressDidog.setCanceledOnTouchOutside(false);
        }
        progressDidog.show();
    }

    private void closeProgressDialog() {
        if (progressDidog != null) {
            progressDidog.dismiss();
        }
    }

}
