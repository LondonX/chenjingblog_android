package com.londonx.lutil.util;

import android.content.Context;
import android.widget.Toast;

import com.londonx.lutil.R;
import com.londonx.lutil.entity.LResponse;

/**
 * Created by london on 15/6/2.
 * ToastUtil, init() before use.
 */
public class ToastUtil {
    private static Context context;
    private static Toast toast;

    public static void init(Context context) {
        ToastUtil.context = context;
        toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        toast.show();
        toast.cancel();
    }

    public static void show(int resId) {
        show(context.getString(resId));
    }

    public static void show(String str) {
        toast.setText(str);
        toast.show();
    }

    /**
     * show server error code
     *
     * @param responseCode response code
     * @deprecated avoid using is directly, use {@code serverErr(LResponse response)} instead
     */
    @Deprecated
    public static void serverErr(int responseCode) {
        show(context.getString(R.string.server_error_) + responseCode);
    }

    public static void serverErr(LResponse response) {
        show(context.getString(R.string.server_error_) + response.responseCode);
    }
}
