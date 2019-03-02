package top.huzhurong.test.other.asm.context;

import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.asm.ASMContext;
import top.huzhurong.test.other.asm.AsmAgentHook;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/1
 */
public class ASMCLass extends AbstractContext implements ASMContext {

    private byte[] classfileBuffer;
    private String className;

    public ASMCLass(byte[] classfileBuffer, String className) {
        this.classfileBuffer = classfileBuffer;
        this.className = className;
    }

    public byte[] tranform(BaseHook baseHook, String method, String desc) {
        ClassReader classReader = new ClassReader(classfileBuffer);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        classReader.accept(new AsmAgentHook(Opcodes.ASM5, classWriter, baseHook, method, desc), ClassReader.EXPAND_FRAMES);
        writeToFile(classWriter, className);
        return classWriter.toByteArray();
    }

}
