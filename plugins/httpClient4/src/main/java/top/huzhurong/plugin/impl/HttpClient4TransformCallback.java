package top.huzhurong.plugin.impl;

import top.huzhurong.plugin.impl.intercepter.HttpClient4Hook;
import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;
import top.huzhurong.test.common.plugin.Plugin;
import top.huzhurong.test.common.util.JvmUtil;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/26
 */
public class HttpClient4TransformCallback implements ProfilerPlugin {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    @Override
    public String[] setTemplate(TranTemplate template, Plugin<ProfilerPlugin> transformCallbackPlugin) {
        if (template == null) {
            throw new NullPointerException("template 为空");
        }
        logger.info("[增加httpClient4回调处理]");
        String internalHttpClient = JvmUtil.jvmName("org.apache.http.impl.client.InternalHttpClient");
        template.addTranCallback(internalHttpClient, HttpClientExec.class, transformCallbackPlugin);
        String abstractHttpClient = JvmUtil.jvmName("org.apache.http.impl.client.AbstractHttpClient");
        template.addTranCallback(abstractHttpClient, HttpClientExec.class, transformCallbackPlugin);
        String minimalHttpClient = JvmUtil.jvmName("org.apache.http.impl.client.MinimalHttpClient");
        template.addTranCallback(minimalHttpClient, HttpClientExec.class, transformCallbackPlugin);
        return new String[]{internalHttpClient, abstractHttpClient, minimalHttpClient};
    }

    public static class HttpClientExec implements TransformCallback {

        private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, Plugin<ProfilerPlugin> pluginPlugin, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            logger.info("处理httpClient4回调 [{}] [{}]", className, classLoader);
            String[] method = {"doExecute"};
            return asmContext.tranform(HttpClient4Hook.Instance, method, "(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/client/methods/CloseableHttpResponse;");
        }
    }
}
