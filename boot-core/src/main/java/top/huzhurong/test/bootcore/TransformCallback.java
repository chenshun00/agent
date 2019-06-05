package top.huzhurong.test.bootcore;

import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.plugin.Plugin;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/20
 */
public interface TransformCallback {
    byte[] doInTransform(TranTemplate tranTemplate, Plugin<ProfilerPlugin> pluginPlugin, ASMContext asmContext, ClassLoader classLoader,
                         String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer);


}
