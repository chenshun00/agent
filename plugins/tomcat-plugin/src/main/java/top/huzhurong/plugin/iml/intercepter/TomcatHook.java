package top.huzhurong.plugin.iml.intercepter;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.bootcore.bean.BeanInfo;
import top.huzhurong.test.common.trace.*;

import java.util.Map;

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

            String traceId = request.getHeader("traceId");
            String parentSpanId = request.getHeader("parentSpanId");
            Trace<SpanEvent> trace = TraceContext.setTrace(Trace.<SpanEvent>newTrace(request.getRequestURI()));
            trace.setSpan(new Span<SpanEvent>());
            Span<SpanEvent> span = trace.getSpan();
            span.setUrl(request.getRequestURI());
            SpanEvent spanEvent = new SpanEvent();
            spanEvent.setClassName(beanInfo.getClassName());
            spanEvent.setMethod(beanInfo.getMethodName());
            spanEvent.setLine(beanInfo.getLineNumber());
            spanEvent.setSpanId(span.getSpanId());
            spanEvent.setStartTime(span.getStartTime());
            span.push(spanEvent);
            if (traceId != null && parentSpanId != null) {
                trace.setTraceId(traceId);
                span.setParentSpanId(parentSpanId);
            }
        }
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        try {
            Trace<SpanEvent> trace = TraceContext.getContext();
            if (trace == null) return;
            Request request = (Request) args[0];
            Response response = (Response) args[1];
            trace.code = response.getResponse().getStatus();
            Span<SpanEvent> span = trace.getSpan();
            span.setEndTIme(System.currentTimeMillis());
            SpanEvent spanEvent = span.pop();
            spanEvent.setEndTime(System.currentTimeMillis());
            StringBuilder builder = new StringBuilder();
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                String key = stringEntry.getKey();
                String[] value = stringEntry.getValue();
                builder.append(key).append(":");
                for (String s : value) {
                    builder.append(s).append(",");
                }
            }
            spanEvent.setTag(builder.toString());
            SentService.push(trace);
        } finally {
            TraceContext.removeContext();
        }
    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {

    }
}
