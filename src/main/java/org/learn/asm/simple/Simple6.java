package org.learn.asm.simple;

import org.learn.asm.utils.FileUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

/**
 * description
 *
 * @author : zhangzy
 * @date : 2022/2/15
 */
public class Simple6 {
    /**
     * 生成如下类
     * public class SimpleDemo5 {
     *     public void test() {
     *          for (int i = 0; i < 10; i++) {
     *              System.out.println(i);
     *          }
     *         }
     *     }
     * }
     */
    private static byte[] generateClazzByte() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_8, ACC_PUBLIC, "org/learn/asm/simple/SimpleDemo6", null, "java/lang/Object", null);
        //设置构造方法 <init>
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            //方法指令开始
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            //方法指令结束 operand stack; local variables
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        //设置test方法
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "test", "()V", null, null);
            Label conditionLabel = new Label();
            Label returnLabel = new Label();

            mv.visitCode();
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ISTORE, 1);

            mv.visitLabel(conditionLabel);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitIntInsn(BIPUSH, 10);
            mv.visitJumpInsn(IF_ICMPGE, returnLabel);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
            mv.visitIincInsn(1, 1);
            mv.visitJumpInsn(GOTO, conditionLabel);

            mv.visitLabel(returnLabel);
            mv.visitInsn(RETURN);
            //方法指令结束 operand stack; local variables
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        byte[] bytes = generateClazzByte();
        String path = FileUtil.getAbsolutePath("/org/learn/asm/simple/SimpleDemo6.class");
        FileUtil.writeByteToFile(bytes, path);

        Class<?> clazz = Class.forName("org.learn.asm.simple.SimpleDemo6");
        System.out.println(clazz);
        Object obj = clazz.getDeclaredConstructor().newInstance();
        Method test = clazz.getDeclaredMethod("test");
        test.invoke(obj);
    }

}
