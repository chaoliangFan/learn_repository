package com.example.fanxh.simpleweather.gson;

/**
 * Created by fanxh on 2017/10/26.
 */

public class Suggestion {
    public Comf comf;
    public Cw cw;
    public Sport sport;

    public Comf getComf() {
        return comf;
    }

    public void setComf(Comf comf) {
        this.comf = comf;
    }

    public Cw getCw() {
        return cw;
    }

    public void setCw(Cw cw) {
        this.cw = cw;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public class Comf{
        public String txt;

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }
    public class Cw{
        public String txt;

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }
    public class Sport{
        public String txt;

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }
}
