package top.huzhurong.plugin.impl.intercepter;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.bean.Builder;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;

/**
 * @author chenshun00@gmail.com
 * @since 2019/6/1
 */
public class HttpClient4Hook implements BaseHook {

    public static HttpClient4Hook Instance = new HttpClient4Hook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        assert args.length == 3;
        Trace<SpanEvent> trace = TraceContext.getContext();
        if (trace == null) return;
        Span<SpanEvent> span = trace.getSpan();
        if (span == null) return;
        SpanEvent spanEvent = Builder.buildContext(index);
        if (spanEvent == null) return;
        HttpRequestBase httpRequest = (HttpRequestBase) args[1];
        URI uri = httpRequest.getURI();
        httpRequest.addHeader("traceId", trace.getTraceId());
        httpRequest.addHeader("parentSpanId", span.getSpanId());
        spanEvent.setTag(URLDecoder.decode(uri.toString()));

        if (httpRequest instanceof HttpPost) {
            HttpPost post = (HttpPost) httpRequest;
            HttpEntity entity = post.getEntity();
            try {
                InputStream content = entity.getContent();
                int available = content.available();
                byte[] bytes = new byte[available];
                int read = content.read(bytes);
                if (read == -1) return;
                spanEvent.setParam(new String(bytes));
            } catch (IOException ignore) {

            }
        }
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        Builder.handleOutTrace();
    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {
        Builder.handleErrorTrace(ex);
    }
}
