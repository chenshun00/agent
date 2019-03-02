package top.huzhurong.test.common.plugin;

import java.net.URL;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/18
 */
public interface Plugin<T> {
    URL getURL();

    List<T> getInstanceList();

    List<String> getPackageList();
}
