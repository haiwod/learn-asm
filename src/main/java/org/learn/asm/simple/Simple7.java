package org.learn.asm.simple;

import org.learn.asm.utils.FileUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

/**
 * 使用ASM框架生成class文件
 *
 * @author : zhangzy
 * @date : 2022/2/16
 */
public class Simple7 {
    /**
     * 生成如下类
     * public class SimpleDemo7 {
     *     public void test() {
     *          try {
     *             System.out.println("start...");
     *             Thread.sleep(1000);
     *             System.out.println("end...");
     *         }catch (InterruptedException e) {
     *             e.printStackTrace();
     *         }
     *     }
     * }
     */
    private static byte[] generateClazzByte() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_8, ACC_PUBLIC, "org/learn/asm/simple/SimpleDemo7", null, "java/lang/Object", null);
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
            Label startLabel = new Label();
            Label endLabel = new Label();
            Label handlerLabel = new Label();
            Label returnLabel = new Label();

            mv.visitCode();
            mv.visitTryCatchBlock(startLabel, endLabel, handlerLabel, "java/lang/InterruptedException");

            mv.visitLabel(startLabel);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("start...");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitLdcInsn(1000L);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "sleep", "(J)V", false);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("end...");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            mv.visitLabel(endLabel);
            mv.visitJumpInsn(GOTO, returnLabel);

            mv.visitLabel(handlerLabel);
            mv.visitVarInsn(ASTORE, 1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/InterruptedException", "printStackTrace", "()V", false);

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
        String path = FileUtil.getAbsolutePath("/org/learn/asm/simple/SimpleDemo7.class");
        FileUtil.writeByteToFile(bytes, path);

        Class<?> clazz = Class.forName("org.learn.asm.simple.SimpleDemo7");
        System.out.println(clazz);
        Object obj = clazz.getDeclaredConstructor().newInstance();
        Method test = clazz.getDeclaredMethod("test");
        test.invoke(obj);
    }

}
