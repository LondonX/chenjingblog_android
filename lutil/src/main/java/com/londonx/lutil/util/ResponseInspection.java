package com.londonx.lutil.util;

import com.londonx.lutil.entity.LResponse;

/**
 * Created by london on 15/12/7.
 * check the response validation
 */
public class ResponseInspection {
    public static boolean isJson(LResponse response) {
        return response.body.length() != 0
                && (response.body.startsWith("{") && response.body.endsWith("}")
                || response.body.startsWith("[") && response.body.endsWith("]"));
    }
}
