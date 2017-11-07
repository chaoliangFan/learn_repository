package com.example.fanxh.simpleweather.gson;

/**
 * Created by fanxh on 2017/10/26.
 */

public class DailyForecast {
    //湿度
    public String hum;
    //降水概率
    public String pcpn;
    //降水量
    public String pop;
    //气压
    public String pres;
    //紫外线能指数
    public String uv;
    //能见度
    public String vis;
    public String date;
    public Astro astro;
    public Cond cond;
    public Tmp tmp;
    public Wind wind;
    public class Astro{
        public String sr;
        public String ss;
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
