package top.huzhurong.plugin.impl.intercepter;

import org.junit.Assert;
import org.junit.Test;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;

import java.util.Arrays;

/**
 * @author chenshun00@gmail.com
 * @since 2019/6/2
 */
public class ArraysTest {

    @Test
    public void name() {
        Class<?>[] parameters = new Class[]{String.class, Object.class,Span.class,SpanEvent.class};
        Assert.assertEquals("[class java.lang.String, class java.lang.Object, class top.huzhurong.test.common.trace.Span, class top.huzhurong.test.common.trace.SpanEvent]",Arrays.toString(parameters));
    }
}
