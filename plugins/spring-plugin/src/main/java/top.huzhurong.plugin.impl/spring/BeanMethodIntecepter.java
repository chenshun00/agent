package top.huzhurong.plugin.impl.spring;

import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.template.TranTemplate;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanMethodIntecepter implements TransformCallback {

    @Override
    public byte[] doInTransform(TranTemplate tranTemplate, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        return asmContext.tranform(BeanHook.Instance);
    }
}
