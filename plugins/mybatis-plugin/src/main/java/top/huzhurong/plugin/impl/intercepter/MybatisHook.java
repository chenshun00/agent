package top.huzhurong.plugin.impl.intercepter;

import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.bean.Builder;
import top.huzhurong.test.common.trace.SpanEvent;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/25
 */
public class MybatisHook implements BaseHook {

    public static MybatisHook Instance = new MybatisHook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        Builder.buildContext(index);
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        SpanEvent spanEvent = Builder.handleOutTrace();
        if (spanEvent == null) return;
        PreparedStatementHandler preparedStatementHandler = (PreparedStatementHandler) cur;
        String sql = preparedStatementHandler.getBoundSql().getSql();
        sql = sql.replaceAll("\n", "").replaceAll("\\s+", " ");
        spanEvent.setTag(sql);
    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {
        Builder.handleErrorTrace(ex);
    }
}
