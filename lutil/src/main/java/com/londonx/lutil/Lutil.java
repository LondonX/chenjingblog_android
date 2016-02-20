package com.londonx.lutil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.londonx.lutil.util.ToastUtil;

/**
 * Created by london on 15/5/29.
 * Lutil, init() before anything
 * Main update 2015-07-20 10:08:30
 */
public class Lutil {
    public static final String KEY_USER = "user";
    public static final String KET_THEME = "theme";
    public static Context context;
    public static SharedPreferences preferences;

    public static void init(Context context) {
        Lutil.context = context;
        preferences = context.
                getSharedPreferences(context.getPackageName() + "_pref", Activity.MODE_PRIVATE);
        ToastUtil.init(context);
    }
}
