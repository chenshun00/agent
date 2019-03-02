package top.huzhurong.test.bootcore.template;

import top.huzhurong.test.bootcore.TransformCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/23
 */
public class TranTemplate {
    private Map<String, Class<? extends TransformCallback>> param = new HashMap<String, Class<? extends TransformCallback>>();


    public void addTranCallback(String inf, Class<? extends TransformCallback> transformCallback) {
        param.put(inf, transformCallback);
    }

    public Class<? extends TransformCallback> getTranCallback(String inf) {
        return param.get(inf);
    }
}
