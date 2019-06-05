package top.huzhurong.plugin.impl.intercepter;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import top.huzhurong.plugin.impl.spring.BeanMethodIntecepter;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.plugin.Plugin;
import top.huzhurong.test.common.util.JvmUtil;

import java.util.Set;

/**
 * 进入Spring的处理，这里一般情况下只处理一次
 *
 * @author chenshun00@gmail.com
 * @since 2019/3/25
 */
public class SpringHook implements BaseHook {

    private TranTemplate tranTemplate;
    private Plugin<ProfilerPlugin> pluginPlugin;

    public SpringHook(TranTemplate tranTemplate, Plugin<ProfilerPlugin> pluginPlugin) {
        this.tranTemplate = tranTemplate;
        this.pluginPlugin = pluginPlugin;
    }

    public static SpringHook Instance = null;


    @Override
    public void into(Object curObject, int index, Object[] args) {

    }

    @Override
    public void out(Object result, Object cur, int index, Object[] args) {
        if (result instanceof Set) {
            Set set = (Set) result;
            for (Object aSet : set) {
                BeanDefinitionHolder holder = (BeanDefinitionHolder) aSet;
                BeanDefinition beanDefinition = holder.getBeanDefinition();
                String beanClassName = beanDefinition.getBeanClassName();
                String inf = JvmUtil.jvmName(beanClassName);
                BeanMethodIntecepter beanMethodIntecepter = new BeanMethodIntecepter();
                this.tranTemplate.addTranCallback(inf, beanMethodIntecepter.getClass(), pluginPlugin);
            }
        }
    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {

    }
}
