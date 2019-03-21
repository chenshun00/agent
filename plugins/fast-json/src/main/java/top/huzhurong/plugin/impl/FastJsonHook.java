package top.huzhurong.plugin.impl;

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
    public void into(Object curObject, Object[] args) {

    }

    @Override
    public void out(Object result, Object cur, Object[] args) {

    }

    @Override
    public void error(Throwable ex, Object curObject, Object[] args) {

    }
}
