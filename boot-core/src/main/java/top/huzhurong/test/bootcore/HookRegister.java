package top.huzhurong.test.bootcore;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/16
 */
public class HookRegister {
    private final static AtomicLong count = new AtomicLong(0);

    private static BaseHook[] hooks = new BaseHook[1024];

    public synchronized static long hookKey(BaseHook baseHook) {
        long key = count.incrementAndGet();
        hooks[(int) key] = baseHook;
        return key;
    }

    public static BaseHook get(long key) {
        return hooks[(int) key];
    }
}
