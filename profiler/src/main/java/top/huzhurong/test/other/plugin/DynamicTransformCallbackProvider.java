package top.huzhurong.test.other.plugin;

import top.huzhurong.test.bootcore.TransformCallback;
import top.huzhurong.test.bootcore.plugin.ProfilerPlugin;
import top.huzhurong.test.common.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/23
 */
public class DynamicTransformCallbackProvider implements TransformCallbackProvider {

    private final String transformCallbackClassName;
    private final Object[] parameters;
    private final Class<?>[] parameterTypes;
    private Plugin<ProfilerPlugin> profilerPluginPlugin;


    private static final Method ADD_URL;

    static {
        try {
            ADD_URL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            ADD_URL.setAccessible(true);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot access URLClassLoader.addURL(URL)", e);
        }
    }

    public DynamicTransformCallbackProvider(String transformCallbackClassName, Plugin<ProfilerPlugin> profilerPluginPlugin) {
        this.transformCallbackClassName = transformCallbackClassName;
        this.parameters = null;
        this.parameterTypes = null;
        this.profilerPluginPlugin = profilerPluginPlugin;
    }

    @Override
    public TransformCallback getTransformCallback(ClassLoader loader) {
        try {
            final Class<? extends TransformCallback> transformCallbackClass = injectClass(loader, transformCallbackClassName);
            Constructor<? extends TransformCallback> constructor = transformCallbackClass.getConstructor(parameterTypes);
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(transformCallbackClassName + " load fail Caused by:" + e.getMessage(), e);
        }
    }


    @SuppressWarnings("unchecked")
    private <T> Class<? extends T> injectClass(ClassLoader classLoader, String className) {
        try {
            final URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            addPluginURLIfAbsent(urlClassLoader, className);
            return (Class<T>) urlClassLoader.loadClass(className);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //重复加载也没问题，内部会去重
    private synchronized void addPluginURLIfAbsent(URLClassLoader classLoader, String className) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        URL url = profilerPluginPlugin.getURL();
        System.out.println("className:" + className + "\turl:" + url);
        ADD_URL.invoke(classLoader, url);
    }
}
