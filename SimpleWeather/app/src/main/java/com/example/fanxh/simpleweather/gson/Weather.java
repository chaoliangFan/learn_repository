package com.example.fanxh.simpleweather.gson;

import java.util.List;

/**
 * Created by fanxh on 2017/10/26.
 */

public class Weather {
    public String status;
    public AQI aqi;
    public Basic basic;
    public Now now;
    public Suggestion suggestion;
    public List<Daily_forecast> daily_forecast;
    public List<Hourly_forecast> hourly_forecast;
}
