package top.huzhurong.plugin.iml.intercepter;

import org.apache.catalina.connector.Request;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.common.storge.Storge;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public final class TomcatHook implements BaseHook {

    private TomcatHook() {
    }

    public static TomcatHook Instance = new TomcatHook();

    @Override
    public void into(Object curObject, int index, Object[] args) {
        if (args[0] instanceof Request) {
            Request request = (Request) args[0];
            System.out.println("into \t" + BeanMethodRegister.get(index).toString() + "\t" + request.getRequestURI());
        }
    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        if (args[0] instanceof Request) {
            Request request = (Request) args[0];
            System.out.println("out \t" + BeanMethodRegister.get(index).toString() + "\t" + request.getRequestURI());
        }
    }

    @Override
    public void error(Throwable ex, Object curObject, int index, Object[] args) {

    }
}
