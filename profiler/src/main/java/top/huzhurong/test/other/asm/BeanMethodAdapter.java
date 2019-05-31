package top.huzhurong.test.other.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.bootcore.HookRegister;
import top.huzhurong.test.bootcore.Invoke;
import top.huzhurong.test.bootcore.bean.BeanInfo;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanMethodAdapter extends AdviceAdapter {

    private final long key;
    private Label globalStart = new Label();// 方法方法字节码结束位置
    private Label globalEnd = new Label();// 方法方法字节码结束位置

    protected BeanMethodAdapter(int api, MethodVisitor methodVisitor, int access, String name,
                                String descriptor, String className, BaseHook baseHook) {
        super(api, methodVisitor, access, name, descriptor);
        HookRegister.hookKey(baseHook);
        key = BeanMethodRegister.hookKey(className, name);
    }


    @Override
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);
        BeanInfo beanInfo = BeanMethodRegister.get(key);
        beanInfo.setLineNumber(String.valueOf(line));
    }


    /**
     * 方法进入
     */
    @Override
    public void visitCode() {
        mv.visitCode();
        insertParameter();
        //(JLjava/lang/Object;[Ljava/lang/Object;)V
        mv.visitMethodInsn(INVOKESTATIC, Invoke.OWNER, Invoke.INTO_NAME, Invoke.INTO_METHOD, false);
        mark(globalStart);
    }

    /**
     * 方法退出
     * <p>
     * opcode 代表的指令是方法返回的值，例如 RETURN 是void的返回值
     * dup 复制栈顶数值，并压入栈顶,在方法的最后，栈顶元素是返回值了
     *
     * @param opcode one of {@link Opcodes#RETURN}, {@link Opcodes#IRETURN}, {@link Opcodes#FRETURN},
     *               {@link Opcodes#ARETURN}, {@link Opcodes#LRETURN}, {@link Opcodes#DRETURN} or {@link
     */
    @Override
    public void onMethodExit(int opcode) {
        //如果是抛出异常，直接返回，不进入
        if (opcode == Opcodes.ATHROW) {
            return;
        }
        if (opcode == Opcodes.RETURN) {
            mv.visitInsn(ACONST_NULL);
        } else if (opcode == Opcodes.ARETURN) {
            dup();
        } else {
            if (opcode == Opcodes.LRETURN || opcode == Opcodes.DRETURN) {
                dup2();
            } else {
                dup();
            }
            final Type type = Type.getReturnType(this.methodDesc);
            box(type);
        }
        insertParameter();
        //"(JLjava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)V";
        visitMethodInsn(INVOKESTATIC, Invoke.OWNER, Invoke.OUT_NAME, Invoke.OUT_METHOD, false);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        mark(globalEnd);
        catchException(globalStart, globalEnd, Type.getType(Throwable.class));
        dup();
        insertParameter();
        visitMethodInsn(INVOKESTATIC, Invoke.OWNER, Invoke.ERROR_NAME, Invoke.ERROR_METHOD, false);
        mv.visitInsn(Opcodes.ATHROW);
        mv.visitMaxs(maxStack, maxLocals);
    }

    /**
     * 最后包装一下异常，有可能开发人员会自己处理这个异常，重新将异常抛出
     */
    @Override
    public void visitEnd() {
        mv.visitEnd();
    }

    private void insertParameter() {
        mv.visitLdcInsn(key);
        //注入非static方法 参考Modifier#isStatic
        if ((this.methodAccess & 0x00000008) == 0) {
            loadThis();
        } else {
            mv.visitInsn(Opcodes.ACONST_NULL);
        }
        int size = Type.getArgumentTypes(this.methodDesc).length;
        if (size > 0) {
            loadArgArray();
        } else {
            mv.visitInsn(Opcodes.ACONST_NULL);
        }
    }

}
