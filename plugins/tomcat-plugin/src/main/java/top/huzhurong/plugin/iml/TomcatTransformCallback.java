package top.huzhurong.plugin.iml;

import top.huzhurong.plugin.iml.intercepter.TomcatHook;
import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.util.JvmUtil;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class TomcatTransformCallback implements ProfilerPlugin {
    @Override
    public void setTemplate(TranTemplate template) {
        template.addTranCallback(JvmUtil.jvmName("org.apache.catalina.core.StandardWrapperValve"), TomcatCallback.class);
    }


    public static class TomcatCallback implements TransformCallback {
        @Override
        public byte[] doInTransform(ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            return asmContext.tranform(TomcatHook.Instance, "invoke", "(Lorg/apache/catalina/connector/Request;Lorg/apache/catalina/connector/Response;)V");
        }
    }
}
