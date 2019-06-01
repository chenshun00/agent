package top.huzhurong.plugin.impl.intercepter;

import org.apache.commons.httpclient.HttpMethod;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.bootcore.bean.Builder;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/26
 */
public class HttpClient3Hook implements BaseHook {

    public static HttpClient3Hook Instance = new HttpClient3Hook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        assert args.length == 3;
        Trace<SpanEvent> trace = TraceContext.getContext();
        if (trace == null) return;
        Span<SpanEvent> span = trace.getSpan();
        if (span == null) return;
        HttpMethod httpMethod = (HttpMethod) args[1];
        httpMethod.setRequestHeader("traceId", trace.getTraceId());
        httpMethod.setRequestHeader("parentSpanId", span.getSpanId());

        Builder.buildContext(index);
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        System.out.println("out\t" + BeanMethodRegister.get(index).toString());
    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {

    }
}
