package com.example.fanxh.simpleweather;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanxh.simpleweather.db.SWDatabase;
import com.example.fanxh.simpleweather.gson.Weather;
import com.example.fanxh.simpleweather.util.HttpUtil;
import com.example.fanxh.simpleweather.util.Utility;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchAreaActivity extends Activity {
    private Button mChooseArea;
    private ImageView mOfficialWebsite;
    private LinearLayout mSearchAreaItem;
    private static SWDatabase swDatabase;
    private static SQLiteDatabase db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_area);
        swDatabase = new SWDatabase(this, "SimpleWeather.db", null, 2);
        db = swDatabase.getWritableDatabase();
        mSearchAreaItem = (LinearLayout) findViewById(R.id.search_area_item);
        mChooseArea = (Button) findViewById(R.id.choose_area);
        mChooseArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchAreaActivity.this, ChangeAreaActivity.class);
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
        if (!TextUtils.isEmpty(weatherId)) {
            addWeather(weatherId, "");
        } else {
            showAllWeather();
        }
    }

    public void showAllWeather() {
        mSearchAreaItem.removeAllViews();
        final List<String> cNList = new ArrayList<String>();
        Cursor cursor = db.query("Information", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                final String county_name = cursor.getString(cursor.getColumnIndex("county_name"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                String degree = cursor.getString(cursor.getColumnIndex("degree"));
                cNList.add(county_name);
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
                mCity.setText(county_name);
                if (!TextUtils.isEmpty(degree)) {
                    TextView mDegrees = (TextView) view.findViewById(R.id.degrees);
                    mDegrees.setText(degree);
                    switch (status) {
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
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    db.delete("Information", "county_name = ?", new String[]{county_name});
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
                        intent.putExtra("item", getIndex(cNList, county_name));
                        startActivity(intent);
                        addWeather(county_name, "value");
                        finish();
                    }
                });
                mSearchAreaItem.addView(view);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void addWeather(final String weatherId, final String value) {
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
                            db.delete("Information", "county_name = ?", new String[]{weather.basic.city});
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SearchAreaActivity.this, "城市获取失败，请换个城市", Toast.LENGTH_SHORT).show();
                        }
                        if (weather != null && "ok".equals(weather.status)) {
                            ContentValues values = new ContentValues();
                            values.clear();
                            values.put("county_name", weather.basic.city);
                            values.put("degree", weather.now.tmp);
                            values.put("status", weather.now.cond.txt);
                            db.insert("Information", null, values);
                            if (TextUtils.isEmpty(value)) {
                                Intent intent = new Intent(SearchAreaActivity.this, SearchAreaActivity.class);
                                intent.putExtra("weather_id", "");
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(final Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        String countyName = getIntent().getStringExtra("countyName");
                        if (!TextUtils.isEmpty(countyName)) {
                            try {
                                db.delete("Information", "county_name = ?", new String[]{countyName});
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(SearchAreaActivity.this, "城市获取失败，请换个城市", Toast.LENGTH_SHORT).show();
                            }
                            ContentValues values = new ContentValues();
                            values.clear();
                            values.put("county_name", countyName);
                            db.insert("Information", null, values);
                        }
                        if (TextUtils.isEmpty(value)) {
                            Intent intent = new Intent(SearchAreaActivity.this, SearchAreaActivity.class);
                            intent.putExtra("weather_id", "");
                            startActivity(intent);
                            finish();
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

    public int getIndex(List<String> list, String string) {
        for (int i = 0; i < list.size(); i++) {
            if (string.equals(list.get(i))) {
                return i;
            }
        }
        return 1;
    }
}
