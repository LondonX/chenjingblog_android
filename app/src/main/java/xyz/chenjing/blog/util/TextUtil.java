package xyz.chenjing.blog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by london on 16/2/20.
 * utils of text
 */
public class TextUtil {
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
//        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); // simple
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//complex
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
