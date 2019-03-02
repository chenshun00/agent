package top.huzhurong.test.boot.agent;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/2
 */
public class BootInfo {
    private String path;
    private String coreJar;
    private String libJar;
    private String pluginJar;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCoreJar() {
        return coreJar;
    }

    public void setCoreJar(String coreJar) {
        this.coreJar = coreJar;
    }

    public String getLibJar() {
        return libJar;
    }

    public void setLibJar(String libJar) {
        this.libJar = libJar;
    }

    public String getPluginJar() {
        return pluginJar;
    }

    public void setPluginJar(String pluginJar) {
        this.pluginJar = pluginJar;
    }
}
