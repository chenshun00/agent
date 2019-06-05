package top.huzhurong.plugin.iml;

import top.huzhurong.plugin.iml.intercepter.TomcatHook;
import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.plugin.Plugin;
import top.huzhurong.test.common.util.JvmUtil;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class TomcatTransformCallback implements ProfilerPlugin {
    @Override
    public String[] setTemplate(TranTemplate template, Plugin<ProfilerPlugin> transformCallbackPlugin) {
        String standardWrapperValve = JvmUtil.jvmName("org.apache.catalina.core.StandardWrapperValve");
        template.addTranCallback(standardWrapperValve, TomcatCallback.class, transformCallbackPlugin);
        return new String[]{standardWrapperValve};
    }


    public static class TomcatCallback implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, Plugin<ProfilerPlugin> pluginPlugin, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String[] method = {"invoke"};
            return asmContext.tranform(TomcatHook.Instance, method, "(Lorg/apache/catalina/connector/Request;Lorg/apache/catalina/connector/Response;)V");
        }
    }
}
