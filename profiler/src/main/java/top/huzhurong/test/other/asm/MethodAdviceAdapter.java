package top.huzhurong.test.other.asm;

import top.huzhurong.test.bootcore.BaseHook;
import top.huzhurong.test.bootcore.HookRegister;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

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
    private Label start = new Label();// 方法方法字节码开始位置
    private Label end = new Label();// 方法方法字节码结束位置
    private int next;

    /**
     * Constructs a new {@link AdviceAdapter}.
     *
     * @param api           the ASM API version implemented by this visitor. Must be one of {@link
     *                      Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
     * @param methodVisitor the method visitor to which this adapter delegates calls.
     * @param access        the method's access flags (see {@link Opcodes}).
     * @param name          the method's name.
     * @param descriptor    the method's descriptor (see {@link Type Type}).
     */
    public MethodAdviceAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, BaseHook baseHook) {
        super(api, methodVisitor, access, name, descriptor);
        key = HookRegister.hookKey(baseHook);
        next = Type.getArgumentTypes(this.methodDesc).length + 1;
    }

    /**
     * 方法进入
     */
    @Override
    public void visitCode() {
        next++;
        mv.visitCode();
        mv.visitLabel(start);
        Label l0 = new Label();
        mv.visitLdcInsn(key);
        mv.visitMethodInsn(INVOKESTATIC, "top/huzhurong/test/bootcore/HookRegister", "get", "(J)Ltop/huzhurong/test/bootcore/Hook;", false);
        int first = next;
        mv.visitVarInsn(ASTORE, first);
        mv.visitVarInsn(ALOAD, first);
        mv.visitTypeInsn(CHECKCAST, "top/huzhurong/test/bootcore/BaseHook");
        Label l1 = new Label();
        next++;
        int second = next;
        mv.visitVarInsn(ASTORE, second);
        Label l2 = new Label();
        mv.visitVarInsn(ALOAD, second);
        insertParameter();
        //Ljava/lang/Object;[Ljava/lang/Object;
        mv.visitMethodInsn(INVOKEINTERFACE, "top/huzhurong/test/bootcore/BaseHook", "into", "(Ljava/lang/Object;[Ljava/lang/Object;)V", true);
        mv.visitLocalVariable("chen", "Ltop/huzhurong/test/bootcore/Hook;", null, l0, l1, first);
        mv.visitLocalVariable("dd", "Ltop/huzhurong/test/bootcore/BaseHook;", null, l1, l2, second);
        super.visitCode();
    }

    private void handleLocal() {
        Label start = new Label();
        Label end = new Label();
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
        next++;
        int first = next;
        mv.visitLdcInsn(key);
        mv.visitMethodInsn(INVOKESTATIC, "top/huzhurong/test/bootcore/HookRegister", "get", "(J)Ltop/huzhurong/test/bootcore/Hook;", true);
        mv.visitTypeInsn(CHECKCAST, "top/huzhurong/test/bootcore/BaseHook");
        mv.visitVarInsn(ASTORE, first);
        next++;
        int second = next;
        if (opcode == ATHROW) {
            return;
        }
        if (opcode == RETURN) {
            visitInsn(ACONST_NULL);
            mv.visitVarInsn(ASTORE, second);
        } else if (opcode == ARETURN) {
            dup();
            mv.visitVarInsn(ASTORE, second);
        } else {
            //long double 类型是2个嘈 slot
            if ((opcode == LRETURN) || (opcode == DRETURN)) {
                dup2();
                mv.visitVarInsn(ASTORE, second);
            } else {
                dup();
                mv.visitVarInsn(ASTORE, second);
            }
            box(Type.getReturnType(this.methodDesc));
        }
        mv.visitVarInsn(ALOAD, first);
        mv.visitVarInsn(ALOAD, second);
        insertParameter();
        mv.visitMethodInsn(INVOKEINTERFACE, "top/huzhurong/test/bootcore/BaseHook", "out", "(Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)V", true);
    }

    /**
     * 最后包装一下异常，有可能开发人员会自己处理这个异常，重新将异常抛出
     */
    @Override
    public void visitEnd() {
        mv.visitLabel(end);
        mv.visitTryCatchBlock(start, end, end, null);
        next++;
        int second = next;
        dup();
        mv.visitVarInsn(ASTORE, second);
//
        next++;
        int first = next;
        mv.visitLdcInsn(key);
        mv.visitMethodInsn(INVOKESTATIC, "top/huzhurong/test/bootcore/HookRegister", "get", "(J)Ltop/huzhurong/test/bootcore/Hook;", true);
        mv.visitTypeInsn(CHECKCAST, "top/huzhurong/test/bootcore/BaseHook");
        mv.visitVarInsn(ASTORE, first);
//
//
//
        mv.visitVarInsn(ALOAD, first);
        mv.visitVarInsn(ALOAD, second);
        insertParameter();
        mv.visitMethodInsn(INVOKEINTERFACE, "top/huzhurong/test/bootcore/BaseHook", "error", "(Ljava/lang/Throwable;Ljava/lang/Object;[Ljava/lang/Object;)V", true);
        mv.visitInsn(ATHROW); // 重新把异常抛出
        mv.visitEnd();
    }

    private void insertParameter() {
        //mv.visitLdcInsn(key);
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

    public void visitMaxs(int maxStack, int maxLocals) {
        if (maxStack == 0) {
            super.visitMaxs(11, maxLocals + 1);
        } else {
            super.visitMaxs(maxStack + 10, maxLocals + 1);
        }
    }
}
