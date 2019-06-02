package top.huzhurong.plugin.impl.intercepter;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.SpanEvent;
import top.huzhurong.test.common.trace.Trace;
import top.huzhurong.test.common.trace.TraceContext;
import top.huzhurong.test.common.util.JvmUtil;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/6/2
 */
public class HttpClient4HookTest {

    @Before
    public void setUp() {
        Trace<SpanEvent> trace = TraceContext.setTrace(new Trace<SpanEvent>(JvmUtil.createTraceId(), "/a"));
        trace.setSpan(new Span<SpanEvent>());
        long executeMethod = BeanMethodRegister.hookKey("org.apache.http.impl.client.InternalHttpClient", "doExecute");
        BeanMethodRegister.get(executeMethod).setLineNumber("158");

    }

    @Test
    public void into() throws ClientProtocolException, UnsupportedEncodingException {
        //HttpGet httpGet = new HttpGet("http://www.huzhurong.top/2019-04-09-mysql%E6%97%A5%E5%B8%B8%E8%AE%B0%E5%BD%95?aa=aa&bb=bb");

        HttpPost httpPost = new HttpPost("http://www.huzhurong.top/2019-04-09-mysql%E6%97%A5%E5%B8%B8%E8%AE%B0%E5%BD%95/");
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("aa", "bb"));
        list.add(new BasicNameValuePair("bb", "2"));
        httpPost.setEntity(new UrlEncodedFormEntity(list));

        HttpClient4Hook.Instance.into(this, 1, new Object[]{determineTarget(httpPost), httpPost, null});

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

    private static HttpHost determineTarget(final HttpUriRequest request) throws ClientProtocolException {
        // A null target may be acceptable if there is a default target.
        // Otherwise, the null target is detected in the director.
        HttpHost target = null;

        final URI requestURI = request.getURI();
        if (requestURI.isAbsolute()) {
            target = URIUtils.extractHost(requestURI);
            if (target == null) {
                throw new ClientProtocolException("URI does not specify a valid host name: "
                        + requestURI);
            }
        }
        return target;
    }
}