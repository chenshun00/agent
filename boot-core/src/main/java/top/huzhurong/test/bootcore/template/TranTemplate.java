package top.huzhurong.test.bootcore.template;

import top.huzhurong.test.bootcore.TransformCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/23
 */
public class TranTemplate {
    //使用WeakHashMap会造成类还没有被加载，这个class就被回收掉了
    private Map<String, Class<? extends TransformCallback>> param = new HashMap<String, Class<? extends TransformCallback>>();


    public void addTranCallback(String inf, Class<? extends TransformCallback> transformCallback) {
        if (param != null) param.put(inf, transformCallback);
    }

    public Class<? extends TransformCallback> getTranCallback(String inf) {
        if (param == null) return null;
        Class<? extends TransformCallback> remove = param.remove(inf);
        if (param.size() == 0) {
            param = null;
        }
        return remove;
    }
}
