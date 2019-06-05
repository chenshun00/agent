package top.huzhurong.plugin.impl;

import top.huzhurong.plugin.impl.intercepter.SpringHook;
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
 * @since 2019/3/25
 */
public class SpringTransformCallback implements ProfilerPlugin {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    @Override
    public String[] setTemplate(TranTemplate template, Plugin<ProfilerPlugin> transformCallbackPlugin) {
        if (template == null) {
            throw new NullPointerException("template 为空");
        }

        logger.info("[增加Spring回调处理]");
        String classPathBeanDefinitionScanner = JvmUtil.jvmName("org.springframework.context.annotation.ClassPathBeanDefinitionScanner");
        template.addTranCallback(classPathBeanDefinitionScanner, SpringMvcCallback.class, transformCallbackPlugin);
        return new String[]{classPathBeanDefinitionScanner};
    }

    public static class SpringMvcCallback implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, Plugin<ProfilerPlugin> pluginPlugin, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            if (SpringHook.Instance == null) {
                SpringHook.Instance = new SpringHook(tranTemplate, pluginPlugin);
            }
            String[] method = {"doScan"};
            return asmContext.tranform(SpringHook.Instance, method, "([Ljava/lang/String;)Ljava/util/Set;");
        }
    }
}
