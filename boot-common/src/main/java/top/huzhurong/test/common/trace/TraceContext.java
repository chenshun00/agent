package top.huzhurong.test.common.trace;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class TraceContext {

    private ThreadLocal<Trace> threadLocal = new ThreadLocal<Trace>();

    private Trace trace;

    public Trace getTrace() {
        return threadLocal.get();
    }

    public void setTrace(Trace trace) {
        threadLocal.set(trace);
    }

    public void remove() {
        threadLocal.remove();
    }

}
