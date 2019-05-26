package top.huzhurong.test.common.trace;

import top.huzhurong.test.common.storge.Storge;

import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Trace {
    private String traceId;
    private String request;
    private Span span;
    private Storge<SpanEvent> storge;

    public static Trace newTrace(String request) {
        return new Trace(UUID.randomUUID().toString().replace("-", ""), request, StorgeFactory.getStorge());
    }

    public Trace(String traceId, String request, Storge storge) {
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

    public Span getSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }

    public Storge<SpanEvent> getStorge() {
        return storge;
    }

    public void setStorge(Storge<SpanEvent> storge) {
        this.storge = storge;
    }
}
