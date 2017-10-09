package com.example.fanxh.weixin_and_news;

/**
 * Created by fanxh on 2017/10/8.
 */

public class NewsData {
    private String newsImgUrl;
    private String newsTitle;
    private String newsDetails;
    private String newsUrl;

    public NewsData(){
        this.newsImgUrl = newsImgUrl;
        this.newsTitle = newsTitle;
        this.newsDetails = newsDetails;
        this.newsUrl = newsUrl;
    }

    public String getNewsImgUrl() {
        return newsImgUrl;
    }

    public void setNewsImgUrl(String newsImgUrl) {
        this.newsImgUrl = newsImgUrl;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDetails() {
        return newsDetails;
    }

    public void setNewsDetails(String newsDetails) {
        this.newsDetails = newsDetails;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
