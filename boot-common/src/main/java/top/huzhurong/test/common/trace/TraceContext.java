package top.huzhurong.test.common.trace;

import java.util.List;

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
            return new Trace();
        }
    };

    private static final ThreadLocal<Span> localSpan = new ThreadLocal<Span>() {
        @Override
        protected Span initialValue() {
            return new Span();
        }
    };


    public static Trace getContext() {
        return threadLocal.get();
    }

    public static void removeContext() {
        threadLocal.remove();
    }

    public static void setSpan(Span span) {
        localSpan.set(span);
    }

    public static Span getSpan() {
        return localSpan.get();
    }

    public static void removeSpan() {
        List<Span> spans = getContext().getSpans();
        spans.add(getSpan());
        localSpan.remove();
    }

}
