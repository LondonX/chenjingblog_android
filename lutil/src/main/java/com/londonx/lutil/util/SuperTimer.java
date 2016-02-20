package com.londonx.lutil.util;

/**
 * Created by london on 15/6/24.
 * SuperTimer
 */
public class SuperTimer {
    private static long timeInMillis = 0;

    /**
     * start
     */
    public static void start() {
        timeInMillis = System.currentTimeMillis();
    }

    /**
     * stop
     *
     * @return total time in Millis
     */
    public static long stop() {
        long time = System.currentTimeMillis() - timeInMillis;
        timeInMillis = System.currentTimeMillis();
        return time;
    }
}
