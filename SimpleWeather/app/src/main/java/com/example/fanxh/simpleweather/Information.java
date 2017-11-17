package com.example.fanxh.simpleweather;

/**
 * Created by fanxh on 2017/11/17.
 */

public class Information {
    public String county_name;
    public String status;
    private String degree;

    public String getCounty_name() {
        return county_name;
    }

    public void setCounty_name(String county_name) {
        this.county_name = county_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
