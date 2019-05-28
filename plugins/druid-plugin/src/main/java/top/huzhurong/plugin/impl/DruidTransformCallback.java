package top.huzhurong.plugin.impl;

import top.huzhurong.plugin.impl.intercepter.DruidHook;
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
 * @since 2019/5/25
 */
public class DruidTransformCallback implements ProfilerPlugin {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    @Override
    public void setTemplate(TranTemplate template) {
        if (template == null) {
            throw new NullPointerException("template 为空");
        }
        logger.info("[增加druid回调处理]");
        template.addTranCallback(JvmUtil.jvmName("com.alibaba.druid.pool.DruidDataSource"), DruidConnection.class);
    }

    public static class DruidConnection implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String[] method = {"getConnection"};
            return asmContext.tranform(DruidHook.Instance, method, "(J)Lcom/alibaba/druid/pool/DruidPooledConnection;");
        }
    }
}
