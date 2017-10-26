package com.example.fanxh.simpleweather.gson;

/**
 * Created by fanxh on 2017/10/26.
 */

public class Basic {
    public String city;
    public String id;
    public Update updata;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Update getUpdata() {
        return updata;
    }

    public void setUpdata(Update updata) {
        this.updata = updata;
    }

    public class Update{
        public String loc;

        public String getLoc() {
            return loc;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }
    }
}
