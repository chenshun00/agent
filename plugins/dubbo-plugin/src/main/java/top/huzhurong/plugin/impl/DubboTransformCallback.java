package top.huzhurong.plugin.impl;

import top.huzhurong.plugin.impl.intercepter.DubboConsumerHook;
import top.huzhurong.plugin.impl.intercepter.DubboProviderHook;
import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;
import top.huzhurong.test.common.util.JvmUtil;

import java.security.ProtectionDomain;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/26
 */
public class DubboTransformCallback implements ProfilerPlugin {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    @Override
    public void setTemplate(TranTemplate template) {
        if (template == null) {
            throw new NullPointerException("template 为空");
        }
        logger.info("[增加Dubbo回调处理]");
        //provider
        template.addTranCallback(JvmUtil.jvmName("com.alibaba.dubbo.rpc.protocol.AbstractInvoker"), AbstractInvokerTransform.class);
        //consumer
        template.addTranCallback(JvmUtil.jvmName("com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker"), AbstractProxyInvokerTransform.class);
    }

    public static class AbstractInvokerTransform implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String[] method = {"invoke"};
            return asmContext.tranform(DubboProviderHook.Instance, method, null);
        }
    }

    public static class AbstractProxyInvokerTransform implements TransformCallback {
        @Override
        public byte[] doInTransform(TranTemplate tranTemplate, ASMContext asmContext, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String[] method = {"invoke"};
            return asmContext.tranform(DubboConsumerHook.Instance, method, null);
        }
    }
}
