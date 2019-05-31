package top.huzhurong.test.bootcore;

import top.huzhurong.test.bootcore.bean.BeanInfo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanMethodRegister {
    private final static AtomicLong count = new AtomicLong(0);

    private static BeanInfo[] beanInfos = new BeanInfo[1024];

    public synchronized static long hookKey(String className, String method) {
        long key = count.incrementAndGet();
        beanInfos[(int) key] = new BeanInfo(className, method);
        return key;
    }

    public static BeanInfo get(long key) {
        return beanInfos[(int) key];
    }
}
