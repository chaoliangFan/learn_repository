package com.example.fanxh.simpleweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by fanxh on 2017/11/14.
 */

public class DbUtil {
    private static SWDatabase swDatabase;
    private static SQLiteDatabase db;

    public static SQLiteDatabase getDb(Context context){
 //       swDatabase = new SWDatabase(context);
        swDatabase = SWDatabase.getDBInstance(context);
        db = swDatabase.getWritableDatabase();
        return db;
    }
}
