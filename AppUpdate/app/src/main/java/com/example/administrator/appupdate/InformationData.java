package com.example.administrator.appupdate;

/**
 * Created by Administrator on 2017/10/19.
 */

public class InformationData {
    private String appkey;
    private String channel;
    private String downurl;
    private String packname;
    private String filename;
    private String filesize;
    private String md5;
    private String version;
    private String user_version;
    private String updatelog;
    private String updatetype;
    private String need_update;


    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDownurl() {
        return downurl;
    }

    public void setDownurl(String downurl) {
        this.downurl = downurl;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUser_version() {
        return user_version;
    }

    public void setUser_version(String user_version) {
        this.user_version = user_version;
    }

    public String getUpdatelog() {
        return updatelog;
    }

    public void setUpdatelog(String updatelog) {
        this.updatelog = updatelog;
    }

    public String getUpdatetype() {
        return updatetype;
    }

    public void setUpdatetype(String updatetype) {
        this.updatetype = updatetype;
    }

    public String getNeed_update() {
        return need_update;
    }

    public void setNeed_update(String need_update) {
        this.need_update = need_update;
    }
}
