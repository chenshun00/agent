package top.huzhurong.test.other.module.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import top.huzhurong.test.common.plugin.PluginLoader;
import top.huzhurong.test.other.annotation.PluginJars;
import top.huzhurong.test.other.plugin.JarPluginLoader;

import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/18
 */
public class PluginLoaderProvider implements Provider<PluginLoader> {

    private final ClassLoader parentClassLoader;
    private final PluginLoader pluginLoader;

    @Inject
    public PluginLoaderProvider(@PluginJars List<String> urls) {
        this.parentClassLoader = Object.class.getClassLoader();
        this.pluginLoader = new JarPluginLoader(urls, parentClassLoader);
    }

    @Override
    public PluginLoader get() {
        return pluginLoader;
    }
}
