package com.example.fanxh.simpleweather.gson;

/**
 * Created by fanxh on 2017/10/26.
 */

public class AQI {
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public class City {
        public String aqi;
        public String pm25;

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }
    }
}
