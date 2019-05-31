package top.huzhurong.plugin.impl.intercepter;

import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.bean.Builder;


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
        Builder.handleOutTrace();
    }

    @Override
    public void error(Throwable ex,  int index, Object[] args) {
        Builder.handleErrorTrace(ex);
    }
}
