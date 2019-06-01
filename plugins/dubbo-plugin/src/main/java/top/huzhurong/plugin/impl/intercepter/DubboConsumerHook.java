package top.huzhurong.plugin.impl.intercepter;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.RpcResult;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.bootcore.bean.BeanInfo;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/26
 */
public class DubboConsumerHook implements BaseHook {

    public static DubboConsumerHook Instance = new DubboConsumerHook();

    //需要提供spanId，traceID来作为处理模型
    @Override
    public void into(Object curObject, int index, Object[] args) {
        Trace<SpanEvent> trace = TraceContext.getContext();
        if (trace == null) return;
        Span<SpanEvent> span = trace.getSpan();
        if (span == null) return;
        assert args.length == 1;
        assert args[0] instanceof Invocation;
        Invocation invocation = (Invocation) args[0];
        Map<String, String> attachments = invocation.getAttachments();
        BeanInfo beanInfo = BeanMethodRegister.get(index);
        String traceId = trace.getTraceId();
        String spanId = span.getSpanId();
        SpanEvent spanEvent = new SpanEvent();
        spanEvent.setClassName(attachments.get("interface"));
        spanEvent.setMethod(invocation.getMethodName());
        spanEvent.setLine(beanInfo.getLineNumber());
        spanEvent.setSpanId(spanId);
        span.push(spanEvent);
        //设置dubbo的attach数据
        attachments.put("traceId", traceId);
        attachments.put("parentSpanId", spanId);
    }

    //结果
    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        Trace<SpanEvent> trace = TraceContext.getContext();
        if (trace == null) return;
        Span<SpanEvent> span = trace.getSpan();
        if (span == null) return;
        assert result instanceof RpcResult;
        SpanEvent spanEvent = span.pop();
        spanEvent.setEndTime(System.currentTimeMillis());
    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {

    }
}
