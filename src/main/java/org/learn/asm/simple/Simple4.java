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
 * @date : 2022/2/14
 */
public class Simple4 {
    /**
     * 生成如下类
     * public interface SimpleDemo4 {
     * <p>
     * public void test(boolean flag){
     * <p>
     * if (flag) {
     * System.out.println("value is true");
     * }else {
     * System.out.println("value is false");
     * }
     * <p>
     * }
     * }
     */
    private static byte[] generateClazzByte() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_8, ACC_PUBLIC, "org/learn/asm/simple/SimpleDemo4", null, "java/lang/Object", null);
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
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "test", "(Z)V", null, null);
            Label elseLabel = new Label();
            Label returnLabel = new Label();

            //方法指令开始
            mv.visitCode();
            mv.visitVarInsn(ILOAD, 1);
            //if ifeq==0 跳转 ; ifne!=0 跳转
            mv.visitJumpInsn(IFEQ, elseLabel);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("value is true");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitJumpInsn(GOTO, returnLabel);

            mv.visitLabel(elseLabel);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("value is false");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            mv.visitLabel(returnLabel);
            mv.visitInsn(RETURN);
            //方法指令结束 operand stack; local variables
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }

        cw.visitEnd();
        return cw.toByteArray();
    }


    public static void main(String[] args) throws Exception {
        byte[] bytes = generateClazzByte();
        String path = FileUtil.getAbsolutePath("/org/learn/asm/simple/SimpleDemo4.class");
        FileUtil.writeByteToFile(bytes, path);

        Class<?> clazz = Class.forName("org.learn.asm.simple.SimpleDemo4");
        System.out.println(clazz);
        Object obj = clazz.getDeclaredConstructor().newInstance();
        Method test = clazz.getDeclaredMethod("test", boolean.class);
        test.invoke(obj, true);
    }

}
