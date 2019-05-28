package top.huzhurong.plugin.impl.intercepter;

import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.bootcore.bean.Builder;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/25
 */
public class DruidHook implements BaseHook {

    public static DruidHook Instance = new DruidHook();

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
