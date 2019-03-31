package top.huzhurong.plugin.impl.spring;

import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanHook implements BaseHook {

    public static BeanHook Instance = new BeanHook();

    @Override
    public void into(Object curObject, Object[] args) {

    }

    @Override
    public void into(int index, Object[] args) {
        System.out.print("into \t");
        System.out.println(BeanMethodRegister.get(index));
    }

    @Override
    public void out(Object result, Object cur, Object[] args) {


    }

    @Override
    public void out(Object result, int index, Object[] args) {
        System.out.print("out\t");
        System.out.println(BeanMethodRegister.get(index));
    }

    @Override
    public void error(Throwable ex, Object curObject, Object[] args) {

    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {

    }
}
