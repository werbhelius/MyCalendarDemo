package com.werb.mycalendardemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by acer-pc on 2016/4/18.
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table alarmlist(_id integer primary key autoincrement,title char(20),isAllday int(20)," +
                "isVibrate int(20),year int(20),month int(20),day int(20),startTimeHour int(20),startTimeMinute int(20),"+
        "endTimeHour int(20),endTimeMinute int(20),alarmTime char(20),alarmColor char(20),alarmTonePath char(20),local char(20),"+
        "description char(100),replay char(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
