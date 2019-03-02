package top.huzhurong.test.common.plugin;


import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author Woonduk Kang(emeroad)
 */
public final class JarFileUtils {

    private JarFileUtils() {
    }

    public static String getManifestValue(JarFile jarFile, String key, String defaultValue) {
        final Manifest manifest = getManifest(jarFile);
        if (manifest == null) {
            return defaultValue;
        }

        final Attributes attributes = manifest.getMainAttributes();
        final String value = attributes.getValue(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }


    public static Manifest getManifest(JarFile pluginJarFile) {
        try {
            return pluginJarFile.getManifest();
        } catch (IOException ex) {
            return null;
        }
    }
}
