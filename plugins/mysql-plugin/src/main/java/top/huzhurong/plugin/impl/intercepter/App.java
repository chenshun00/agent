package top.huzhurong.plugin.impl.intercepter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import top.huzhurong.test.common.util.JvmUtil;
import top.huzhurong.test.other.asm.AsmAgentHook;
import top.huzhurong.test.other.asm.TraceClassWriter;

import static top.huzhurong.test.other.asm.context.AbstractContext.writeToFile;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/30
 */
public class App {
    public static void main(String[] args) throws Exception {
        ClassReader classReader = new ClassReader("com.mysql.jdbc.PreparedStatement");
        ClassWriter classWriter = new TraceClassWriter(classReader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES, App.class.getClassLoader());
        classReader.accept(new AsmAgentHook(JvmUtil.jvmName("com.mysql.jdbc.PreparedStatement"),
                Opcodes.ASM7, classWriter, MysqlHook.Instance, new String[]{"executeInternal"}, null), ClassReader.EXPAND_FRAMES);
        writeToFile(classWriter, "PreparedStatement");
    }
}
