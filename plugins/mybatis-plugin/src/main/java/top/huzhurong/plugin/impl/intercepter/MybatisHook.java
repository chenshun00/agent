package top.huzhurong.plugin.impl.intercepter;

import top.huzhurong.plugin.impl.spring.BeanHook;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/25
 */
public class MybatisHook implements BaseHook {

    public static BeanHook Instance = new BeanHook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        System.out.println("into \t" + BeanMethodRegister.get(index).toString());
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        System.out.println("out\t" + BeanMethodRegister.get(index).toString());
    }

    @Override
    public void error(Throwable ex, Object curObject, int index, Object[] args) {

    }
}
