package com.example.fanxh.simpleweather;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanxh.simpleweather.gson.Weather;
import com.example.fanxh.simpleweather.util.HttpUtil;
import com.example.fanxh.simpleweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchAreaActivity extends Activity {
    private static List<InformationBean> mInformationBeanList = new ArrayList<>();
    private InformationBean mInformationBean;
    private Button mChooseArea;
    private ImageView mOfficialWebsite;
    private LinearLayout mSearchAreaItem;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_area);
        mSearchAreaItem = (LinearLayout) findViewById(R.id.search_area_item);
        mChooseArea = (Button) findViewById(R.id.choose_area);
        mChooseArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchAreaActivity.this, ChangeArea.class);
                startActivity(intent);
                finish();
            }
        });
        mOfficialWebsite = (ImageView) findViewById(R.id.official_website);
        mOfficialWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://tools.2345.com/m/rili.htm"));
                startActivity(intent);
            }
        });

        String weatherId = getIntent().getStringExtra("weather_id");

        if (weatherId != null) {

            requestWeather(weatherId);
        } else {
                final List<InformationBean> informationBeans = DataSupport.findAll(InformationBean.class);
            mSearchAreaItem.removeAllViews();
            for (final InformationBean informationBean : informationBeans) {
                View view = LayoutInflater.from(SearchAreaActivity.this).inflate(R.layout.search_area_item, mSearchAreaItem, false);
                TextView mSystemTime = (TextView) view.findViewById(R.id.system_time);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                String t = format.format(new Date());
                if (Time(t) < 10) {
                    mSystemTime.setText("上午" + t.substring(12, 16));
                } else if (10 <= Time(t) && Time(t) < 12) {
                    mSystemTime.setText("上午" + t.substring(11, 16));
                } else if (Time(t) == 12) {
                    mSystemTime.setText("下午12时");
                } else {
                    mSystemTime.setText("下午" + Integer.toString(Time(t) - 12) + t.substring(13, 16));
                }
                TextView mCity = (TextView) view.findViewById(R.id.city);
                mCity.setText(informationBean.getCity());
                if (!TextUtils.isEmpty(informationBean.getDegrees())) {
                    TextView mDegrees = (TextView) view.findViewById(R.id.degrees);
                    mDegrees.setText(informationBean.getDegrees());
                    switch (informationBean.getStatus()) {
                        case "晴":
                            view.setBackgroundResource(R.drawable.ic_choose_sun_bg);
                            break;
                        case "阴":
                            view.setBackgroundResource(R.drawable.ic_choose_overcast_bg);
                            break;
                        case "多云":
                            view.setBackgroundResource(R.drawable.ic_choose_cloudy_bg);
                            break;
                        case "小雨":
                            view.setBackgroundResource(R.drawable.i_light_rain);
                            break;
                        case "中雨":
                            view.setBackgroundResource(R.drawable.i_moderate_rain);
                            break;
                        case "大雨":
                            view.setBackgroundResource(R.drawable.i_heavy_rain);
                            break;
                        case "阵雨":
                            view.setBackgroundResource(R.drawable.i_shower_rain);
                            break;
                        default:
                            break;
                    }

                }

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SearchAreaActivity.this);
//                        dialog.setTitle("删除关注城市");
//                        dialog.setMessage("");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    DataSupport.deleteAll(InformationBean.class, "city = ?", informationBean.getCity());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                onCreate(null);
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                        return true;
                    }
                });


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchAreaActivity.this, WeatherActivity.class);
                        intent.putExtra("item",informationBean.getItem());
                        Log.d("**********","item=  "+informationBean.getItem());
//                        intent.putExtra("weather_id", informationBean.getCity());
                        startActivity(intent);
                        requestWeather(informationBean.getCity());
                        finish();
                    }
                });
                mSearchAreaItem.addView(view);
            }
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
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            DataSupport.deleteAll(InformationBean.class, "city = ?", weather.basic.city);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SearchAreaActivity.this, "城市获取失败，请换个城市", Toast.LENGTH_SHORT).show();
                        }
                        if (weather != null && "ok".equals(weather.status)) {
                            mInformationBean = new InformationBean();
                            mInformationBean.setCity(weather.basic.city);
                            mInformationBean.setDegrees(weather.now.tmp);
                            mInformationBean.setStatus(weather.now.cond.txt);
//                            mInformationBean.setItem((DataSupport.findAll(InformationBean.class)).size());
                            Log.d("***************","informationBeans.size:   "+DataSupport.findAll(InformationBean.class));
                            mInformationBean.save();


                            final List<InformationBean> informationBeans = DataSupport.findAll(InformationBean.class);

                            for (int i=0;i<informationBeans.size();i++){
                                    InformationBean mInformationBeanItem = new InformationBean();
                                    mInformationBeanItem.setItem(i);
                                    mInformationBeanItem.updateAll("city = ?",informationBeans.get(i).getCity());
                            }

                            mSearchAreaItem.removeAllViews();
                            for (final InformationBean informationBean : informationBeans) {
                                View view = LayoutInflater.from(SearchAreaActivity.this).inflate(R.layout.search_area_item, mSearchAreaItem, false);
                                TextView mSystemTime = (TextView) view.findViewById(R.id.system_time);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                                String t = format.format(new Date());
                                if (Time(t) < 10) {
                                    mSystemTime.setText("上午" + t.substring(12, 16));
                                } else if (10 <= Time(t) && Time(t) < 12) {
                                    mSystemTime.setText("上午" + t.substring(11, 16));
                                } else if (Time(t) == 12) {
                                    mSystemTime.setText("下午12时");
                                } else {
                                    mSystemTime.setText("下午" + Integer.toString(Time(t) - 12) + t.substring(13, 16));
                                }

                                TextView mCity = (TextView) view.findViewById(R.id.city);
                                mCity.setText(informationBean.getCity());
                                if (!TextUtils.isEmpty(informationBean.getDegrees())) {
                                    TextView mDegrees = (TextView) view.findViewById(R.id.degrees);
                                    mDegrees.setText(informationBean.getDegrees());
                                    switch (informationBean.getStatus()) {
                                        case "晴":
                                            view.setBackgroundResource(R.drawable.ic_choose_sun_bg);
                                            break;
                                        case "阴":
                                            view.setBackgroundResource(R.drawable.ic_choose_overcast_bg);
                                            break;
                                        case "多云":
                                            view.setBackgroundResource(R.drawable.ic_choose_cloudy_bg);
                                            break;
                                        case "小雨":
                                            view.setBackgroundResource(R.drawable.i_light_rain);
                                            break;
                                        case "中雨":
                                            view.setBackgroundResource(R.drawable.i_moderate_rain);
                                            break;
                                        case "大雨":
                                            view.setBackgroundResource(R.drawable.i_heavy_rain);
                                            break;
                                        case "阵雨":
                                            view.setBackgroundResource(R.drawable.i_shower_rain);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SearchAreaActivity.this, WeatherActivity.class);
                                        intent.putExtra("item",informationBean.getItem());
  //                                      intent.putExtra("weather_id", informationBean.getCity());
                                        startActivity(intent);

                                        requestWeather(informationBean.getCity());
                                        finish();
                                    }
                                });
                                mSearchAreaItem.addView(view);
                            }
                        } else {
                            Toast.makeText(SearchAreaActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            String countyName = getIntent().getStringExtra("countyName");
                            DataSupport.deleteAll(InformationBean.class, "city = ?", countyName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SearchAreaActivity.this, "城市获取失败，请换个城市", Toast.LENGTH_SHORT).show();
                        }
                        mInformationBean = new InformationBean();
                        mInformationBean.setCity(getIntent().getStringExtra("countyName"));
                        mInformationBean.save();
                        final List<InformationBean> informationBeans = DataSupport.findAll(InformationBean.class);
                        mSearchAreaItem.removeAllViews();
                        for (final InformationBean informationBean : informationBeans) {
                            View view = LayoutInflater.from(SearchAreaActivity.this).inflate(R.layout.search_area_item, mSearchAreaItem, false);
                            TextView mSystemTime = (TextView) view.findViewById(R.id.system_time);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                            String t = format.format(new Date());
                            if (Time(t) < 10) {
                                mSystemTime.setText("上午" + t.substring(12, 16));
                            } else if (10 <= Time(t) && Time(t) < 12) {
                                mSystemTime.setText("上午" + t.substring(11, 16));
                            } else if (Time(t) == 12) {
                                mSystemTime.setText("下午12时");
                            } else {
                                mSystemTime.setText("下午" + Integer.toString(Time(t) - 12) + t.substring(13, 16));
                            }

                            TextView mCity = (TextView) view.findViewById(R.id.city);
                            mCity.setText(informationBean.getCity());
                        if (!TextUtils.isEmpty(informationBean.getDegrees())) {
                            TextView mDegrees = (TextView) view.findViewById(R.id.degrees);
                            mDegrees.setText(informationBean.getDegrees());
                            switch (informationBean.getStatus()) {
                                case "晴":
                                    view.setBackgroundResource(R.drawable.ic_choose_sun_bg);
                                    break;
                                case "阴":
                                    view.setBackgroundResource(R.drawable.ic_choose_overcast_bg);
                                    break;
                                case "多云":
                                    view.setBackgroundResource(R.drawable.ic_choose_cloudy_bg);
                                    break;
                                case "小雨":
                                    view.setBackgroundResource(R.drawable.i_light_rain);
                                    break;
                                case "中雨":
                                    view.setBackgroundResource(R.drawable.i_moderate_rain);
                                    break;
                                case "大雨":
                                    view.setBackgroundResource(R.drawable.i_heavy_rain);
                                    break;
                                case "阵雨":
                                    view.setBackgroundResource(R.drawable.i_shower_rain);
                                    break;
                                default:
                                    break;
                            }
                        }

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SearchAreaActivity.this, WeatherActivity.class);
                                    intent.putExtra("weather_id", informationBean.getCity());
                                    startActivity(intent);
                                    requestWeather(informationBean.getCity());
                                    finish();
                                }
                            });
                            mSearchAreaItem.addView(view);
                        }
                    }

//                        Toast.makeText(SearchAreaActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();

                });
            }
        });
    }

    public int Time(String string) {
        String str = string;
        String detailedTime = str.substring(11, 13);
        return Integer.parseInt(detailedTime);
    }
}
