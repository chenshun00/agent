package top.huzhurong.test.other.plugin;

import top.huzhurong.test.bootcore.AgentOption;
import top.huzhurong.test.bootcore.TransformCallback;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/23
 */
public class DynamicTransformCallbackProvider implements TransformCallbackProvider {

    private final String transformCallbackClassName;
    private final Object[] parameters;
    private final Class<?>[] parameterTypes;
    private AgentOption agentOption;


    private static final Method ADD_URL;
    private static Boolean trace = false;

    static {
        try {
            ADD_URL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            ADD_URL.setAccessible(true);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot access URLClassLoader.addURL(URL)", e);
        }
    }

    public DynamicTransformCallbackProvider(String transformCallbackClassName, Object[] parameters, Class<?>[] parameterTypes) {
        this.transformCallbackClassName = transformCallbackClassName;
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
    }

    public DynamicTransformCallbackProvider(String transformCallbackClassName, AgentOption agentOption) {
        this.transformCallbackClassName = transformCallbackClassName;
        this.parameters = null;
        this.parameterTypes = null;
        this.agentOption = agentOption;
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
    public <T> Class<? extends T> injectClass(ClassLoader classLoader, String className) {
        try {
            final URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            addPluginURLIfAbsent(urlClassLoader, className);
            return (Class<T>) urlClassLoader.loadClass(className);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private synchronized void addPluginURLIfAbsent(URLClassLoader classLoader, String className) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        List<String> pluginJars = agentOption.getPluginJars();
        for (String pluginJar : pluginJars) {
            String[] split = pluginJar.split("/");
            String detail = split[split.length - 1];
            String[] jarInfo = detail.split("-");
            String jar = jarInfo[0];
            if (className.toUpperCase().contains(jar.toUpperCase())) {
                System.out.println("className:" + className + "\t" + pluginJar);
                ADD_URL.invoke(classLoader, toUrl(new File(pluginJar)));
            }
        }
    }

    private static URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL:" + file);
        }
    }
}
