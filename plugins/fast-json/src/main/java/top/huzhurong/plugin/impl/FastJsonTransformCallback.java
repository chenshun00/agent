package top.huzhurong.plugin.impl;

import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/20
 */
public class FastJsonTransformCallback implements ProfilerPlugin {

    @Override
    public void setTemplate(TranTemplate template) {
        if (template == null) {
            throw new NullPointerException("template 为空");
        }
//        template.addTranCallback("com.alibaba.fastjson.serializer.JSONSerializer".replaceAll("\\.", "/"), FastJsonCallback.class);
    }

    public static class FastJsonCallback implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate,ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String[] method = {"write"};
            return asmContext.tranform(FastJsonHook.Instance, method, "(Ljava/lang/Object;)V");
        }
    }
}
