package top.huzhurong.test.boot;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/13
 */
public class MyClassLoader extends URLClassLoader {

    private final ClassLoader parent;

    private final BootLoader bootLoader = BootLoaderFactory.newBootLoader();

    static {
        if (!ClassLoader.registerAsParallelCapable()) {
            System.err.println("MyClassLoader ParallelClassLoader::registerAsParallelCapable() fail");
        }
    }

    public MyClassLoader(URL[] urls) {
        super(urls);
        parent = Object.class.getClassLoader();
    }

    public MyClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.parent = parent;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class clazz = findLoadedClass(name);
            if (clazz == null) {
                //是不是应该被我加载
                if (onLoadClass(name)) {
                    // load a class used for Pinpoint itself by this ClassLoader
                    clazz = findClass(name);
                } else {
                    try {
                        // load a class by parent ClassLoader
                        if (parent != null) {
                            clazz = parent.loadClass(name);
                        } else {
                            clazz = this.bootLoader.findBootstrapClassOrNull(this, name);
                        }
                    } catch (ClassNotFoundException ignore) {
                    }
                    if (clazz == null) {
                        // if not found, try to load a class by this ClassLoader
                        clazz = findClass(name);
                    }
                }
            }
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }
    }

    @Override
    public String toString() {
        return "MyClassLoader{" +
                "parent=" + parent +
                ", bootLoader=" + bootLoader +
                '}';
    }

    private boolean onLoadClass(String name) {
        return name.startsWith("top.huzhurong.test.classloader");
    }
}
