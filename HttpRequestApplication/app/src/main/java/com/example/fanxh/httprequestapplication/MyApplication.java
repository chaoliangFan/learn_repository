package com.example.fanxh.httprequestapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by fanxh on 2017/10/13.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
