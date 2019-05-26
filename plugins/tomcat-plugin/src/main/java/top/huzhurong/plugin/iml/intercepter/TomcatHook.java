package top.huzhurong.plugin.iml.intercepter;

import org.apache.catalina.connector.Request;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.bootcore.bean.BeanInfo;
import top.huzhurong.test.common.storge.Storge;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

import java.util.Map;
import java.util.Stack;
import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public final class TomcatHook implements BaseHook {

    private TomcatHook() {
    }

    public static TomcatHook Instance = new TomcatHook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        if (args[0] instanceof Request) {

            BeanInfo beanInfo = BeanMethodRegister.get(index);

            Request request = (Request) args[0];
            Trace trace = TraceContext.getContext();
            trace.setRequest(request.getRequestURI());
            trace.setSpan(new Span());
            Span span = trace.getSpan();
            span.setUrl(request.getRequestURI());
            Stack<SpanEvent> spanEventStack = span.getSpanEventStack();

            SpanEvent spanEvent = new SpanEvent();
            spanEvent.setClassName(beanInfo.getClassName());
            spanEvent.setMethod(beanInfo.getMethodName());
            spanEvent.setLine(beanInfo.getLineNumber());
            spanEvent.setSpanId(span.getSpanId());
            spanEvent.setStartTime(span.getStartTime());
            spanEventStack.push(spanEvent);
        }
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        try {
            Trace trace = TraceContext.getContext();
            Span span = trace.getSpan();
            Stack<SpanEvent> spanEventStack = span.getSpanEventStack();
            SpanEvent pop = spanEventStack.pop();
            pop.setEndTime(System.currentTimeMillis());
            System.out.println(pop);
        } finally {
            TraceContext.removeContext();
        }
    }

    @Override
    public void error(Throwable ex, Object curObject, int index, Object[] args) {

    }
}
