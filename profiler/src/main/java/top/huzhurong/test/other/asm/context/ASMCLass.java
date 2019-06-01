package top.huzhurong.test.other.asm.context;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.other.asm.AsmAgentHook;
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

    /**
     * new ClassWriter(0)时,不会自动计算任何东西。必须自行计算帧(StackMapFrame)、局部变量与操作数栈的大小。使用visitMaxs手动传入局部标量和操作数栈的大小
     * new ClassWriter(ClassWriter.COMPUTE_MAXS)时,将为你计算局部变量与操作数栈部分的大小，但不会计算StackMapFrame。visitMaxs仍然需要调用,但可以使用任何参数:它们将被忽略并重新计算
     * 在 new ClassWriter(ClassWriter.COMPUTE_FRAMES)时,一切都是自动计算。不再需要调用 visitFrame,但仍然必须调用 visitMaxs(参数将被忽略并重新计算)
     */
    public byte[] tranform(BaseHook baseHook, String[] method, String desc) {
        ClassReader classReader = new ClassReader(classfileBuffer);
        ClassWriter classWriter = new TraceClassWriter(classReader, ClassWriter.COMPUTE_MAXS, this.classLoader);
        try {
            classReader.accept(new AsmAgentHook(className, Opcodes.ASM7, classWriter, baseHook, method, desc), ClassReader.SKIP_FRAMES);
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
            ClassWriter classWriter = new TraceClassWriter(classReader, ClassWriter.COMPUTE_MAXS, this.classLoader);
            classReader.accept(new BeanAgentHook(Opcodes.ASM7, classWriter, baseHook, className), ClassReader.SKIP_FRAMES);
            writeToFile(classWriter, className);
            return classWriter.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
