package top.huzhurong.test.common.trace;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class TraceContext {

    private TraceContext() {
    }

    private static final ThreadLocal threadLocal = new ThreadLocal() {
        @Override
        protected Trace initialValue() {
            return null;
        }
    };

    @SuppressWarnings("unchecked")
    public static Trace<SpanEvent> getContext() {
        return (Trace<SpanEvent>) threadLocal.get();
    }

    public static void removeContext() {
        threadLocal.remove();
    }

    @SuppressWarnings("unchecked")
    public static Trace<SpanEvent> setTrace(Trace<SpanEvent> trace) {
        threadLocal.set(trace);
        return trace;
    }


}
