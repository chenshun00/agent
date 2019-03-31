package top.huzhurong.test.bootcore;

import top.huzhurong.test.bootcore.bean.BeanInfo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanMethodRegister {
    private final static AtomicInteger count = new AtomicInteger(0);

    private static BeanInfo[] beanInfos = new BeanInfo[1024];

    public synchronized static int hookKey(String className, String method) {
        int key = count.incrementAndGet();
        beanInfos[key] = new BeanInfo(className, method);
        return key;
    }

    public static BeanInfo get(int key) {
        return beanInfos[key];
    }
}
