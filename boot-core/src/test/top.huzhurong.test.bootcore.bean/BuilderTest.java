package top.huzhurong.test.bootcore.bean;

import org.junit.Before;
import org.junit.Test;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;

import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/27
 */
public class BuilderTest {

    @Before
    public void setUp() throws Exception {
        BeanMethodRegister.hookKey("first", "1");
        BeanMethodRegister.hookKey("second", "2");
        BeanMethodRegister.hookKey("third", "3");
        BeanMethodRegister.hookKey("third1", "4");
        BeanMethodRegister.hookKey("third2", "5");
        BeanMethodRegister.hookKey("third3", "6");
    }

    @Test
    public void testTraceContext() {
        Trace trace = TraceContext.setTrace(Trace.newTrace(UUID.randomUUID().toString()));
        trace.setSpan(new Span());

        Builder.buildContext(1);
        Builder.buildContext(2);

        Builder.handleOutTrace();
        Builder.buildContext(3);
        Builder.handleOutTrace();
        Builder.handleOutTrace();

        Builder.buildContext(4);
        Builder.buildContext(5);
        Builder.buildContext(6);

        Builder.handleOutTrace();
        Builder.handleOutTrace();
        Builder.handleOutTrace();

        System.out.println(TraceContext.getContext());
    }

    @Test
    public void testMS() throws InterruptedException {
        long timeMillis = System.currentTimeMillis();
        Thread.sleep(1000L);
        long timeMillis1 = System.currentTimeMillis();
        System.out.println(timeMillis1 - timeMillis);

    }
}
