package top.huzhurong.test.common.trace;

import java.util.Stack;
import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Span {
    private String url;
    private String node = "1";
    private String spanId = UUID.randomUUID().toString().replaceAll("-", "");
    private String parentSpanId = null;
    private String tag;
    private long startTime = System.currentTimeMillis();
    private long endTIme;
    private Stack<SpanEvent> spanEventStack = new Stack<SpanEvent>();

    public void push(SpanEvent spanEvent) {
        spanEventStack.push(spanEvent);
    }

    public SpanEvent pop() {
        return spanEventStack.pop();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getParentSpanId() {
        return parentSpanId;
    }

    public void setParentSpanId(String parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public Stack<SpanEvent> getSpanEventStack() {
        return spanEventStack;
    }

    public void setSpanEventStack(Stack<SpanEvent> spanEventStack) {
        this.spanEventStack = spanEventStack;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTIme() {
        return endTIme;
    }

    public void setEndTIme(long endTIme) {
        this.endTIme = endTIme;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
