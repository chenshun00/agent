package top.huzhurong.test.common.trace;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class TraceContext<T> {

    private TraceContext() {
    }

    private static final ThreadLocal threadLocal = new ThreadLocal() {
        @Override
        protected Trace initialValue() {
            return null;
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Trace<? extends T> getContext() {
        return (Trace<? extends T>) threadLocal.get();
    }

    public static void removeContext() {
        threadLocal.remove();
    }

    @SuppressWarnings("unchecked")
    public static <T> Trace<? extends T> setTrace(Trace<? extends T> trace) {
        threadLocal.set(trace);
        return trace;
    }


}
