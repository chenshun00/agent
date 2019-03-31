package top.huzhurong.plugin.impl.intercepter;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import top.huzhurong.plugin.impl.spring.BeanMethodIntecepter;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;
import top.huzhurong.test.common.util.JvmUtil;

import java.util.Set;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/25
 */
public class SpringHook implements BaseHook {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    private TranTemplate tranTemplate;

    public SpringHook(TranTemplate tranTemplate) {
        this.tranTemplate = tranTemplate;
    }

    public static SpringHook Instance = null;

    @Override
    public void into(Object curObject, Object[] args) {

    }

    @Override
    public void into(int index, Object[] args) {

    }

    @Override
    public void out(Object result, Object cur, Object[] args) {
        if (result instanceof Set) {
            Set set = (Set) result;
            for (Object aSet : set) {
                BeanDefinitionHolder holder = (BeanDefinitionHolder) aSet;
                BeanDefinition beanDefinition = holder.getBeanDefinition();
                String beanClassName = beanDefinition.getBeanClassName();
                String inf = JvmUtil.jvmName(beanClassName);
                BeanMethodIntecepter beanMethodIntecepter = new BeanMethodIntecepter();
                this.tranTemplate.addTranCallback(inf, beanMethodIntecepter.getClass());
            }
        }
    }

    @Override
    public void out(Object result, int index, Object[] args) {

    }

    @Override
    public void error(Throwable ex, Object curObject, Object[] args) {

    }

    @Override
    public void error(Throwable ex, int index, Object[] args) {

    }
}
