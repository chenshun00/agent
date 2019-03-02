package top.huzhurong.test.bootcore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/16
 */
public class HookRegister {
    private final static AtomicLong count = new AtomicLong(0);

    private static Map<Long, Hook> hooks = new HashMap<Long, Hook>();


    public synchronized static long hookKey(Hook baseHook) {
        long key = count.incrementAndGet();
        hooks.put(key, baseHook);

        return key;
    }

    public static Hook get(long key) {
        return hooks.get(key);
    }
}
