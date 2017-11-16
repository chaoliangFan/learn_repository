package com.example.fanxh.simpleweather;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanxh.simpleweather.db.DbUtil;
import com.example.fanxh.simpleweather.gson.Weather;
import com.example.fanxh.simpleweather.util.HttpUtil;
import com.example.fanxh.simpleweather.util.Utility;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchAreaActivity extends Activity {
    private static final String URLSTART = "https://free-api.heweather.com/s6/weather?location=";
    private static final String URLEND = "&key=168d59faf85840c0b262b671067367e1";
    private static final String WEBSITE = "http://tools.2345.com/m/rili.htm";
    private ProgressDialog progressDidog;
    private Button mChooseArea;
    private ImageView mOfficialWebsite;
    private LinearLayout mSearchAreaItem;
    private static SQLiteDatabase db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_area);

        db = DbUtil.getDb(this);
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
                intent.setData(Uri.parse(WEBSITE));
                startActivity(intent);
            }
        });
        String weatherId = getIntent().getStringExtra("weather_id");
        if (!TextUtils.isEmpty(weatherId)) {
            showProgressDialog();
            addWeather(weatherId, "");
        } else {
            showAllWeather();
        }
    }

    public void showAllWeather() {
        closeProgressDialog();
        mSearchAreaItem.removeAllViews();
        final List<String> cNList = new ArrayList<String>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Cursor cursor = db.query("Information", null, null, null, null, null, null);
                    if (cursor != null) {
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
                                try {
                                    Date date = format.parse(t);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                    int minute = calendar.get(Calendar.MINUTE);
                                    if (hour < 10) {
                                        mSystemTime.setText("上午" + hour + ":" + minute);
                                    } else if (10 <= hour && hour < 12) {
                                        mSystemTime.setText("上午" + hour + ":" + minute);
                                    } else if (hour == 12) {
                                        mSystemTime.setText("下午" + hour + ":" + minute);
                                    } else {
                                        mSystemTime.setText("下午" + (hour - 12) + ":" + minute);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                TextView mCity = (TextView) view.findViewById(R.id.city);
                                mCity.setText(county_name);
                                if (!TextUtils.isEmpty(degree)) {
                                    TextView mDegrees = (TextView) view.findViewById(R.id.degrees);
                                    mDegrees.setText(degree);
                                    setBackground(status, view);
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
                                                showAllWeather();
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
                    }
                    try {
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addWeather(final String weatherId, final String value) {
        if (!TextUtils.isEmpty(weatherId)) {
            String weatherUrl = URLSTART + weatherId + URLEND;
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
                                db.delete("Information", "county_name = ?", new String[]{weather.basic.parent_city});
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(SearchAreaActivity.this, "城市获取失败，请换个城市", Toast.LENGTH_SHORT).show();
                            }
                            if (weather != null && "ok".equals(weather.status)) {
                                ContentValues values = new ContentValues();
                                values.clear();
                                values.put("county_name", weather.basic.parent_city);
                                values.put("degree", weather.now.tmp);
                                values.put("status", weather.now.cond_txt);
                                try {
                                    db.insert("Information", null, values);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (TextUtils.isEmpty(value)) {
                                    showAllWeather();
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
                                }
                                ContentValues values = new ContentValues();
                                values.clear();
                                values.put("county_name", countyName);
                                try {
                                    db.insert("Information", null, values);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (TextUtils.isEmpty(value)) {
                                showAllWeather();
                            }
                        }
                    });
                }
            });
        }
    }

    public static void setBackground(String string, View view) {
        final String SUN = "晴";
        final String OVERCAST = "阴";
        final String CLOUDY = "多云";
        final String LIGHT_RAIN = "小雨";
        final String MODERATE_RAIN = "中雨";
        final String HEAVY_RAIN = "大雨";
        final String SHOWER_RAIN = "阵雨";
        final String THUNDERSHOWER = "雷阵雨";
        final String LIGHT_SNOW = "小雪";

        switch (string) {

            case SUN:
                view.setBackgroundResource(R.drawable.ic_choose_sun_bg);
                break;
            case OVERCAST:
                view.setBackgroundResource(R.drawable.ic_choose_overcast_bg);
                break;
            case CLOUDY:
                view.setBackgroundResource(R.drawable.ic_choose_cloudy_bg);
                break;
            case LIGHT_RAIN:
                view.setBackgroundResource(R.drawable.i_light_rain);
                break;
            case MODERATE_RAIN:
                view.setBackgroundResource(R.drawable.i_moderate_rain);
                break;
            case HEAVY_RAIN:
                view.setBackgroundResource(R.drawable.i_heavy_rain);
                break;
            case SHOWER_RAIN:
                view.setBackgroundResource(R.drawable.i_shower_rain);
                break;
            case THUNDERSHOWER:
                view.setBackgroundResource(R.drawable.i_thundershower);
                break;
            case LIGHT_SNOW:
                view.setBackgroundResource(R.drawable.i_light_snow);
                break;
            default:
                break;
        }
    }

    public int getIndex(List<String> list, String string) {
        try {
            for (int i = 0; i < list.size(); i++) {
                if (string.equals(list.get(i))) {
                    return i;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void showProgressDialog() {
        if (progressDidog == null) {
            progressDidog = new ProgressDialog(this);
            progressDidog.setMessage("刷新界面...");
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
