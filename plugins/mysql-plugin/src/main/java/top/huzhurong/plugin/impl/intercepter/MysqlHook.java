package top.huzhurong.plugin.impl.intercepter;

import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.bean.Builder;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;


/**
 * @author chenshun00@gmail.com
 * @since 2018/10/14
 */
public class MysqlHook implements BaseHook {

    public static final MysqlHook Instance = new MysqlHook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        Builder.buildContext(index);
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        //Builder.handleOutTrace();
        Trace<SpanEvent> trace = TraceContext.getContext();
        if (trace == null) return;
        Span<SpanEvent> span = trace.getSpan();
        SpanEvent spanEvent = span.pop();
        spanEvent.setEndTime(System.currentTimeMillis());
        int len = 39 + Integer.toHexString(cur.hashCode()).length();
        String sql = cur.toString().substring(len).trim().replaceAll("\n", "")
                .replaceAll("\\s+", " ");
        spanEvent.setTag(sql);
    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {
        Builder.handleErrorTrace(ex);
    }
}
