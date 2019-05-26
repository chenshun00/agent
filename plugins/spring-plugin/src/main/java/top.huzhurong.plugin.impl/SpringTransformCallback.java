package top.huzhurong.plugin.impl;

import top.huzhurong.plugin.impl.intercepter.SpringHook;
import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.util.JvmUtil;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/25
 */
public class SpringTransformCallback implements ProfilerPlugin {

    @Override
    public void setTemplate(TranTemplate template) {
        template.addTranCallback(JvmUtil.jvmName("org.springframework.context.annotation.ClassPathBeanDefinitionScanner"), SpringMvcCallback.class);
    }

    public static class SpringMvcCallback implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            if (SpringHook.Instance == null) {
                SpringHook.Instance = new SpringHook(tranTemplate);
            }
            String[] method = {"doScan"};
            return asmContext.tranform(SpringHook.Instance, method, "([Ljava/lang/String;)Ljava/util/Set;");
        }
    }
}
