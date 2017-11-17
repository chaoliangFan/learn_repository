package com.example.fanxh.simpleweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fanxh on 2017/11/5.
 */

public class SWDatabase extends SQLiteOpenHelper {
    public static SWDatabase swDatabase;
    private final static String DATABASE_NAME="SimpleWeather.db";
    private final static int VERSION = 2;
    public static final String CREATE_PROVINCE = "create table Province("
            +"id integer primary key autoincrement,"
            +"province_name text,"
            +"province_code integer)";
    public static final String CREATE_CITY = "create table City("
            +"id integer primary key autoincrement,"
            +"city_name text,"
            +"city_code integer,"
            + "province_id integer)";
    public static final String CREATE_COUNTY = "create table County("
            +"id integer primary key autoincrement,"
            +"county_name text,"
            +"weatherId text,"
            + "city_id integer)";
    public static final String CREATE_INFORMATION = "create table Information("
            +"id integer primary key autoincrement,"
            +"county_name text,"
            +"status text,"
            +"degree text,"
            +"weatherString text)";

    private SWDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

//    public SWDatabase(Context context) {
//        super(context, DATABASE_NAME, null, VERSION);
//    }

    public static synchronized SWDatabase getDBInstance(Context context) {
        if (swDatabase == null) {
            swDatabase = new SWDatabase(context,DATABASE_NAME,null,VERSION);
        }
        return swDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
        db.execSQL(CREATE_INFORMATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                db.execSQL("alter table Information add column weatherString text");
            case 2:
                db.execSQL(CREATE_INFORMATION);
            default:
        }
    }
}
