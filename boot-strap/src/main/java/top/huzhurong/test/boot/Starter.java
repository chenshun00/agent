package top.huzhurong.test.boot;

import top.huzhurong.test.boot.agent.BootInfo;
import top.huzhurong.test.bootcore.Agent;
import top.huzhurong.test.bootcore.AgentOption;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/15
 */
public class Starter {
    public void start(final ClassLoader pluginClassLoader, Instrumentation instrumentation, BootInfo bootInfo) throws IOException {
        List<String> stringList = resolvePath(bootInfo.getCoreJar());
        appendToBootstrapClassLoader(instrumentation, stringList);
        Agent boot = boot(pluginClassLoader, instrumentation, bootInfo);
        if (boot == null) {
            System.err.println("加载agent失败");
        } else {
            boot.start();
        }
    }

    private static void appendToBootstrapClassLoader(Instrumentation instrumentation, List<String> stringList) throws IOException {
        for (String zz : stringList) {
            File file = toFile(zz);
            JarFile jarFile = new JarFile(file, true);
            instrumentation.appendToBootstrapClassLoaderSearch(jarFile);
        }
    }

    private static File toFile(String filePath) {
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

    private Agent boot(ClassLoader classLoader, Instrumentation instrumentation, BootInfo bootInfo) {
        try {
            Class<?> aClass = classLoader.loadClass("top.huzhurong.test.other.DefaultAgent");
            Constructor<?> constructor = aClass.getConstructor(AgentOption.class);

            AgentOption agentOption = new AgentOption();
            agentOption.setInstrumentation(instrumentation);
            agentOption.setPath(bootInfo.getPath());
            agentOption.setClassLoader(classLoader);
            List<String> stringList = resolvePath(bootInfo.getPluginJar());
            agentOption.setPluginJars(stringList);
            Object o = constructor.newInstance(agentOption);
            if (o instanceof Agent) {
                Agent agent = (Agent) o;
                return agent;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> resolvePath(String pathath) {
        final File directory = new File(pathath);

        final File[] jars = listFiles(directory, Collections.singletonList(".jar"));
        if (isEmpty(jars)) {
            return Collections.emptyList();
        }
        return filterReadPermission(jars);
    }

    private List<String> filterReadPermission(File[] jars) {
        List<String> result = new ArrayList<String>();
        for (File pluginJar : jars) {
            if (!pluginJar.canRead()) {
                continue;
            }

            result.add(pluginJar.getPath());
        }
        return result;
    }

    public static File[] listFiles(final File path, final List<String> fileExtensionList) {
        if (path == null) {
            throw new NullPointerException("path must not be null");
        }
        if (fileExtensionList == null) {
            throw new NullPointerException("fileExtensionList must not be null");
        }
        return path.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String path = pathname.getName();
                for (String extension : fileExtensionList) {
                    if (path.lastIndexOf(extension) != -1) {
                        return true;
                    }
                }
                return false;
            }
        });
    }


    public static boolean isEmpty(File[] files) {
        return files == null || files.length == 0;
    }


    public static String toCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

}
