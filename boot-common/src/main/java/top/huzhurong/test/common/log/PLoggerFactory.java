package top.huzhurong.test.common.log;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/1
 */
public final class PLoggerFactory {
    private static PLoggerBinder loggerBinder;

    public static void initialize(PLoggerBinder loggerBinder) {
        if (PLoggerFactory.loggerBinder == null) {
            PLoggerFactory.loggerBinder = loggerBinder;
        }
    }

    public static AgentLog getLogger(String name) {
        return loggerBinder.getLogger(name);
    }

    public static AgentLog getLogger(Class clazz) {
        if (clazz == null) {
            throw new NullPointerException("class must not be null");
        }
        return getLogger(clazz.getName());
    }
}
