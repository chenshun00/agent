package top.huzhurong.test.common.trace;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/21
 */
public class SpanEvent {
    private String spanId;
    private long startTime;
    private long endTime;
    private String className;
    private String method;
    private String line;
    private String param;

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "SpanEvent{" +
                "spanId='" + spanId + '\'' +
                ", duration=" + (endTime - startTime) + "(ms)" +
                ", className='" + className + '\'' +
                ", method='" + method + '\'' +
                ", line='" + line + '\'' +
                '}';
    }
}
