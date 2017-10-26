package com.example.fanxh.simpleweather.gson;

/**
 * Created by fanxh on 2017/10/26.
 */

public class Daily_forecast {
    public String data;
    public Tmp tmp;
    private Cond cond;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Tmp getTmp() {
        return tmp;
    }

    public void setTmp(Tmp tmp) {
        this.tmp = tmp;
    }

    public Cond getCond() {
        return cond;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public class Tmp{
        public String max;
        public String min;

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }
    }
    public class Cond{
        public String txt_d;

        public String getTxt_d() {
            return txt_d;
        }

        public void setTxt_d(String txt_d) {
            this.txt_d = txt_d;
        }
    }
}
