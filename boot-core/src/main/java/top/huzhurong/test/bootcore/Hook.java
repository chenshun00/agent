package top.huzhurong.test.bootcore;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/17
 */
public interface Hook {
    /**
     * 进入方法
     * <p>
     * * @param curObject 调用该方法的对象，如果方法是静态的那么为null
     * * @param args      参数，如果没有参数，那么为null
     */
    void into(Object curObject, Object[] args);


    void into(int index, Object[] args);

    /**
     * 出去方法
     *
     * @param cur  返回值，如果没有返回值，那么是null
     * @param args 参数，如果没有参数，那么为null
     */
    void out(Object result, Object cur, Object[] args);

    void out(Object result, int index, Object[] args);


    /**
     * 执行方法出现异常
     *
     * @param curObject 调用该方法的对象，如果方法是静态的那么为null
     * @param ex        执行方法出现的异常(该异常需要重新抛出，上层应用可能会获取该异常并且进行处理)
     * @param args      参数，如果没有参数，那么为null
     */
    void error(Throwable ex, Object curObject, Object[] args);

    void error(Throwable ex, int index, Object[] args);
}
