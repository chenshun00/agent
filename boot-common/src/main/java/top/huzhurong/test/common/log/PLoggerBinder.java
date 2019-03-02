package top.huzhurong.test.common.log;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/1
 */
public interface PLoggerBinder {
    AgentLog getLogger(String name);

    void shutdown();
}
