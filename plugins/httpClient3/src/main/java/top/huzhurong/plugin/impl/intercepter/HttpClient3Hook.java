package top.huzhurong.plugin.impl.intercepter;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.bean.Builder;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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
        HttpMethodParams params = httpMethod.getParams();
        assert params != null;
        try {
            Field parameters = params.getClass().getSuperclass().getDeclaredField("parameters");
            parameters.setAccessible(true);
            Object param = parameters.get(params);
            StringBuilder builder = new StringBuilder();
            if (param != null) {
                @SuppressWarnings("unchecked")
                HashMap<String, Object> map = (HashMap) param;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    builder.append(key).append(":").append(value).append(",");
                }
            }
            SpanEvent spanEvent = Builder.buildContext(index);
            if (spanEvent == null) return;
            String name = httpMethod.getName();//get post
            String queryString = httpMethod.getQueryString();//可能是参数
            HostConfiguration hostConfiguration = httpMethod.getHostConfiguration();
            String path = httpMethod.getPath();
            if (queryString != null) {
                path += "?" + queryString;
            }
            spanEvent.setParam(builder.toString());
            spanEvent.setTag(hostConfiguration.getHostURL() + path + "===" + name);
        } catch (Exception ignore) {

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
