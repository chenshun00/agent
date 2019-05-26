package top.huzhurong.plugin.impl.intercepter;

import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;


/**
 * @author chenshun00@gmail.com
 * @since 2018/10/14
 */
public class MysqlHook implements BaseHook {

    private MysqlHook() {
    }

    public static final MysqlHook Instance = new MysqlHook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        System.out.println("into \t" + BeanMethodRegister.get(index).toString());
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        System.out.println("out \t" + BeanMethodRegister.get(index).toString());
    }

    @Override
    public void error(Throwable ex, Object curObject, int index, Object[] args) {

    }
}
