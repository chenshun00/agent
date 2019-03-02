package top.huzhurong.test.other.plugin;

import top.huzhurong.test.common.plugin.JarFileUtils;
import top.huzhurong.test.common.plugin.JarPlugin;
import top.huzhurong.test.common.plugin.Plugin;
import top.huzhurong.test.common.plugin.PluginLoader;
import top.huzhurong.test.common.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.jar.JarFile;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/18
 */
public class JarPluginLoader implements PluginLoader {

    private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();

    private final ClassLoader parentClassLoader;

    private final List<Entry> serviceLoaderList;

    public JarPluginLoader(List<String> pluginJar, ClassLoader parentClassLoader) {
        this.serviceLoaderList = loadPluginJar(pluginJar);
        this.parentClassLoader = parentClassLoader;
    }

    private List<Entry> loadPluginJar(List<String> pluginJar) {
        return isolationPolicy(pluginJar);
    }

    private List<Entry> isolationPolicy(List<String> pluginJar) {
        final List<Entry> list = new ArrayList<Entry>();
        for (String filePath : pluginJar) {
            final File file = toFile(filePath);
            final URL url = toUrl(file);
            final ClassLoader pluginClassLoader = createPluginClassLoader(new URL[]{url}, parentClassLoader);
            Entry entry = new Entry(url, file, pluginClassLoader);
            list.add(entry);
        }
        return list;
    }

    @Override
    public <T> List<Plugin<T>> load(Class<T> serviceType) {
        List<Plugin<T>> result = new ArrayList<Plugin<T>>();
        for (Entry entry : serviceLoaderList) {
            final Plugin<T> plugin = newPlugin(serviceType, entry);
            result.add(plugin);
        }
        return result;
    }

    private <T> Plugin<T> newPlugin(Class<T> serviceType, Entry entry) {
        URL pluginURL = entry.getURL();
        ServiceLoader<T> serviceLoader = ServiceLoader.load(serviceType, entry.getClassLoader());
        List<T> pluginList = toList(serviceLoader, serviceType);
        JarFile jarFile = createJarFile(entry.getFile());
        String pluginPackages = JarFileUtils.getManifestValue(jarFile, "packageName", "defaultName");
        List<String> pluginPackageList = StringUtils.tokenizeToStringList(pluginPackages, ",");

        return new JarPlugin<T>(pluginURL, jarFile, pluginList, pluginPackageList);
    }

    private JarFile createJarFile(File pluginJar) {
        try {
            return new JarFile(pluginJar);
        } catch (IOException e) {
            throw new RuntimeException("IO error. " + e.getCause(), e);
        }
    }

    private ClassLoader createPluginClassLoader(final URL[] urls, final ClassLoader parent) {
        if (SECURITY_MANAGER != null) {
            return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                public ClassLoader run() {
                    return new URLClassLoader(urls, parent);
                }
            });
        } else {
            return new URLClassLoader(urls, parent);
        }
    }

    private static <T> List<T> toList(Iterable<T> iterable, Class<T> serviceType) {
        final List<T> list = new ArrayList<T>();
        for (T plugin : iterable) {
            list.add(serviceType.cast(plugin));
        }
        return list;
    }


    private URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL:" + file);
        }
    }

    private File toFile(String filePath) {
        final File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException(file + " File not exist");
        }
        if (!file.isFile()) {
            throw new RuntimeException(file + " is not file");
        }
        if (!file.canRead()) {
            throw new RuntimeException(file + " File cannot be read");
        }
        return file;
    }

    public static class Entry {
        private final URL filePath;
        private final File file;
        private final ClassLoader classLoader;

        public Entry(URL filePath, File file, ClassLoader classLoader) {
            this.filePath = filePath;
            this.file = file;
            this.classLoader = classLoader;
        }

        public ClassLoader getClassLoader() {
            return classLoader;
        }

        public URL getURL() {
            return filePath;
        }

        public File getFile() {
            return file;
        }
    }
}
