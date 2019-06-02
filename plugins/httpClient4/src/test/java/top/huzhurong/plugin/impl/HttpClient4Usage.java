package top.huzhurong.plugin.impl;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import top.huzhurong.test.common.util.JvmUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/6/2
 */
public class HttpClient4Usage {

    /**
     * get请求使用带参数后缀的形式
     *
     * @throws IOException 发生io错误
     */
    @Test
    public void testGetUsage() throws IOException {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.huzhurong.top/2019-04-09-mysql%E6%97%A5%E5%B8%B8%E8%AE%B0%E5%BD%95/");
        CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }

    /**
     * post请求 可以多次读写所带参数
     */
    @Test
    public void testPostUsage() throws IOException {
        HttpPost httpPost = new HttpPost("http://www.huzhurong.top/2019-04-09-mysql%E6%97%A5%E5%B8%B8%E8%AE%B0%E5%BD%95/");
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("aa", "bb"));
        list.add(new BasicNameValuePair("bb", "2"));
        httpPost.setEntity(new UrlEncodedFormEntity(list));
        httpPost.addHeader("traceId", JvmUtil.createTraceId());


        HttpEntity entity = httpPost.getEntity();
        InputStream content = entity.getContent();
        int available = content.available();
        byte[] bytes = new byte[available];
        Assert.assertEquals(available, content.read(bytes));
        System.out.println(new String(bytes));
        String method = httpPost.getMethod();
        Assert.assertEquals("POST", method);

        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse execute = closeableHttpClient.execute(httpPost);
        Assert.assertEquals(200, execute.getStatusLine().getStatusCode());
        System.out.println(EntityUtils.toString(execute.getEntity()));
    }
}
