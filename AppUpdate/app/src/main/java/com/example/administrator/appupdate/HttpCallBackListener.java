package com.example.administrator.appupdate;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
