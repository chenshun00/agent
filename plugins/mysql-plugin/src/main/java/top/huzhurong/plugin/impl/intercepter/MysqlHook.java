package top.huzhurong.plugin.impl.intercepter;

import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.ResultSetImpl;
import com.mysql.jdbc.RowData;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.common.HoldData;
import top.huzhurong.test.common.ThreadData;
import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerFactory;

import java.lang.reflect.Field;


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
        Thread thread = Thread.currentThread();
        ThreadData threadData = new ThreadData();
        threadData.setCurTime(System.currentTimeMillis());
        threadData.setKey(thread.getId());
        HoldData.putData((int) thread.getId(), threadData);
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
        ThreadData andRemove = HoldData.getAndRemove((int) Thread.currentThread().getId());
        long exe = 0L;
        if (andRemove != null) {
            exe = System.currentTimeMillis() - andRemove.getCurTime();
        }
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
    public static String getSql(Buffer bufferr) {
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
