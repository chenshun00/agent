package top.huzhurong.plugin.impl;

import top.huzhurong.plugin.impl.intercepter.HttpClient4Hook;
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
public class HttpClient4TransformCallback implements ProfilerPlugin {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    @Override
    public void setTemplate(TranTemplate template) {
        if (template == null) {
            return;
        }
        logger.info("[增加httpClient4回调处理]");
//        template.addTranCallback(JvmUtil.jvmName("com.ibatis.sqlmap.engine.execution.SqlExecutor"), HttpClientExec.class);
    }

    public static class HttpClientExec implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String[] method = {"executeUpdate", "executeQuery"};
            return asmContext.tranform(HttpClient4Hook.Instance, method, null);
        }
    }
}
