package top.huzhurong.test.common.util;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/23
 */
public class JvmUtil {
    public static String jvmName(String javaName) {
        return javaName.replaceAll("\\.", "/");
    }

    public static void main(String[] args) {
        System.out.println(jvmName("abc.aaa.bbb"));
    }
}
