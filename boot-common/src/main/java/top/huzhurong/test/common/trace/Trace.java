package top.huzhurong.test.common.trace;

import top.huzhurong.test.common.storge.Storge;

import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Trace {
    private String traceId;
    private String project;
    private Span span;
    private Storge<SpanEvent> storge;

    public static Trace newTrace(String project) {
        return new Trace(UUID.randomUUID().toString().replace("-", ""), project, StorgeFactory.getStorge());
    }

    public Trace(String traceId, String project, Storge storge) {
        this.traceId = traceId;
        this.project = project;
        this.storge = storge;
    }

    public Storge<SpanEvent> getStorge() {
        return storge;
    }

    public void setStorge(Storge storge) {
        this.storge = storge;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Span getSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }
}
