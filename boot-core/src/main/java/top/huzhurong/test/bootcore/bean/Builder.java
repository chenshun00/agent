package top.huzhurong.test.bootcore.bean;

import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/26
 */
public class Builder {
    public static void buildContext(int index) {
        Trace trace = TraceContext.getContext();
        if (trace == null) return;

        Span span = trace.getSpan();
        BeanInfo beanInfo = BeanMethodRegister.get(index);

        SpanEvent spanEvent = new SpanEvent();
        spanEvent.setClassName(beanInfo.getClassName());
        spanEvent.setMethod(beanInfo.getMethodName());
        spanEvent.setLine(beanInfo.getLineNumber());
        spanEvent.setSpanId(span.getSpanId());
        spanEvent.setStartTime(span.getStartTime());
        span.push(spanEvent);
    }

    public static void handleOutTrace() {
        Trace trace = TraceContext.getContext();
        if (trace == null) return;
        Span span = trace.getSpan();
        SpanEvent spanEvent = span.pop();
        spanEvent.setEndTime(System.currentTimeMillis());
    }

    /**
     * 这里的处理还有问题
     *
     * @param ex 异常信息
     */
    public static void handleErrorTrace(Throwable ex) {
        if (ex == null) return;
        Trace trace = TraceContext.getContext();
        if (trace == null) return;
        Span span = trace.getSpan();
        SpanEvent spanEvent = span.pop();
        spanEvent.setEndTime(System.currentTimeMillis());
        if (span.error) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(256);
        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            stringBuilder.append(stackTraceElement.getFileName()).append("\t").append(stackTraceElement.getClassName()).append(".").append(stackTraceElement.getMethodName()).append("\n");
        }
        spanEvent.setErrorStack(stringBuilder.toString());
        span.error = true;
    }

    public static void handleTomcatOutTrace() {
        //将所有的数据导出到http当中
    }


}
