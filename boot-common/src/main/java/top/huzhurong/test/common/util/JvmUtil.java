package top.huzhurong.test.common.util;

import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/23
 */
public class JvmUtil {
    public static String jvmName(String javaName) {
        return javaName.replaceAll("\\.", "/");
    }

    public static String createTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String createSpanId() {
        return createTraceId();
    }
}
