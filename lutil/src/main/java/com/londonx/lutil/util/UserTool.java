package com.londonx.lutil.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.londonx.lutil.db.DBHelper;
import com.londonx.lutil.entity.LEntity;

import java.util.ArrayList;

/**
 * Created by LondonX on 2015/4/30.
 * UserTool
 */
public class UserTool {
    public static long rememberUser(Context context, String username, String password) {
        DBHelper helper = DBHelper.get(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isDbLockedByCurrentThread()) {
            return 0;
        }
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        long result = db.update("t_user", cv, "username= ?", new String[]{username});
        if (result == 0) {
            result = db.insert("t_user", null, cv);
        }
        db.close();
        return result;
    }

    public static ArrayList<SimpleUser> getSimpleUsers(Context context) {
        DBHelper helper = DBHelper.get(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from t_user";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        ArrayList<SimpleUser> simpleUsers = new ArrayList<>();
        while (cursor.moveToNext()) {
            simpleUsers.add(new UserTool().new SimpleUser(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }
        cursor.close();
        db.close();
        return simpleUsers;
    }

    public class SimpleUser extends LEntity {
        public String username;
        public String password;

        public SimpleUser(int id, String username, String password) {
            this.id = id;
            this.username = username;
            this.password = password;
        }
    }
}
