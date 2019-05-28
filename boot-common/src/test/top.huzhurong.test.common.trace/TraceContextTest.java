package top.huzhurong.test.common.trace;

import org.junit.Test;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/26
 */
public class TraceContextTest {

    @Test
    public void testGetContext() {
        Trace trace = TraceContext.getContext();
        if (trace == null) {
            trace = TraceContext.setTrace(Trace.newTrace("11"));
        }
        System.out.println(trace);

        trace = TraceContext.getContext();
        System.out.println(trace);
        trace = TraceContext.getContext();
        System.out.println(trace);
        TraceContext.removeContext();
        trace = TraceContext.getContext();
        System.out.println(trace);
    }
}
