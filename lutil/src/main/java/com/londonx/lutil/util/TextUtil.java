package com.londonx.lutil.util;

/**
 * Created by LondonX on 2015/4/24.
 * TextUtil
 */
public class TextUtil {
    public static String format(String from) {
        String[] ts = from.split("");
        String finalContent = "";
        for (int i = 0; i < ts.length; i++) {
            String t = ts[i];
            if (t.matches("\\p{P}+") && i + 1 < ts.length) {//如果是标点，且不是最后一个字
                if (!ts[i + 1].matches("\\p{P}+")) {//如果后一个字不是标点
                    if (!ts[i + 1].equals(" ") && !ts[i + 1].equals("\n")) {//如果后一个字不是空格也不是换行
                        t += " ";
                    }
                }
            }
            if (i == 72) {
                System.out.print("asd");
            }
            finalContent += t;
        }
        return finalContent;
    }
}
