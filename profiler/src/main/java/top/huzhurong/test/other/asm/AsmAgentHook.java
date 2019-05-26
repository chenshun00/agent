package top.huzhurong.test.other.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.huzhurong.test.bootcore.BaseHook;

import java.util.Arrays;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/16
 */
public class AsmAgentHook extends ClassVisitor {

    private Logger logger = LoggerFactory.getLogger(AsmAgentHook.class);

    private BaseHook baseHook;
    private List<String> methodName;
    private String desc;
    private String className;

    public AsmAgentHook(String className, int api, ClassWriter classVisitor, BaseHook baseHook, String[] methodName, String desc) {
        super(api, classVisitor);
        this.className = className;
        this.baseHook = baseHook;
        this.methodName = Arrays.asList(methodName);
        this.desc = desc;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if (baseHook != null && this.methodName.contains(name) && (this.desc == null || this.desc.equals(descriptor))) {
            logger.info("ASM 修改字节码 [{}] [{}] [{}]", name, this.desc, descriptor);
            return new BeanMethodAdapter(api, mv, access, name, descriptor, className, baseHook);
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
