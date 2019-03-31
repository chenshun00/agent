package top.huzhurong.test.bootcore.asm;

import top.huzhurong.test.bootcore.BaseHook;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/21
 */
public interface ASMContext {
    byte[] tranform(BaseHook baseHook, String method, String desc);

    byte[] tranform(BaseHook baseHook);
}
