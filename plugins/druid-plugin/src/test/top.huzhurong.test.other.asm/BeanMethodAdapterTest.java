package top.huzhurong.test.other.asm;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import top.huzhurong.plugin.impl.intercepter.DruidHook;

import static top.huzhurong.test.other.asm.context.AbstractContext.writeToFile;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/29
 */
public class BeanMethodAdapterTest {

    @Test
    public void testDruidInjectByteCode() throws Exception {
        ClassReader classReader = new ClassReader("com.alibaba.druid.pool.DruidDataSource");
        ClassWriter classWriter = new TraceClassWriter(classReader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES, this.getClass().getClassLoader());
        classReader.accept(new AsmAgentHook("com/alibaba/druid/pool/DruidDataSource", Opcodes.ASM7, classWriter, DruidHook.Instance, new String[]{"getConnection"}, "(J)Lcom/alibaba/druid/pool/DruidPooledConnection;"), ClassReader.EXPAND_FRAMES);
        writeToFile(classWriter, "com.alibaba.druid.pool.DruidDataSource");
    }
}
