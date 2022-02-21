package org.learn.asm.classvisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

/**
 * 方法调用后增强
 *
 * @author : zhangzy
 * @date : 2022/2/21
 */
public class MethodInvokeAfterVisitor extends ClassVisitor {

    public MethodInvokeAfterVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null && !"<init>".equals(name) && !"<clinit>".equals(name)) {
            boolean isAbstract = (access & ACC_ABSTRACT) == ACC_ABSTRACT;
            boolean isNative = (access & ACC_NATIVE) == ACC_NATIVE;
            if (isAbstract && isNative) {
                return mv;
            }
            return new MethodInvokeAfterWrapper(api, mv);
        }
        return mv;
    }

    private static class MethodInvokeAfterWrapper extends MethodVisitor {

        public MethodInvokeAfterWrapper(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitInsn(int opcode) {
            // 抛异常和正常返回时增强
            if (opcode == Opcodes.ATHROW || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                super.visitLdcInsn("Method Exit...");
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            super.visitInsn(opcode);
        }
    }
}
