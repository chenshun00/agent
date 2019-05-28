package top.huzhurong.test.other.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.BeanMethodRegister;
import top.huzhurong.test.bootcore.HookRegister;
import top.huzhurong.test.bootcore.bean.BeanInfo;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanMethodAdapter extends AdviceAdapter {

    private final int key;
    private final long hookIndex;
    private Label start = new Label();
    private Label end = new Label();
    private Label globalStart = new Label();// 方法方法字节码结束位置
    private Label globalEnd = new Label();// 方法方法字节码结束位置
    private int hook;
    private int result;
    private int excep;

    protected BeanMethodAdapter(int api, MethodVisitor methodVisitor, int access, String name,
                                String descriptor, String className, BaseHook baseHook) {
        super(api, methodVisitor, access, name, descriptor);

        key = BeanMethodRegister.hookKey(className, name);
        hookIndex = HookRegister.hookKey(baseHook);
        int next = Type.getArgumentTypes(this.methodDesc).length;
        hook = next + 1;
        result = hook + 1;
        excep = result + 1;
    }

    private int loadParam(String name, String desc, Label start, Label end) {
        mv.visitLocalVariable(name, desc, null, start, end, hook);
        return hook;
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        BeanInfo beanInfo = BeanMethodRegister.get(key);
        beanInfo.setLineNumber(String.valueOf(line));
        super.visitLineNumber(line, start);
    }

    /**
     * 方法进入
     */
    @Override
    public void visitCode() {
        mv.visitCode();
        loadParam("hook", "Ltop/huzhurong/test/bootcore/BaseHook;", globalStart, globalEnd);
        load();
        mark(globalStart);
        mv.visitVarInsn(ALOAD, hook);
        insertParameter();
        mv.visitMethodInsn(INVOKEINTERFACE, "top/huzhurong/test/bootcore/BaseHook", "into", "(Ljava/lang/Object;I[Ljava/lang/Object;)V", true);
        mv.visitLabel(start);
    }


    private void load() {
        mv.visitLdcInsn(hookIndex);
        mv.visitMethodInsn(INVOKESTATIC, "top/huzhurong/test/bootcore/HookRegister", "get", "(J)Ltop/huzhurong/test/bootcore/BaseHook;", false);
        mv.visitVarInsn(ASTORE, hook);
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
        load();

        loadReturn(opcode);
        mv.visitVarInsn(ALOAD, hook);
        mv.visitVarInsn(ALOAD, result);
        insertParameter();
        mv.visitMethodInsn(INVOKEINTERFACE, "top/huzhurong/test/bootcore/BaseHook", "out", "(Ljava/lang/Object;Ljava/lang/Object;I[Ljava/lang/Object;)V", true);

    }

    private void loadReturn(int opcode) {
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
        mv.visitVarInsn(ASTORE, result);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitLabel(end);
        catchException(start, end, Type.getType(Throwable.class));
        mv.visitVarInsn(ASTORE, excep);
//
        mv.visitVarInsn(ALOAD, hook);
        mv.visitVarInsn(ALOAD, excep);
        insertParameter();
        mv.visitMethodInsn(INVOKEINTERFACE, "top/huzhurong/test/bootcore/BaseHook", "error", "(Ljava/lang/Throwable;Ljava/lang/Object;I[Ljava/lang/Object;)V", true);
        mv.visitVarInsn(ALOAD, excep);
        mv.visitInsn(Opcodes.ATHROW);
        super.visitMaxs(0, 0);
    }

    //    /**
//     * 最后包装一下异常，有可能开发人员会自己处理这个异常，重新将异常抛出
//     */
    @Override
    public void visitEnd() {
        mark(globalEnd);
        mv.visitEnd();
    }

    private void insertParameter() {
        //注入非static方法 参考Modifier#isStatic
        if ((this.methodAccess & 0x00000008) == 0) {
            loadThis();
        } else {
            mv.visitInsn(Opcodes.ACONST_NULL);
        }
        visitKey();
        int size = Type.getArgumentTypes(this.methodDesc).length;
        if (size > 0) {
            loadArgArray();
        } else {
            mv.visitInsn(Opcodes.ACONST_NULL);
        }
    }

    private void visitKey() {
        if (key >= -1 && key <= 5) {
            mv.visitInsn(key);
        } else if (key >= Byte.MIN_VALUE && key <= Byte.MAX_VALUE) {
            mv.visitIntInsn(BIPUSH, +key);
        } else if (key >= Short.MIN_VALUE && key <= Short.MAX_VALUE) {
            mv.visitIntInsn(Opcodes.SIPUSH, key);
        } else {
            mv.visitLdcInsn(key);
        }
    }

}
