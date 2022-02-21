package org.learn.asm.classvisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

/**
 * 方法调用前增强
 *
 * @author : zhangzy
 * @date : 2022/2/21
 */
public class MethodInvokeBeforeVisitor extends ClassVisitor {
    public MethodInvokeBeforeVisitor(int api, ClassVisitor classVisitor) {
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
            return new MethodInvokeBeforeWrapper(api, mv);
        }
        return mv;
    }

    private static class MethodInvokeBeforeWrapper extends MethodVisitor {

        public MethodInvokeBeforeWrapper(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn("Method Enter...");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            super.visitCode();
        }
    }
}
