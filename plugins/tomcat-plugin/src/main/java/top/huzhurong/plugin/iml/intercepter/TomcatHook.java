package top.huzhurong.plugin.iml.intercepter;

import org.apache.catalina.connector.Request;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.MimeHeaders;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.common.trace.Span;
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
                org.apache.coyote.Request coyoteRequest = request.getCoyoteRequest();
                MimeHeaders mimeHeaders = coyoteRequest.getMimeHeaders();
                MessageBytes traceHeader = mimeHeaders.addValue("traceId");
                traceId = UUID.randomUUID().toString();
                traceHeader.setString(traceId);
                //trace
                Trace trace = TraceContext.getContext();
                trace.setTraceId(traceId);
                Span rootSpan = new Span();
                rootSpan.setTag("tomcat");
                rootSpan.setUrl(request.getDecodedRequestURI());
                rootSpan.setsTime(System.currentTimeMillis());
                rootSpan.setSpanId(UUID.randomUUID().toString());
                trace.setRootSpan(rootSpan);
            }
        }
    }

    @Override
    public void out(Object result, Object cur, Object[] args) {
        try {
            Trace context = TraceContext.getContext();
            Span rootSpan = context.getRootSpan();
            rootSpan.seteTime(System.currentTimeMillis());
            //计算,写入定时线程队列，或是异步写入文件
            System.out.println("请求url:" + rootSpan.getUrl() + "\t耗时:" + (rootSpan.geteTime() - rootSpan.getsTime()) + "(ms)");
        } finally {
            TraceContext.removeContext();
        }
    }

    @Override
    public void error(Throwable ex, Object curObject, Object[] args) {

    }
}
