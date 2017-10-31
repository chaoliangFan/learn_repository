package com.example.fanxh.simpleweather.gson;

/**
 * Created by fanxh on 2017/10/26.
 */

public class AQI {
    public City city;
    public class City {
        public String aqi;
        public String pm25;
        public String qlty;
    }
}
