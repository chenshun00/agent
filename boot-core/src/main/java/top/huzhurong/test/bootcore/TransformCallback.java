package top.huzhurong.test.bootcore;

import top.huzhurong.test.bootcore.asm.ASMContext;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/20
 */
public interface TransformCallback {
    byte[] doInTransform(ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer);
}
