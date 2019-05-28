package top.huzhurong.test.common.trace;

import top.huzhurong.test.common.storge.Storge;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/21
 */
public class LogStorge implements Storge<SpanEvent> {

    @Override
    public boolean storgeInfo(SpanEvent spanEvent) {
        System.out.println(String.format("[%s] [%s] [%s] [%dms]", spanEvent.getLine(), spanEvent.getClassName(),
                spanEvent.getMethod(), (spanEvent.getEndTime() - spanEvent.getStartTime())));
        return true;
    }
}
