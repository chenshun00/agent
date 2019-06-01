package top.huzhurong.test.common.trace;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/26
 */
public class TraceContextTest {

    @Test
    public void testGetContext() {
        Trace<?> trace = TraceContext.getContext();
        Assert.assertNull(trace);
        trace = TraceContext.setTrace(Trace.<SpanEvent>newTrace("11"));
        Assert.assertNotNull(trace);
        trace = TraceContext.getContext();
        Assert.assertNotNull(trace);
        trace = TraceContext.getContext();
        Assert.assertNotNull(trace);
        TraceContext.removeContext();
        trace = TraceContext.getContext();
        Assert.assertNull(trace);
    }
}
