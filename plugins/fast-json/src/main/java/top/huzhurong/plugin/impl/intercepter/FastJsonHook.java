package top.huzhurong.plugin.impl.intercepter;

import top.huzhurong.test.bootcore.BaseHook;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/24
 */
public class FastJsonHook implements BaseHook {

    private FastJsonHook() {
    }

    public static final FastJsonHook Instance = new FastJsonHook();


    @Override
    public void into(Object curObject, int index, Object[] args) {

    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {

    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {

    }
}
