package top.huzhurong.test.other.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;
import top.huzhurong.test.bootcore.BaseHook;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanAgentHook extends ClassVisitor {

    private BaseHook baseHook;
    private String className;

    public BeanAgentHook(int api, ClassVisitor classVisitor, BaseHook baseHook, String className) {
        super(api, classVisitor);
        this.baseHook = baseHook;
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);

        if (mv != null && access == Opcodes.ACC_PUBLIC && !name.equalsIgnoreCase("<init>")) {
            BeanMethodAdapter beanMethodAdapter = new BeanMethodAdapter(api, mv, access, name, descriptor, this.className, this.baseHook);
            return new TraceMethodVisitor(beanMethodAdapter, new Textifier());
        } else {
            return mv;
        }
    }
}
