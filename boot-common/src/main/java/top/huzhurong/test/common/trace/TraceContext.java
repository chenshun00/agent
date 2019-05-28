package top.huzhurong.test.common.trace;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class TraceContext {

    private TraceContext() {
    }

    private static final ThreadLocal<Trace> threadLocal = new ThreadLocal<Trace>() {
        @Override
        protected Trace initialValue() {
            return null;
        }
    };

    public static Trace getContext() {
        return threadLocal.get();
    }

    public static void removeContext() {
        threadLocal.remove();
    }

    public static Trace setTrace(Trace trace) {
        threadLocal.set(trace);
        return trace;
    }


}
