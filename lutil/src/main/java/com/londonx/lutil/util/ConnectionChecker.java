package com.londonx.lutil.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.londonx.lutil.Lutil;

/**
 * Created by london on 15/11/23.
 * Connection status checker
 */
public class ConnectionChecker {
    /**
     * @return true if network is functional
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)
                Lutil.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * @return network status
     */
    public static NetworkStatus getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                Lutil.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetworkStatus.Non;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetworkStatus.WiFi;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            return NetworkStatus.Cell;
        } else {
            return NetworkStatus.Cell;
        }
    }

    public enum NetworkStatus {
        WiFi, Cell, Non
    }
}
