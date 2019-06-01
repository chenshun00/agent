package top.huzhurong.test.bootcore;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/31
 */
public class Invoke {

    public static String OWNER = "top/huzhurong/test/bootcore/Invoke";

    public static String INTO_NAME = "into";
    public static String OUT_NAME = "out";
    public static String ERROR_NAME = "error";

    public static String INTO_METHOD = "(JLjava/lang/Object;[Ljava/lang/Object;)V";
    public static String OUT_METHOD = "(Ljava/lang/Object;JLjava/lang/Object;[Ljava/lang/Object;)V";
    public static String ERROR_METHOD = "(Ljava/lang/Throwable;JLjava/lang/Object;[Ljava/lang/Object;)V";

    public static void into(long index, Object cur, Object[] args) {
        BaseHook baseHook = HookRegister.get(index);
        baseHook.into(cur, (int) index, args);
    }


    public static void out(Object ret, long index, Object cur, Object[] args) {
        BaseHook baseHook = HookRegister.get(index);
        baseHook.out(ret, cur, (int) index, args);
    }


    public static void error(Throwable exception, long index, Object cur, Object[] args) {
        BaseHook baseHook = HookRegister.get(index);
        baseHook.error(exception, (int) index, args);
    }
}
