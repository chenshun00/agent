package top.huzhurong.test.common.trace;

import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Trace {
    private String traceId;
    private List<Span> spans;

    public String getTraceId() {
        return traceId;
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
