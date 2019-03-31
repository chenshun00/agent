package top.huzhurong.test.common.trace;

import java.util.Stack;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Span {
    private String spanId;
    private String parentSpanId;
    private String url;
    private String tag;
    private long sTime;
    private long eTime;
    private Stack<SpanEvent> spanEventStack = new Stack<SpanEvent>();

    public void push(SpanEvent spanEvent) {
        spanEventStack.push(spanEvent);
    }

    public SpanEvent pop() {
        return spanEventStack.pop();
    }

    public String getUrl() {
        return url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public long getsTime() {
        return sTime;
    }

    public void setsTime(long sTime) {
        this.sTime = sTime;
    }

    public long geteTime() {
        return eTime;
    }

    public void seteTime(long eTime) {
        this.eTime = eTime;
    }
}
