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
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.fanxh.simpleweather.db.DbUtil;
import com.example.fanxh.simpleweather.gson.Weather;
import com.example.fanxh.simpleweather.util.HttpUtil;
import com.example.fanxh.simpleweather.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchAreaActivity extends Activity {
    private static final String URLSTART = "https://free-api.heweather.com/s6/weather?location=";
    private static final String URLEND = "&key=168d59faf85840c0b262b671067367e1";
    private static final String WEBSITE = "http://tools.2345.com/m/rili.htm";
    private static final int SUCCESS = 1;
    private ProgressDialog progressDidog;
    private Button mChooseArea;
    private ImageView mOfficialWebsite;
    private static SQLiteDatabase db;
    private Cursor cursor;

    private static List<Information> informationList;


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
           switch (msg.what){
               case SUCCESS:
                   InformationAdapter adapter=new InformationAdapter(SearchAreaActivity.this, R.layout.search_area_item,informationList);
                   ListView listView=(ListView)findViewById(R.id.search_area);
                   listView.setAdapter(adapter);
                   setClick(listView);
           }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_area);
        db = DbUtil.getDb(this);
        mChooseArea = (Button) findViewById(R.id.choose_area);
        mOfficialWebsite = (ImageView) findViewById(R.id.official_website);
        mChooseArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchAreaActivity.this, ChangeAreaActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mOfficialWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(WEBSITE));
                startActivity(intent);
            }
        });
        if (getIntent() != null) {
            String weatherId = getIntent().getStringExtra("weather_id");
            if (!TextUtils.isEmpty(weatherId)) {
                showProgressDialog();
                addWeather(weatherId, "");
            } else {
                showAllWeather();
            }
        }
    }

    /**
     *设置点击事件
     * @param listView
     */
    public void setClick(ListView listView){
        if(listView != null){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Information information = informationList.get(position);
                    Intent intent = new Intent(SearchAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("item", position);
                    startActivity(intent);
                    addWeather(information.getCounty_name(), "value");
                    finish();
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Information information = informationList.get(position);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SearchAreaActivity.this);
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                db.delete("Information", "county_name = ?", new String[]{information.getCounty_name()});
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
        }
    }

    /**
     * 显示所有添加的地区列表
     */
    public void showAllWeather() {
        closeProgressDialog();
        informationList = new ArrayList<>();
        informationList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cursor = db.query("Information", null, null, null, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                Information information = new Information();
                                final String county_name = cursor.getString(cursor.getColumnIndex("county_name"));
                                String status = cursor.getString(cursor.getColumnIndex("status"));
                                String degree = cursor.getString(cursor.getColumnIndex("degree"));
                                information.setCounty_name(county_name);
                                information.setDegree(degree);
                                information.setStatus(status);
                                informationList.add(information);
                            } while (cursor.moveToNext());
                        }
                        Message message = new Message();
                        message.what = SUCCESS;
                        handler.sendMessage(message);
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

    /**
     * 新增地区信息
     * @param weatherId
     * @param value
     */
    public void addWeather(final String weatherId, final String value) {
        if (!TextUtils.isEmpty(weatherId)) {
            String weatherUrl = URLSTART + weatherId + URLEND;
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = Utility.handleWeatherResponse(responseText);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                db.delete("Information", "county_name = ?", new String[]{weather.basic.location});
                            } catch (Exception e) {
                                e.printStackTrace();
//                                Toast.makeText(SearchAreaActivity.this, "城市获取失败，请换个城市", Toast.LENGTH_SHORT).show();
                            }
                            if (weather != null && "ok".equals(weather.status)) {
                                ContentValues values = new ContentValues();
                                values.clear();
                                values.put("county_name", weather.basic.location);
                                values.put("degree", weather.now.tmp);
                                values.put("status", weather.now.cond_txt);
                                values.put("weatherString",responseText);
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
                    }).start();
                }

                @Override
                public void onFailure(final Call call, IOException e) {
                    new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            if (getIntent() != null) {
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
                            }
                            if (TextUtils.isEmpty(value)) {
                                showAllWeather();
                            }
                        }
                    }).start();
                }
            });
        }
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
