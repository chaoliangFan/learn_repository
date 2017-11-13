package com.example.fanxh.simpleweather.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;

import android.os.Build;
import android.text.TextUtils;

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
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    values.put("province_name", provinceObject.getString("name"));
                    values.put("province_code", provinceObject.getInt("id"));
                    db.insert("Province", null, values);
                    values.clear();
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
                    JSONObject cityObject = allCities.getJSONObject(i);
                    values.put("city_name", cityObject.getString("name"));
                    values.put("city_code", cityObject.getInt("id"));
                    values.put("province_id", provinceId);
                    db.insert("City", null, values);
                    values.clear();
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

                    values.put("county_name", countyObject.getString("name"));
                    values.put("weatherId", countyObject.getString("weather_id"));
                    values.put("city_id", cityId);
                    db.insert("County", null, values);
                    values.clear();
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
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
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

    @TargetApi(Build.VERSION_CODES.N)
    public static String getWeek(String pTime) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }
        return Week;
    }
}
