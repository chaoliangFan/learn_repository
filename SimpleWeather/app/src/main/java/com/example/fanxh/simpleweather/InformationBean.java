package com.example.fanxh.simpleweather;

import org.litepal.crud.DataSupport;

/**
 * Created by fanxh on 2017/11/2.
 */

public class InformationBean extends DataSupport{
    private String city;
    private String degrees;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDegrees() {
        return degrees;
    }

    public void setDegrees(String degrees) {
        this.degrees = degrees;
    }
}
