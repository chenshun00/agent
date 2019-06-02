package top.huzhurong.plugin.impl.intercepter;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;
import top.huzhurong.test.common.util.JvmUtil;

import java.util.Arrays;

/**
 * @author chenshun00@gmail.com
 * @since 2019/6/2
 */
public class HttpClient3HookTest {

    @Before
    public void setUp() {
        Trace<SpanEvent> trace = TraceContext.setTrace(new Trace<SpanEvent>(JvmUtil.createTraceId(), "/a"));
        trace.setSpan(new Span<SpanEvent>());
        long executeMethod = BeanMethodRegister.hookKey("org.apache.commons.httpclient.HttpClient", "executeMethod");
        BeanMethodRegister.get(executeMethod).setLineNumber("372");
    }

    @Test
    public void into() {
        HttpMethod httpMethod = new GetMethod("https://www.kancloud.cn/longxuan/httpclient-arron/117499?aa=bb&bb=ccc&1=2");
        HttpMethodParams params = httpMethod.getParams();
        params.setParameter("test1", "testString");
        params.setParameter("testNumber", 9876);
        params.setParameter("testList", Arrays.asList("11", "22", "33"));
        HttpClient3Hook.Instance.into(this, 1, new Object[]{null, httpMethod, null});

        //assert out
        Trace<SpanEvent> trace = TraceContext.getContext();
        Assert.assertNotNull(trace);
        Span<SpanEvent> span = trace.getSpan();
        Assert.assertNotNull(span);
        Assert.assertEquals(1, span.size());
        SpanEvent one = span.getOne();
        Assert.assertNotNull(one);
    }

    @Test
    public void out() {
    }
}