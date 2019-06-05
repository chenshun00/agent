package top.huzhurong.test.bootcore.plugin;

import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.plugin.Plugin;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/20
 */
public interface ProfilerPlugin {
    String[] setTemplate(TranTemplate template, Plugin<ProfilerPlugin> transformCallbackPlugin);
}
