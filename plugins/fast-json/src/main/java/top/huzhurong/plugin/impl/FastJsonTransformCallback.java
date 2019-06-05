package top.huzhurong.plugin.impl;

import top.huzhurong.plugin.impl.intercepter.FastJsonHook;
import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.plugin.Plugin;
import top.huzhurong.test.common.util.JvmUtil;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/20
 */
public class FastJsonTransformCallback implements ProfilerPlugin {

    @Override
    public String[] setTemplate(TranTemplate template, Plugin<ProfilerPlugin> transformCallbackPlugin) {
        if (template == null) {
            throw new NullPointerException("template 为空");
        }
        String jSONSerializer = JvmUtil.jvmName("com.alibaba.fastjson.serializer.JSONSerializer");
        template.addTranCallback(jSONSerializer, FastJsonCallback.class, transformCallbackPlugin);
        return new String[]{jSONSerializer};
    }

    public static class FastJsonCallback implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, Plugin<ProfilerPlugin> pluginPlugin, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String[] method = {"write"};
            return asmContext.tranform(FastJsonHook.Instance, method, "(Ljava/lang/Object;)V");
        }
    }
}
