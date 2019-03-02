package top.huzhurong.test.other.plugin;

import top.huzhurong.test.bootcore.TransformCallback;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/23
 */
public interface TransformCallbackProvider {
    TransformCallback getTransformCallback(ClassLoader loader);
}
