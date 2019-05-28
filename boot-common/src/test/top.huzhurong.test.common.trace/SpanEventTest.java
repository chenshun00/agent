package top.huzhurong.test.common.trace;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/27
 */
public class SpanEventTest {

    @Before
    public void setUp() {
        push();
        push();
        push();
        push();
        push();
    }


    @Test
    public void testPutOut() {
        Span span = new Span();
        span.push(push());
        span.push(push());
        span.push(push());
        Assert.assertEquals(3, span.index);

        span.pop();
        Assert.assertEquals(3, span.index);
        span.getOne();
        Assert.assertEquals(-1, span.index);
    }

    private SpanEvent push() {
        SpanEvent spanEvent = new SpanEvent();
        spanEvent.setStartTime(System.currentTimeMillis());
        spanEvent.setEndTime(System.currentTimeMillis());
        spanEvent.setSpanId(UUID.randomUUID().toString());
        spanEvent.setMethod("11" + UUID.randomUUID().toString().split("-")[2]);
        spanEvent.setClassName("Dd");
        spanEvent.setLine("1");
        return spanEvent;

    }
}
