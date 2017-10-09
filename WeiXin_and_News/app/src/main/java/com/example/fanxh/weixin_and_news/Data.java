package com.example.fanxh.weixin_and_news;

/**
 * Created by fanxh on 2017/10/8.
 */

public class Data {

        private String icon;
        private String title;
        private String desc;
        private String url;

        public String getImgUrl() {
            return icon;
        }

        public void setImgUrl(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetails() {
            return desc;
        }

        public void setDetails(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
}
