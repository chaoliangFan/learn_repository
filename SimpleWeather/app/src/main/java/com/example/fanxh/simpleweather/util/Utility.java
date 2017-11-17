package com.example.fanxh.simpleweather.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.fanxh.simpleweather.R;
import com.example.fanxh.simpleweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by fanxh on 2017/10/23.
 */

public class Utility {
    public static Weather weather;
    public static ContentValues values = new ContentValues();

    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvincesResponse(String response, SQLiteDatabase db) {
        if (!TextUtils.isEmpty(response)) {
            db.beginTransaction();
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    values.clear();
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    values.put("province_name", provinceObject.getString("name"));
                    values.put("province_code", provinceObject.getInt("id"));
                    db.insert("Province", null, values);
                }
                db.setTransactionSuccessful();
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(String response, int provinceId, SQLiteDatabase db) {
        if (!TextUtils.isEmpty(response)) {
            db.beginTransaction();
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    values.clear();
                    JSONObject cityObject = allCities.getJSONObject(i);
                    values.put("city_name", cityObject.getString("name"));
                    values.put("city_code", cityObject.getInt("id"));
                    values.put("province_id", provinceId);
                    db.insert("City", null, values);
                }
                db.setTransactionSuccessful();
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的区或县级数据
     */
    public static boolean handleCountiesResponse(String response, int cityId, SQLiteDatabase db) {
        if (!TextUtils.isEmpty(response)) {
            db.beginTransaction();
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    values.clear();
                    values.put("county_name", countyObject.getString("name"));
                    values.put("weatherId", countyObject.getString("weather_id"));
                    values.put("city_id", cityId);
                    db.insert("County", null, values);
                }
                db.setTransactionSuccessful();
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherInformation = jsonArray.getJSONObject(0).toString();
            return (new Gson().fromJson(weatherInformation, Weather.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ActivityCollector{
        public static List<Activity> activities = new ArrayList<Activity>();
        public static void addActivity(Activity activity){
            activities.add(activity);
        }
        public static void finishAll(){
            for (Activity activity : activities){
                if (!activity.isFinishing()){
                    activity.finish();
                }
            }
        }
    }

    /**
     *获取星期情况
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static String getWeek(String pTime) {
        String week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(pTime)) {
            try {
                c.setTime(format.parse(pTime));
                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    week += "日";
                }
                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    week += "一";
                }
                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                    week += "二";
                }
                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                    week += "三";
                }
                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                    week += "四";
                }
                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    week += "五";
                }
                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    week += "六";
                }
            } catch (Exception e) {
                week += "非日期或格式有误";
                e.printStackTrace();
            }
        }
            return week;
    }

    public static void setImage(String string, ImageView imageView) {
        final String SUN = "晴";
        final String OVERCAST = "阴";
        final String CLOUDY = "多云";
        final String LIGHT_RAIN = "小雨";
        final String MODERATE_RAIN = "中雨";
        final String HEAVY_RAIN = "大雨";
        final String SHOWER_RAIN = "阵雨";
        final String THUNDERSHOWER = "雷阵雨";
        final String LIGHT_SNOW = "小雪";
        if (!TextUtils.isEmpty(string) && imageView != null) {
            switch (string) {

                case SUN:
                    imageView.setImageResource(R.drawable.i_sun);
                    break;
                case OVERCAST:
                    imageView.setImageResource(R.drawable.i_overcast);
                    break;
                case CLOUDY:
                    imageView.setImageResource(R.drawable.i_cloudy);
                    break;
                case LIGHT_RAIN:
                    imageView.setImageResource(R.drawable.i_light_rain);
                    break;
                case MODERATE_RAIN:
                    imageView.setImageResource(R.drawable.i_moderate_rain);
                    break;
                case HEAVY_RAIN:
                    imageView.setImageResource(R.drawable.i_heavy_rain);
                    break;
                case SHOWER_RAIN:
                    imageView.setImageResource(R.drawable.i_shower_rain);
                    break;
                case THUNDERSHOWER:
                    imageView.setImageResource(R.drawable.i_thundershower);
                    break;
                case LIGHT_SNOW:
                    imageView.setImageResource(R.drawable.i_light_snow);
                    break;
                default:
                    break;
            }
        }
    }
}
