package top.huzhurong.test.common.trace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Trace {
    private String traceId;
    private Span rootSpan;
    private List<Span> spans = new ArrayList<Span>(16);

    public String getTraceId() {
        return traceId;
    }

    public Span getRootSpan() {
        return rootSpan;
    }

    public void setRootSpan(Span rootSpan) {
        this.rootSpan = rootSpan;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public List<Span> getSpans() {
        return spans;
    }

    public void setSpans(List<Span> spans) {
        this.spans = spans;
    }
}
