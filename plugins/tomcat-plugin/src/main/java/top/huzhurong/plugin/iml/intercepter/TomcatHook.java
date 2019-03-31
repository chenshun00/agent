package top.huzhurong.plugin.iml.intercepter;

import org.apache.catalina.connector.Request;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.common.storge.Storge;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public final class TomcatHook implements BaseHook {

    private TomcatHook() {
    }

    public static TomcatHook Instance = new TomcatHook();

    //添加一个请求头
    @Override
    public void into(Object curObject, Object[] args) {
        if (args[0] instanceof Request) {
            Request request = (Request) args[0];
            String traceId = request.getRequest().getHeader("traceId");
            if (traceId == null) {
                Trace trace = TraceContext.getContext();
                Span span = new Span();
                span.setTag("tomcat");
                span.setUrl(request.getDecodedRequestURI());
                span.setsTime(System.currentTimeMillis());
                span.setSpanId(UUID.randomUUID().toString().replaceAll("-", ""));
                trace.setSpan(span);
                SpanEvent spanEvent = new SpanEvent();
                spanEvent.setClassName("org.apache.catalina.core.StandardWrapperValve");
                spanEvent.setMethod("invoke");
                spanEvent.setSpanId(span.getSpanId());
                spanEvent.setStartTime(System.currentTimeMillis());
                span.push(spanEvent);
            }
        }
    }

    @Override
    public void into(int index, Object[] args) {

    }

    @Override
    public void out(Object result, Object cur, Object[] args) {
        try {
            Trace context = TraceContext.getContext();
            Span span = context.getSpan();
            span.seteTime(System.currentTimeMillis());
            SpanEvent pop = span.pop();
            Storge<SpanEvent> storge = context.getStorge();
            pop.setEndTime(System.currentTimeMillis());
            storge.storgeInfo(pop);
        } finally {
            TraceContext.removeContext();
        }
    }

    @Override
    public void out(Object result, int index, Object[] args) {

    }

    @Override
    public void error(Throwable ex, Object curObject, Object[] args) {

    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {

    }
}
