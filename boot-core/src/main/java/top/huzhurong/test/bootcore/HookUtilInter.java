package top.huzhurong.test.bootcore;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/16
 */
public interface HookUtilInter {
    void enterMethod(long key, Object currentObject, Object[] args);

    void endMethod(Object ret, long key, Object object, Object[] args);

    void errorMethod(Throwable ex, long key, Object cur, Object[] args);
}
