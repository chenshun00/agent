package top.huzhurong.plugin.impl.spring;

import org.springframework.web.bind.annotation.ControllerAdvice;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.bean.Builder;
import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanHook implements BaseHook {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    public static BeanHook Instance = new BeanHook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        Builder.buildContext(index);
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        Trace trace = TraceContext.getContext();
        if (trace == null) return;
        Span span = trace.getSpan();
        SpanEvent spanEvent = span.pop();
        spanEvent.setEndTime(System.currentTimeMillis());
        try {
            if (cur.getClass().isAnnotationPresent(ControllerAdvice.class)) {
                SpanEvent controllerEvent = span.pop();
                controllerEvent.setEndTime(System.currentTimeMillis());
            }
        } catch (Exception ignore) {
            logger.error("[处理span出现异常] [{}] [{}]", cur.getClass().getName(), args);
        }
    }

    @Override
    public void error(Throwable ex, Object curObject, int index, Object[] args) {

    }
}
