package top.huzhurong.plugin.impl.intercepter;

import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.bean.Builder;
import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/25
 */
public class MybatisHook implements BaseHook {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    public static MybatisHook Instance = new MybatisHook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        Builder.buildContext(index);
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        Builder.handleOutTrace();
    }

    @Override
    public void error(Throwable ex, Object curObject, int index, Object[] args) {
        Builder.handleErrorTrace(ex);
    }
}
