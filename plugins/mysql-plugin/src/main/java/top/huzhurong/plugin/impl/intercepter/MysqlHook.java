package top.huzhurong.plugin.impl.intercepter;

import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetImpl;
import com.mysql.jdbc.RowData;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;
import top.huzhurong.test.common.trace.Span;
import top.huzhurong.test.common.trace.TraceContext;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.UUID;


/**
 * @author chenshun00@gmail.com
 * @since 2018/10/14
 */
public class MysqlHook implements BaseHook {

    private AgentLog logger = PLoggerFactory.getLogger(this.getClass());

    private MysqlHook() {
    }

    public static final MysqlHook Instance = new MysqlHook();

    @Override
    public void into(Object curObject, Object[] args) {
        String url = "";
        try {
            if (curObject instanceof PreparedStatement) {
                PreparedStatement preparedStatement = (PreparedStatement) curObject;
                Connection connection = preparedStatement.getConnection();
                url = connection.getMetaData().getURL();
            }
        } catch (Exception ex) {
            logger.error("获取sql URL为空:[{}]", ex.getMessage(), ex);
        }
        Span span = TraceContext.getSpan();
        TraceContext.removeSpan();
        Span newSpan = new Span();
        newSpan.setUrl(url);
        newSpan.setTag("Mysql");
        newSpan.setSpanId(UUID.randomUUID().toString());
        newSpan.setsTime(System.currentTimeMillis());
        newSpan.setPspanId(span.getSpanId());
        TraceContext.setSpan(span);
        if (curObject != null) {
            String ss = curObject.toString();
            if (ss.length() > 47 && ss.length() < 500) {
                logger.info("执行前sql:[{}]", ss.substring(47).trim()
                        .replaceAll("\t", "").replaceAll("\n", "").replaceAll("\\s+", " "));
            } else {
                logger.info("执行前sql:[{}]", ss.substring(47).trim());
            }
        }
    }

    @Override
    public void out(Object result, Object cur, Object[] args) {
        Span span = TraceContext.getSpan();
        span.seteTime(System.currentTimeMillis());
        long exe = span.geteTime() - span.getsTime();
        String sql = "";
        if (args[1] instanceof Buffer) {
            Buffer buffer = (Buffer) args[1];
            sql += getSql(buffer);
            //如果sql比较长replaceAll对于性能的影响还是比较大的
            sql = sql.replaceAll("\t", "")
                    .replaceAll("\n", "").replaceAll("\\s+", " ");
        }
        if (result instanceof ResultSetImpl) {
            ResultSetImpl resultSet = (ResultSetImpl) result;
            try {
                Field rowData = resultSet.getClass().getSuperclass().getDeclaredField("rowData");
                rowData.setAccessible(true);
                RowData req = (RowData) rowData.get(resultSet);
                long size;
                if (req == null) {
                    size = resultSet.getUpdateCount();
                } else {
                    size = req.size();
                }
                logger.info("sql:[{}] [{}] [{}(ms)]", sql, size, exe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void error(Throwable ex, Object curObject, Object[] args) {
        logger.error("[执行sql出错] [{}] [{}]", curObject.toString(), ex.getMessage());
    }


    //从字节数组中计算sql语句
    static String getSql(Buffer bufferr) {
        StringBuilder buffer = new StringBuilder();
        byte[] getByteBuffers = bufferr.getByteBuffer();
        for (int i = 0; i < 4; ++i) {
            String hexVal = Integer.toHexString(getByteBuffers[i] & 255);
            if (hexVal.length() == 1) {
                hexVal = "0" + hexVal;
            }
            buffer.insert(0, hexVal);
        }
        Integer integer = Integer.decode(buffer.insert(0, "0x").toString());
        if (integer == 0) {
            return "";
        }
        if (integer > 2048) {
            integer = 2048;
        }
        byte[] bytes = new byte[integer];
        System.arraycopy(getByteBuffers, 5, bytes, 0, integer - 1);
        return new String(bytes).replace("\r", " ").replace("\n", " ").replace("\\s+", " ").trim();
    }
}
