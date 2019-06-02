package top.huzhurong.plugin.impl.httpcliet3test;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author chenshun00@gmail.com
 * @since 2019/6/1
 */
public class TestHttpClient3 {
    @org.junit.Test
    public void test() throws IOException {
        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod("https://www.kancloud.cn/longxuan/httpclient-arron/117499");
        int i = client.executeMethod(getMethod);
        String responseBodyAsString = getMethod.getResponseBodyAsString();
        System.out.println(responseBodyAsString);
    }

    @Test
    public void testRequestParam() throws IllegalAccessException, NoSuchFieldException {
        GetMethod getMethod = new GetMethod("https://www.kancloud.cn/longxuan/httpclient-arron/117499?aa=bb&bb=ccc&1=2");
        String queryString = getMethod.getQueryString();
        Assert.assertEquals("aa=bb&bb=ccc&1=2", queryString);

        HttpMethodParams getMethodParams = getMethod.getParams();
        Assert.assertNotNull(getMethodParams);
        Field parameters = getMethodParams.getClass().getSuperclass().getDeclaredField("parameters");
        parameters.setAccessible(true);
        Object o = parameters.get(getMethodParams);
        Assert.assertNull(o);

        Assert.assertEquals("/longxuan/httpclient-arron/117499", getMethod.getPath());
        HostConfiguration hostConfiguration = getMethod.getHostConfiguration();
        String hostURL = hostConfiguration.getHostURL();
        Assert.assertEquals("https://www.kancloud.cn", hostURL);
    }

    @Test
    public void testGetRequestParam() throws NoSuchFieldException, IllegalAccessException {
        GetMethod getMethod = new GetMethod("https://www.kancloud.cn/longxuan/httpclient-arron/117499");

        final HttpMethodParams params = new HttpMethodParams();
        params.setParameter("aa", "bb");
        params.setParameter("bb", "ccc");
        params.setParameter("1", "2");
        params.setContentCharset("utf-8");
        getMethod.setParams(params);
        String queryString = getMethod.getQueryString();
        Assert.assertNull(queryString);
        final HttpMethodParams getMethodParams = getMethod.getParams();
        Assert.assertEquals("bb", getMethodParams.getParameter("aa"));
        Assert.assertEquals("ccc", getMethodParams.getParameter("bb"));
        Assert.assertEquals("2", getMethodParams.getParameter("1"));


        Field parameters = getMethodParams.getClass().getSuperclass().getDeclaredField("parameters");
        parameters.setAccessible(true);
        Object o = parameters.get(getMethodParams);
        Assert.assertTrue(o instanceof HashMap);
        HashMap hashMap = (HashMap) o;
        Assert.assertTrue(hashMap.containsKey("aa"));
        Assert.assertTrue(hashMap.containsKey("bb"));
        Assert.assertTrue(hashMap.containsKey("1"));

        Assert.assertEquals("bb", hashMap.get("aa"));
        Assert.assertEquals("ccc", hashMap.get("bb"));
        Assert.assertEquals("2", hashMap.get("1"));
    }
}
