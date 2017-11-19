package com.example.fanxh.simpleweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * Created by fanxh on 2017/11/14.
 */

public class DbUtil {

    private static SWDatabase swDatabase;
    private static SQLiteDatabase db;
    private static String databaseFileName = "/data/data/com.example.fanxh.simpleweather/simpleweather.db";

    public static SQLiteDatabase getDb(Context context){

        db = SQLiteDatabase.openOrCreateDatabase(databaseFileName,null);
//        swDatabase = SWDatabase.getDBInstance(context);
//        db = swDatabase.getWritableDatabase();
        return db;
    }
}
