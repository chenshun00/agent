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
    public static SpanEvent buildContext(int index) {
        Trace<SpanEvent> trace = TraceContext.getContext();
        if (trace == null) return null;

        Span<SpanEvent> span = trace.getSpan();
        BeanInfo beanInfo = BeanMethodRegister.get(index);

        SpanEvent spanEvent = new SpanEvent();
        spanEvent.setClassName(beanInfo.getClassName());
        spanEvent.setMethod(beanInfo.getMethodName());
        spanEvent.setLine(beanInfo.getLineNumber());
        spanEvent.setSpanId(span.getSpanId());
        span.push(spanEvent);
        return spanEvent;
    }

    public static void handleOutTrace() {
        Trace<SpanEvent> trace = TraceContext.getContext();
        if (trace == null) return;
        Span<SpanEvent> span = trace.getSpan();
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
        Trace<SpanEvent> trace = (Trace<SpanEvent>) TraceContext.getContext();
        if (trace == null) return;
        Span<SpanEvent> span = trace.getSpan();
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
}
