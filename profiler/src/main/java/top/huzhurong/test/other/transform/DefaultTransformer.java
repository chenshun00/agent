package top.huzhurong.test.other.transform;

import com.google.inject.Inject;
import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.plugin.Plugin;
import top.huzhurong.test.common.plugin.PluginLoader;
import top.huzhurong.test.common.util.JvmUtil;
import top.huzhurong.test.other.asm.context.ASMCLass;
import top.huzhurong.test.other.plugin.DynamicTransformCallbackProvider;
import top.huzhurong.test.other.plugin.TransformCallbackProvider;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/16
 */
public class DefaultTransformer implements ClassFileTransformer {

    private ClassLoader agentClassLoader;
    private TranTemplate tranTemplate;

    @Inject
    public DefaultTransformer(ClassLoader classLoader, PluginLoader pluginLoader, TranTemplate tranTemplate) {
        this.agentClassLoader = classLoader;
        this.tranTemplate = tranTemplate;
        //使用上下文加载器进行加载
        List<Plugin<ProfilerPlugin>> load = pluginLoader.load(ProfilerPlugin.class);
        for (Plugin<ProfilerPlugin> transformCallbackPlugin : load) {
            List<ProfilerPlugin> instanceList = transformCallbackPlugin.getInstanceList();
            for (ProfilerPlugin profilerPlugin : instanceList) {
                profilerPlugin.setTemplate(this.tranTemplate, transformCallbackPlugin);
            }
        }

    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (className.startsWith(JvmUtil.jvmName("top.huzhurong.test")) && loader == agentClassLoader) {
            return null;
        }
        Class<? extends TransformCallback> tranTemplateTranCallback = this.tranTemplate.getTranCallback(className);
        if (tranTemplateTranCallback != null) {
            Thread thread = Thread.currentThread();
            ClassLoader contextClassLoader = thread.getContextClassLoader();
            try {
                thread.setContextClassLoader(agentClassLoader);
                TransformCallbackProvider provider = new DynamicTransformCallbackProvider(tranTemplateTranCallback.getName(), this.tranTemplate.getPlugin(className));
                TransformCallback transformCallback = provider.getTransformCallback(loader);
                ASMContext asmContext = new ASMCLass(classfileBuffer, className, loader);
                return transformCallback.doInTransform(this.tranTemplate, this.tranTemplate.getPlugin(className), asmContext, loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
            } finally {
                thread.setContextClassLoader(contextClassLoader);
            }
        }
        return null;
    }

}