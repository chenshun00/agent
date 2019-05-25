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
 * 调用方法的原则是先将参数入栈，然后在调用方法的时候一个一个的将参数从操作数栈中弹出，同时在方法的底部，即return的前一个指令，必然是返回值
 * 1、方法参数入栈，例如dup(),加入其他参数，ldc都可以
 * 2、调用方法 invoke，根据方法的描述符去栈顶拿参数
 *
 * @author chenshun00@gmail.com
 * @since 2018/9/29
 */
public class MethodAdviceAdapter extends AdviceAdapter {

    private long key;
    private int methodKey;
    private Label start;// 方法方法字节码开始位置
    private Label end;// 方法方法字节码结束位置
    private Label globalStart = new Label();// 方法方法字节码结束位置
    private Label globalEnd = new Label();// 方法方法字节码结束位置
    private int next;
    private int hook;
    private int excep;
    private int result;

    public MethodAdviceAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, BaseHook baseHook, String className) {
        super(api, methodVisitor, access, name, descriptor);
        methodKey = BeanMethodRegister.hookKey(className, name);
        key = HookRegister.hookKey(baseHook);
        next = Type.getArgumentTypes(this.methodDesc).length + 1;
        hook = next + 1;
        result = hook + 1;
        excep = result + 1;
    }

    /**
     * 添加一个局部变量，然后局部变量表的槽+1，
     *
     * @param name 变量名称
     * @param desc 描述符
     */
    private int loadParam(String name, String desc, Label start, Label end) {
        mv.visitLocalVariable(name, desc, null, start, end, hook);
        return hook;
    }

    /**
     * 方法进入
     */
    @Override
    public void visitCode() {
        mv.visitCode();
        start = new Label();
        end = new Label();
        loadParam("hook", "Ltop/huzhurong/test/bootcore/Hook;", globalStart, globalEnd);

        mv.visitLdcInsn(key);
        mv.visitMethodInsn(INVOKESTATIC, "top/huzhurong/test/bootcore/HookRegister", "get", "(J)Ltop/huzhurong/test/bootcore/Hook;", false);
        mv.visitVarInsn(ASTORE, hook);

        mv.visitLabel(globalStart);

        mv.visitVarInsn(ALOAD, hook);
        insertParameter();
        mv.visitMethodInsn(INVOKEINTERFACE, "top/huzhurong/test/bootcore/BaseHook", "into", "(Ljava/lang/Object;I[Ljava/lang/Object;)V", true);

        mv.visitLabel(start);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        BeanInfo beanInfo = BeanMethodRegister.get(methodKey);
        beanInfo.setLineNumber(String.valueOf(line));
        super.visitLineNumber(line, start);
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
        mv.visitLdcInsn(key);
        mv.visitMethodInsn(INVOKESTATIC, "top/huzhurong/test/bootcore/HookRegister", "get", "(J)Ltop/huzhurong/test/bootcore/Hook;", false);
        mv.visitVarInsn(ASTORE, hook);

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

        mv.visitVarInsn(ALOAD, hook);
        mv.visitVarInsn(ALOAD, excep);
        insertParameter();
        mv.visitMethodInsn(INVOKEINTERFACE, "top/huzhurong/test/bootcore/BaseHook", "error", "(Ljava/lang/Throwable;Ljava/lang/Object;I[Ljava/lang/Object;)V", true);


        mv.visitVarInsn(ALOAD, excep);
        mv.visitInsn(Opcodes.ATHROW);
        super.visitMaxs(-1, -1);
    }

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
        mv.visitLdcInsn(methodKey);
        int size = Type.getArgumentTypes(this.methodDesc).length;
        if (size > 0) {
            loadArgArray();
        } else {
            mv.visitInsn(Opcodes.ACONST_NULL);
        }
    }
}
