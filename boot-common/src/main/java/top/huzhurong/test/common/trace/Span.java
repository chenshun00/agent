package top.huzhurong.test.common.trace;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Span {
    public boolean error = false;
    public int index = -1;
    private String url;
    private String node = "1";
    private String spanId = UUID.randomUUID().toString().replaceAll("-", "");
    private String parentSpanId = null;
    private String tag;
    private long startTime = System.currentTimeMillis();
    private long endTIme;
    private BuilderStack<SpanEvent> spanEventStack = new BuilderStack<SpanEvent>();
    private Map<SpanEvent, Integer> eventIndex = new HashMap<SpanEvent, Integer>();

    public void push(SpanEvent spanEvent) {
        index++;
        spanEventStack.push(spanEvent);
        eventIndex.put(spanEvent, index);
    }

    public SpanEvent pop() {
        try {
            while (true) {
                SpanEvent pop = spanEventStack.pop(index--);
                if (eventIndex.get(pop) != null) {
                    eventIndex.remove(pop);
                    return pop;
                }
            }
        } finally {
            index = spanEventStack.size();
        }
    }

    public SpanEvent getOne() {
        return spanEventStack.getOne();
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

    @Override
    public String toString() {
        return "Span{" +
                "error=" + error +
                ", index=" + index +
                ", url='" + url + '\'' +
                ", node='" + node + '\'' +
                ", spanId='" + spanId + '\'' +
                ", parentSpanId='" + parentSpanId + '\'' +
                ", tag='" + tag + '\'' +
                ", startTime=" + startTime +
                ", endTIme=" + endTIme +
                ", spanEventStack=" + spanEventStack +
                '}';
    }
}
