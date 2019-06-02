package top.huzhurong.test.common.trace;

import top.huzhurong.test.common.util.JvmUtil;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Trace<T> {
    private String traceId;
    private String request;
    private Span<T> span;
    public Integer code;

    public static <T> Trace<T> newTrace(String request) {
        return new Trace<T>(JvmUtil.createTraceId(), request);
    }

    public Trace(String traceId, String request) {
        this.traceId = traceId;
        this.request = request;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Span<T> getSpan() {
        return span;
    }

    public void setSpan(Span<T> span) {
        this.span = span;
    }

    @Override
    public String toString() {
        return "Trace{" +
                "traceId='" + traceId + '\'' +
                ", request='" + request + '\'' +
                ", span=" + span +
                '}';
    }
}
