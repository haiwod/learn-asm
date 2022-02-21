package org.learn.asm.classvisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

/**
 * 统计方法耗时(类新增methodName_timer变量来统计)
 *
 * @author : zhangzy
 * @date : 2022/2/21
 */
public class MethodTimerVisitor extends ClassVisitor {

    private boolean isInterface;
    private String className;

    public MethodTimerVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name;
        isInterface = (access & ACC_INTERFACE) == ACC_INTERFACE;
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
            //新增字段 public static long methodName_timer
            String fieldName = name + "_timer";
            FieldVisitor fv = super.visitField(ACC_PUBLIC | ACC_STATIC, fieldName, "J", null, null);
            if (fv != null) {
                fv.visitEnd();
            }

            return new MethodTimerWrapper(api, mv, className, fieldName);
        }
        return mv;
    }

    private static class MethodTimerWrapper extends MethodVisitor {
        private final String className;
        private final String fieldName;

        public MethodTimerWrapper(int api, MethodVisitor mv, String className, String fieldName) {
            super(api, mv);
            this.className = className;
            this.fieldName = fieldName;
        }

        @Override
        public void visitCode() {
            super.visitFieldInsn(GETSTATIC, className, fieldName, "J");
            super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            super.visitInsn(LSUB);
            super.visitFieldInsn(PUTSTATIC, className, fieldName, "J");

            super.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            // 抛异常和正常返回时增强
            if (opcode == Opcodes.ATHROW || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                super.visitFieldInsn(GETSTATIC, className, fieldName, "J");
                super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                super.visitInsn(LADD);
                super.visitFieldInsn(PUTSTATIC, className, fieldName, "J");
            }
            super.visitInsn(opcode);
        }
    }
}
