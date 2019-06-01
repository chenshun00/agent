package top.huzhurong.test.common.trace;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author chenshun00@gmail.com
 * @since 2019/6/1
 */
public class SpanTypeTest {
    @Test
    public void testType() {
        assertEquals(SpanType.http.getValue(),"http");
        assertEquals(SpanType.dubbo.getValue(),"dubbo");
    }
}
