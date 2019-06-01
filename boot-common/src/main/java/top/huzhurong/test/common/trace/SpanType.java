package top.huzhurong.test.common.trace;

/**
 * @author chenshun00@gmail.com
 * @since 2019/6/1
 */
public enum SpanType {
    http("http"), dubbo("dubbo");


    private String value;

    SpanType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
