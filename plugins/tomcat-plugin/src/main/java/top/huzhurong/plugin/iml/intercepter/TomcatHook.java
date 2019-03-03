package top.huzhurong.plugin.iml.intercepter;

import org.apache.catalina.connector.Request;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.MimeHeaders;
import top.huzhurong.test.bootcore.BaseHook;

import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public final class TomcatHook implements BaseHook {

    private TomcatHook() {
    }

    public static TomcatHook Instance = new TomcatHook();

    //添加一个请求头
    @Override
    public void into(Object curObject, Object[] args) {
        if (args[0] instanceof Request) {
            Request request = (Request) args[0];
            String traceId = request.getRequest().getHeader("traceId");
            if (traceId == null) {
                org.apache.coyote.Request coyoteRequest = request.getCoyoteRequest();
                MimeHeaders mimeHeaders = coyoteRequest.getMimeHeaders();
                MessageBytes traceHeader = mimeHeaders.addValue("traceId");
                traceHeader.setString(UUID.randomUUID().toString());


            }
        }
    }

    @Override
    public void out(Object result, Object cur, Object[] args) {
        //计算耗时
    }

    @Override
    public void error(Throwable ex, Object curObject, Object[] args) {

    }
}
