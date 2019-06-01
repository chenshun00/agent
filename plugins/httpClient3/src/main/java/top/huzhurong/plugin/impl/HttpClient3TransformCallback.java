package top.huzhurong.plugin.impl;

import top.huzhurong.plugin.impl.intercepter.HttpClient3Hook;
import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;
import top.huzhurong.test.common.util.JvmUtil;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/26
 */
public class HttpClient3TransformCallback implements ProfilerPlugin {

    private AgentLog logger = PLoggerFactory.getLogger("top.huzhurong.plugin.impl");

    @Override
    public void setTemplate(TranTemplate template) {
        if (template == null) {
            return;
        }
        logger.info("[增加httpClient3回调处理]");
        template.addTranCallback(JvmUtil.jvmName("org.apache.commons.httpclient.HttpClient"), HttpClientExec.class);
    }

    public static class HttpClientExec implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String[] method = {"executeMethod"};
            return asmContext.tranform(HttpClient3Hook.Instance, method, "(Lorg/apache/commons/httpclient/HostConfiguration;Lorg/apache/commons/httpclient/HttpMethod;Lorg/apache/commons/httpclient/HttpState;)I");
        }
    }
}
