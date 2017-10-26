package com.example.fanxh.simpleweather.gson;

/**
 * Created by fanxh on 2017/10/26.
 */

public class Now {
    public String tmp;
    public Cond cond;

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public Cond getCond() {
        return cond;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public class Cond{
        public String txt;

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }
}
