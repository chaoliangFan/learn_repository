package com.example.administrator.appupdate;

import android.app.Application;
import android.content.Context;

/**
 * Created by fanxh on 2017/10/20.
 */

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }
}
