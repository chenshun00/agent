package top.huzhurong.test.common.trace;

import top.huzhurong.test.common.util.JvmUtil;

import java.util.Stack;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Span<T> {
    public boolean error = false;
    public String type = SpanType.http.getValue();
    public int index = -1;
    private String url;
    private String spanId = JvmUtil.createSpanId();
    private String parentSpanId = null;
    private String tag;
    private long startTime = System.currentTimeMillis();
    private long endTIme;
    private Stack<T> spanEventStack = new Stack<T>();
    private Stack<T> otherStack = new Stack<T>();

    public void push(T t) {
        index++;
        if (t instanceof SpanEvent) {
            SpanEvent spanEvent = (SpanEvent) t;
            spanEvent.setSequence((short) index);
        }
        spanEventStack.push(t);
    }

    /**
     * 先pop一次
     */
    public T pop() {
        try {
            T pop = spanEventStack.pop();
            otherStack.push(pop);
            return pop;
        } finally {
            index--;
        }
    }

    //for local test
    public T getOne() {
        return otherStack.pop();
    }

    public Stack<T> getAll() {
        return otherStack;
    }

    public int size() {
        return otherStack.size();
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTIme;
    }

    public void setEndTIme(long endTIme) {
        this.endTIme = endTIme;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Span{" +
                "error=" + error +
                ", index=" + index +
                ", url='" + url + '\'' +
                ", spanId='" + spanId + '\'' +
                ", parentSpanId='" + parentSpanId + '\'' +
                ", tag='" + tag + '\'' +
                ", startTime=" + startTime +
                ", endTIme=" + endTIme +
                ", spanEventStack=" + spanEventStack +
                '}';
    }
}
