package top.huzhurong.test.other.asm.context;

import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.other.asm.AsmAgentHook;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import top.huzhurong.test.other.asm.BeanAgentHook;
import top.huzhurong.test.other.asm.TraceClassWriter;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/1
 */
public class ASMCLass extends AbstractContext implements ASMContext {

    private byte[] classfileBuffer;
    private String className;
    private ClassLoader classLoader;

    public ASMCLass(byte[] classfileBuffer, String className, ClassLoader loader) {
        this.classfileBuffer = classfileBuffer;
        this.className = className;
        this.classLoader = loader;
    }

    public byte[] tranform(BaseHook baseHook, String[] method, String desc) {
        ClassReader classReader = new ClassReader(classfileBuffer);
        ClassWriter classWriter = new TraceClassWriter(classReader, ClassWriter.COMPUTE_FRAMES, this.classLoader);
        try {
            classReader.accept(new AsmAgentHook(className, Opcodes.ASM7, classWriter, baseHook, method, desc), 0);
            writeToFile(classWriter, className);
            return classWriter.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] tranform(BaseHook baseHook) {
        try {
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new TraceClassWriter(classReader, ClassWriter.COMPUTE_FRAMES, this.classLoader);
            classReader.accept(new BeanAgentHook(Opcodes.ASM7, classWriter, baseHook, className), 0);
            writeToFile(classWriter, className);
            return classWriter.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
