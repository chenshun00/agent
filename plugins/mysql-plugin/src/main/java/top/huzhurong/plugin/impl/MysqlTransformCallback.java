package top.huzhurong.plugin.impl;

import top.huzhurong.plugin.impl.intercepter.MysqlHook;
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
 * 被自定义的类加载器加载
 *
 * @author chenshun00@gmail.com
 * @since 2019/2/20
 */
public class MysqlTransformCallback implements ProfilerPlugin {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    @Override
    public String[] setTemplate(TranTemplate template, Plugin<ProfilerPlugin> transformCallbackPlugin) {
        if (template == null) {
            throw new NullPointerException("template 为空");
        }
        logger.info("[增加mysql回调处理]");
        String preparedStatement = JvmUtil.jvmName("com.mysql.jdbc.PreparedStatement");
        template.addTranCallback(preparedStatement, MysqlCallback.class, transformCallbackPlugin);
        return new String[]{preparedStatement};
    }

    public static class MysqlCallback implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, Plugin<ProfilerPlugin> pluginPlugin, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String[] method = {"executeInternal"};
            return asmContext.tranform(MysqlHook.Instance, method, null);
        }
    }
}
