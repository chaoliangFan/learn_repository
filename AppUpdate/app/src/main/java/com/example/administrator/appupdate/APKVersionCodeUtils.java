package com.example.administrator.appupdate;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2017/10/20.
 */

public class APKVersionCodeUtils {
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getVerName(Context mContext) {
        String verName = "";
        try {
            verName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
