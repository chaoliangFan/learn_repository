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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AQI getAqi() {
        return aqi;
    }

    public void setAqi(AQI aqi) {
        this.aqi = aqi;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public List<Daily_forecast> getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(List<Daily_forecast> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }
}
