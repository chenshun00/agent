package top.huzhurong.test.other.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.huzhurong.test.bootcore.BaseHook;

import java.util.Arrays;
import java.util.List;

/**
 * LocalVariablesSorter 方法适配器将一个方法中使用的局部变量按照它们在这个方法中的出现顺序重新进行编号
 *
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

    /**
     * 除了一些与注释和调试信息有关的方法之外,这个类为每个字节代码指令类别定义了一个方法,其依据就是这些指令的参数个数和类型。
     * 对于非抽象方法,如果存在注释和属性的话,必须首先访问它们,然后是该方 法的字节代码。
     * 对于这些方法,其代码必须按顺序访问,位于对 visitCode 的调用(有且仅有一个调用)与对 visitMaxs 的调用(有且仅有一个调用)之间。
     */
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
