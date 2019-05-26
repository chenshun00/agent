package top.huzhurong.plugin.impl.spring;

import org.springframework.web.bind.annotation.ControllerAdvice;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.bootcore.bean.BeanInfo;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

import java.util.Stack;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanHook implements BaseHook {

    public static BeanHook Instance = new BeanHook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        Trace trace = TraceContext.getContext();
        Span span = trace.getSpan();
        if (span == null) {
            TraceContext.removeContext();
            return;
        }

        BeanInfo beanInfo = BeanMethodRegister.get(index);
        Stack<SpanEvent> spanEventStack = span.getSpanEventStack();

        SpanEvent spanEvent = new SpanEvent();
        spanEvent.setClassName(beanInfo.getClassName());
        spanEvent.setMethod(beanInfo.getMethodName());
        spanEvent.setLine(beanInfo.getLineNumber());
        spanEvent.setSpanId(span.getSpanId());
        spanEvent.setStartTime(span.getStartTime());
        spanEventStack.push(spanEvent);
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        Trace trace = TraceContext.getContext();
        if (trace == null) {
            TraceContext.removeContext();
            return;
        }
        Span span = trace.getSpan();
        if (span == null) {
            TraceContext.removeContext();
            return;
        }
        Stack<SpanEvent> spanEventStack = span.getSpanEventStack();
        SpanEvent pop = spanEventStack.pop();
        pop.setEndTime(System.currentTimeMillis());
        System.out.println(pop);
        try {
            if (cur.getClass().isAnnotationPresent(ControllerAdvice.class)) {
                SpanEvent pop1 = spanEventStack.pop();
                pop1.setEndTime(System.currentTimeMillis());
                System.out.println(pop1);
            }
        } catch (Exception ignore) {

        }
    }

    @Override
    public void error(Throwable ex, Object curObject, int index, Object[] args) {

    }
}
