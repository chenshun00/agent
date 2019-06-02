package top.huzhurong.plugin.impl.intercepter;

import com.alibaba.dubbo.rpc.Invocation;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.bootcore.bean.BeanInfo;
import top.huzhurong.test.common.trace.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/26
 */
public class DubboProviderHook implements BaseHook {

    public static DubboProviderHook Instance = new DubboProviderHook();

    //进入dubbo provider , 新创建一个trace和span
    @Override
    public void into(Object curObject, int index, Object[] args) {
        assert args.length == 1;
        assert args[0] instanceof Invocation;
        Invocation invocation = (Invocation) args[0];
        Map<String, String> attachments = invocation.getAttachments();
        String traceId = attachments.get("traceId");
        String parentSpanId = attachments.get("parentSpanId");
        if (traceId == null || parentSpanId == null) return;

        Trace<SpanEvent> trace = TraceContext.setTrace(Trace.<SpanEvent>newTrace(invocation.getMethodName()));
        Span<SpanEvent> span = new Span<SpanEvent>();
        span.type = SpanType.dubbo.getValue();
        trace.setSpan(span);
        span.setUrl(invocation.getInvoker().getInterface().getName() + "#" + invocation.getMethodName());

        span.setParentSpanId(parentSpanId);
        trace.setTraceId(traceId);
        BeanInfo beanInfo = BeanMethodRegister.get(index);
        SpanEvent spanEvent = new SpanEvent();
        spanEvent.setClassName(beanInfo.getClassName());
        spanEvent.setMethod(beanInfo.getMethodName());
        spanEvent.setLine(beanInfo.getLineNumber());
        spanEvent.setSpanId(span.getSpanId());
        spanEvent.setStartTime(span.getStartTime());
        spanEvent.setParam(Arrays.toString(invocation.getParameterTypes()) + ":" + Arrays.toString(invocation.getArguments()));
        span.push(spanEvent);
    }

    //处理完成 发送给collect
    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        try {
            Trace<SpanEvent> trace = TraceContext.getContext();
            if (trace == null) return;
            Span<SpanEvent> span = trace.getSpan();
            span.setEndTIme(System.currentTimeMillis());
            SpanEvent spanEvent = span.pop();
            spanEvent.setEndTime(System.currentTimeMillis());
            SentService.push(trace);
        } finally {
            TraceContext.removeContext();
        }
    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {

    }
}
