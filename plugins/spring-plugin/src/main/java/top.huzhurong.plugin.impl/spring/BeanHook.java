package top.huzhurong.plugin.impl.spring;

import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.bean.Builder;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanHook implements BaseHook {

    static BeanHook Instance = new BeanHook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        Builder.buildContext(index);
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
