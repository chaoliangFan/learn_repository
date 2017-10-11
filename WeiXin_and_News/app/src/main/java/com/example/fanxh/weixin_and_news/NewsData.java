package com.example.fanxh.weixin_and_news;

/**
 * Created by fanxh on 2017/10/8.
 */

public class NewsData {
    private String icon;
    private String title;
    private String desc;
    private String url;

    public NewsData(String icon, String title, String desc, String url) {
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    //
//    @Override
//    public String toString() {
//        return "NewsData{" +
//                "icon='" + icon + '\'' +
//                ", title='" + title + '\'' +
//                ", desc='" + desc + '\'' +
//                ", url='" + url + '\'' +
//                '}';
//    }
//
//    public String getImgUrl() {
//        return icon;
//    }
//
//    public void setImgUrl(String icon) {
//        this.icon = icon;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDetails() {
//        return desc;
//    }
//
//    public void setDetails(String desc) {
//        this.desc = desc;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
}
