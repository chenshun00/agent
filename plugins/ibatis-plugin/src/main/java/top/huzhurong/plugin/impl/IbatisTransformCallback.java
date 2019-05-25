package top.huzhurong.plugin.impl;

import top.huzhurong.plugin.impl.intercepter.IbatisHook;
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
public class IbatisTransformCallback implements ProfilerPlugin {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    @Override
    public void setTemplate(TranTemplate template) {
        if (template == null) {
            throw new NullPointerException("template 为空");
        }
        logger.info("[增加mybatis回调处理]");
        template.addTranCallback(JvmUtil.jvmName("com.ibatis.sqlmap.engine.execution.SqlExecutor"), IbatisTransformCallback.IbatisUpdate.class);
        template.addTranCallback(JvmUtil.jvmName("com.ibatis.sqlmap.engine.execution.SqlExecutor"), IbatisTransformCallback.IbatisQuery.class);
    }

    public static class IbatisUpdate implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            return asmContext.tranform(IbatisHook.Instance, "executeUpdate", null);
        }
    }

    public static class IbatisQuery implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            return asmContext.tranform(IbatisHook.Instance, "executeQuery", null);
        }
    }
}