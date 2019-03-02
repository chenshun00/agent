package top.huzhurong.test.bootcore;

import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/15
 */
public class AgentOption {
    private Instrumentation instrumentation;
    private String path;
    private ClassLoader classLoader;
    private List<String> pluginJars;

    public List<String> getPluginJars() {
        return pluginJars;
    }

    public void setPluginJars(List<String> pluginJars) {
        this.pluginJars = pluginJars;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }
}
