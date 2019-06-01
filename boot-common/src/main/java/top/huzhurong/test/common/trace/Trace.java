package top.huzhurong.test.common.trace;

import top.huzhurong.test.common.storge.Storge;

import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Trace<T> {
    private String traceId;
    private String request;
    private Span<T> span;
    private Storge<T> storge;

    public static <T> Trace<T> newTrace(String request) {
        return new Trace(UUID.randomUUID().toString().replace("-", ""), request, StorgeFactory.getStorge());
    }

    public Trace(String traceId, String request, Storge<T> storge) {
        this.traceId = traceId;
        this.request = request;
        this.storge = storge;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Span<T> getSpan() {
        return span;
    }

    public void setSpan(Span<T> span) {
        this.span = span;
    }

    public Storge<T> getStorge() {
        return storge;
    }

    public void setStorge(Storge<T> storge) {
        this.storge = storge;
    }

    @Override
    public String toString() {
        return "Trace{" +
                "traceId='" + traceId + '\'' +
                ", request='" + request + '\'' +
                ", span=" + span +
                ", storge=" + storge +
                '}';
    }
}
