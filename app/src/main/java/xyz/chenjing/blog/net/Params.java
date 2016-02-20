package xyz.chenjing.blog.net;

import java.util.HashMap;

/**
 * Created by london on 16/2/19.
 * params used in {@link com.londonx.lutil.util.LRequestTool}
 */
public class Params extends HashMap<String, Object> {
    public Params put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
