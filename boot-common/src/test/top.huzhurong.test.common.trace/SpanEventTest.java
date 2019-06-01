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
        Span<String> span = new Span<String>();
        span.push("1");
        span.push("2");
        span.push("3");
        Assert.assertEquals(2, span.index);
        String pop = span.pop();
        Assert.assertEquals("3", pop);
        pop = span.pop();
        Assert.assertEquals("3", pop);
        Assert.assertEquals(2, span.index);

        span.push("4");
        Assert.assertEquals(3, span.index);
        pop = span.pop();
        Assert.assertEquals("4", pop);
        String one = span.getOne();
        Assert.assertEquals("4", one);
        Assert.assertEquals(2, span.index);
        Assert.assertEquals("3", span.getOne());
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


    @Test
    public void testMockRun() {
        Span<SpanEvent> span = new Span<SpanEvent>();
        span.setSpanId("dc9c5f8173554a23ae56b8742a87c136");
        //tomcat mvc service dao druid mybatis mysql ||  service dao druid mybatis mysql
        span.push(push("301", span.getSpanId(), "org.apache.catalina.core.StandardWrapperValve", "invoke"));
        //500
        span.push(push("28", span.getSpanId(), "top.huzhurong.mysql.transaction.view.AAA", "request"));
        span.push(push("184", span.getSpanId(), "top.huzhurong.mysql.transaction.service.TestTransactionService", "firstTransaction"));
        span.push(push("28", span.getSpanId(), "top.huzhurong.mysql.transaction.dao.impl.TestTransactionDao", "addTestTransaction"));
        span.push(push("1235", span.getSpanId(), "com.alibaba.druid.pool.DruidDataSource", "getConnection"));
        span.push(push("52", span.getSpanId(), "org.apache.ibatis.executor.statement.PreparedStatementHandler", "update"));
        span.push(push("1906", span.getSpanId(), "com.mysql.jdbc.PreparedStatement", "executeInternal"));
        handle(span);
        handle(span);
        handle(span);
        handle(span);
        handle(span);
        span.push(push("192", span.getSpanId(), "top.huzhurong.mysql.transaction.service.TestTransactionService", "secondTransaction"));
        span.push(push("28", span.getSpanId(), "top.huzhurong.mysql.transaction.dao.impl.TestTransactionDao", "addTestTransaction"));
        span.push(push("1235", span.getSpanId(), "com.alibaba.druid.pool.DruidDataSource", "getConnection"));
        span.push(push("52", span.getSpanId(), "org.apache.ibatis.executor.statement.PreparedStatementHandler", "update"));
        span.push(push("1906", span.getSpanId(), "com.mysql.jdbc.PreparedStatement", "executeInternal"));
        handle(span);
        handle(span);
        handle(span);
        handle(span);
        handle(span);

        handle(span);
        handle(span);
        span.setEndTIme(System.currentTimeMillis());
        System.out.println(span);
    }

    private void handle(Span<SpanEvent> span) {
        SpanEvent spanEvent = span.pop();
        spanEvent.setEndTime(System.currentTimeMillis());
    }

    private SpanEvent push(String line, String spanId, String className, String methodName) {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SpanEvent spanEvent = new SpanEvent();
        spanEvent.setLine(line);
        spanEvent.setSpanId(spanId);
        spanEvent.setClassName(className);
        spanEvent.setMethod(methodName);
        spanEvent.setStartTime(System.currentTimeMillis());
        return spanEvent;
    }
}
