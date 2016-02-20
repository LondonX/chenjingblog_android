package com.londonx.lutil.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 英伦 on 2015/3/18.
 * DBHelper
 */
public class DBHelper extends SQLiteOpenHelper {
    public static DBHelper get(Context context) {
        return new DBHelper(context, "cache.db", null, 2);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists t_cache(id integer primary key autoincrement ," +
                "url text,response text)";
        String sql2 = "create table if not exists t_user(id integer primary key autoincrement ," +
                "username text,password text)";
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > 1) {
            String sql = "create table if not exists t_user(id integer primary key autoincrement ," +
                    "username text,password text)";
            db.execSQL(sql);
        }
    }
}
