package com.example.fanxh.simpleweather;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by fanxh on 2017/11/19.
 */

public class CopyDB {

    public static void copy(Context context) {
        String databaseFileName = "/data/data/com.example.fanxh.simpleweather/simpleweather.db";
        InputStream is = null;
        FileOutputStream fos = null;
        if (!(new File(databaseFileName).exists())) {
            try {
                is = context.getResources().openRawResource(R.raw.simpleweather);
                fos = new FileOutputStream(databaseFileName);
                byte[] buffer = new byte[4096];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
