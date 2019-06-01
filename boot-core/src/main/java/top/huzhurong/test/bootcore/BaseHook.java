package top.huzhurong.test.bootcore;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/13
 */
public interface BaseHook {

    /**
     * 进入方法
     * <p>
     * * @param curObject 调用该方法的对象，如果方法是静态的那么为null
     * * @param args      参数，如果没有参数，那么为null
     */
    void into(Object curObject, int index, Object[] args);

    /**
     * 出去方法
     *
     * @param result 返回值
     * @param cur    当前对象this/null
     * @param args   参数，如果没有参数，那么为null
     */
    void out(Object result, Object cur, int index, Object[] args);


    /**
     * 执行方法出现异常
     *
     * @param ex   执行方法出现的异常(该异常需要重新抛出，上层应用可能会获取该异常并且进行处理)
     * @param args 参数，如果没有参数，那么为null
     */
    void error(Throwable ex, int index, Object[] args);
}
