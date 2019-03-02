package top.huzhurong.test.common;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/1
 */
public class HoldData {
    private static ThreadData[] threadData = new ThreadData[65535];

    public static void putData(int key, ThreadData data) {
        threadData[key] = data;
    }

    public static ThreadData getAndRemove(int key) {
        return threadData[key];
    }

}
