package top.huzhurong.test.common.trace;

import top.huzhurong.test.common.storge.Storge;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/21
 */
public class StorgeFactory {
    private static Storge storge = new LogStorge();

    public static Storge getStorge() {
        return storge;
    }
}
