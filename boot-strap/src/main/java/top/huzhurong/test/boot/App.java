package top.huzhurong.test.boot;

import top.huzhurong.test.boot.agent.BootInfo;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/15
 */
public class App {

    private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();

    public static void premain(String args, Instrumentation instrumentation) throws Exception {
        if (App.class.getClassLoader() != Object.class.getClassLoader()) {
            System.err.println("引导jar包没有被bootClassLoader加载");
            return;
        }

        String agentPath = getAgentPath();
        if (agentPath == null) {
            System.err.println("获取javaagent失败");
            return;
        }
        BootInfo bootInfo = new BootInfo();
        bootInfo.setPath(agentPath);
        bootInfo.setCoreJar(agentPath + "core");
        bootInfo.setLibJar(agentPath + "lib");
        bootInfo.setPluginJar(agentPath + "plugin");
        System.setProperty("log4j", bootInfo.getLibJar());
        File dir = new File(bootInfo.getLibJar());
        File[] files = dir.listFiles();
        assert files != null;
        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++) {
            urls[i] = toUrl(files[i]);
        }
        final ClassLoader agentClassLoader = createPluginClassLoader(urls, Object.class.getClassLoader());
        Starter starter = new Starter();
        starter.start(agentClassLoader, instrumentation, bootInfo);
    }


    private static ClassLoader createPluginClassLoader(final URL[] urls, final ClassLoader parent) {
        if (SECURITY_MANAGER != null) {
            return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    return new MyClassLoader(urls, parent);
                }
            });
        } else {
            return new MyClassLoader(urls, parent);
        }
    }

    private static URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL:" + file);
        }
    }

    private static String getAgentPath() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> inputArguments = runtimeMXBean.getInputArguments();
        for (String inputArgument : inputArguments) {
            if (inputArgument.startsWith("-javaagent") && inputArgument.endsWith(".jar")) {
                return inputArgument.substring(11).replace("boot-strap-1.0-SNAPSHOT.jar", "");
            }
        }
        return null;
    }
}
