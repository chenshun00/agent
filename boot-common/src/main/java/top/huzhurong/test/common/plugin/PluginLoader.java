package top.huzhurong.test.common.plugin;

import java.util.List;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface PluginLoader {
    <T> List<Plugin<T>> load(Class<T> serviceType);
}
