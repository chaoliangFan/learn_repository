package com.example.fanxh.simpleweather.gson;

/**
 * Created by fanxh on 2017/10/26.
 */

public class Daily_forecast {
    public String date;
    public Astro astro;
    public Cond cond;
    public Tmp tmp;
    public Wind wind;
    public class Astro{
        public String sr;
        public String ms;
    }
    public  class Cond{
        public String txt_d;
        public String txt_n;
    }
    public class Tmp{
        public String max;
        public String min;
    }
    public class Wind {
        public String dir;
        public String spd;
    }
}
