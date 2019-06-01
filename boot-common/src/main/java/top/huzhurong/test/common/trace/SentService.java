package top.huzhurong.test.common.trace;

import com.alibaba.fastjson.JSONObject;
import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;
import top.huzhurong.test.common.util.WebUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/27
 */
public class SentService {

    private static ScheduledExecutorService service;

    private static boolean start = false;

    private static AgentLog logger = PLoggerFactory.getLogger(SentService.class);

    private static String LOG_URL = System.getProperty("log.url", "http://127.0.0.1:12345/info");

    private static Queue<Trace<SpanEvent>> obj = new LinkedBlockingQueue<Trace<SpanEvent>>(100000);
    private static Queue<Trace<SpanEvent>> temp = new LinkedBlockingQueue<Trace<SpanEvent>>(10000);

    static {
        service = Executors.newSingleThreadScheduledExecutor();
        start();
    }


    public static void push(Trace<SpanEvent> trace) {
        if (obj.offer(trace)) {
            return;
        }
        temp.offer(trace);
    }

    private static void handle(Trace<SpanEvent> trace) {
        logger.info("[poll] [{}]", trace);
        try {
            String traceId = trace.getTraceId();
            Span<SpanEvent> span = trace.getSpan();
            int size = span.size();
            List<SpanEvent> spanEvents = new ArrayList<SpanEvent>(size);
            for (int i = 0; i < size; i++) {
                spanEvents.add(span.getOne());
            }

            Map<String, String> param = new HashMap<String, String>();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("traceId", traceId);
            jsonObject.put("spanId", span.getSpanId());
            jsonObject.put("parentSpanId", span.getParentSpanId());
            jsonObject.put("endpoint", span.getUrl());
            jsonObject.put("duration", span.getEndTime() - span.getStartTime());
            jsonObject.put("type",span.type);
            jsonObject.put("stack", spanEvents);
            param.put("json", jsonObject.toJSONString());
            WebUtils.doPost(LOG_URL, param, 3000, 3000);
        } catch (IOException e) {
            logger.error("[send agent error] [{}]", e.getMessage(), e);
        } catch (Exception ex) {
            logger.error("[send agent error] [{}]", ex.getMessage(), ex);
            ex.printStackTrace();
        }
    }

    private static void start() {
        if (start) {
            return;
        }
        start = true;
        service.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (obj.size() == 0) {
                    return;
                }
                while (true) {
                    Trace<SpanEvent> poll = obj.poll();
                    if (poll != null) {
                        handle(poll);
                    } else {
                        break;
                    }
                }
                ;
                while (true) {
                    Trace<SpanEvent> poll = temp.poll();
                    if (poll != null) {
                        handle(poll);
                    } else {
                        break;
                    }
                }
            }
        }, 10, 10, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                SentService.service.shutdown();
            }
        }));
    }
}
