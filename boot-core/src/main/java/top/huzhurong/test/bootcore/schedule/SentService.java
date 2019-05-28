package top.huzhurong.test.bootcore.schedule;

import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;

import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/27
 */
public class SentService {

    private static AgentLog logger = PLoggerFactory.getLogger(SentService.class);

    private static Queue obj = new LinkedBlockingQueue(100000);

    public ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();


    public static void push(Object object) {
        obj.offer(object);
    }

    private static void handle(Object poll) {
        logger.info("[poll] [{}]", poll);
    }

    public SentService() {
        service.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (obj.size() == 0) {
                    return;
                }
                while (true) {
                    Object poll = obj.poll();
                    if (poll != null) {
                        handle(poll);
                    } else {
                        break;
                    }
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }
}
