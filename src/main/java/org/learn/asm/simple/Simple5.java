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
 * @date : 2022/2/15
 */
public class Simple5 {
    /**
     * 生成如下类
     * public class SimpleDemo5 {
     *     public void test(int var) {
     *         switch (var) {
     *             case 1:
     *                 System.out.println("case1");
     *                 break;
     *             case 2:
     *                 System.out.println("case2");
     *                 break;
     *             default:
     *                 System.out.println("default");
     *         }
     *     }
     * }
     */
    private static byte[] generateClazzByte() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_8, ACC_PUBLIC, "org/learn/asm/simple/SimpleDemo5", null, "java/lang/Object", null);
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
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "test", "(I)V", null, null);
            Label caseLabel1 = new Label();
            Label caseLabel2 = new Label();
            Label defaultLabel = new Label();
            Label returnLabel = new Label();

            mv.visitCode();
            mv.visitVarInsn(ILOAD, 1);
            mv.visitTableSwitchInsn(1, 2, defaultLabel, caseLabel1, caseLabel2);

            mv.visitLabel(caseLabel1);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("case1");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitJumpInsn(GOTO, returnLabel);

            mv.visitLabel(caseLabel2);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("case2");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitJumpInsn(GOTO, returnLabel);

            mv.visitLabel(defaultLabel);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("default");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            // return
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
        String path = FileUtil.getAbsolutePath("/org/learn/asm/simple/SimpleDemo5.class");
        FileUtil.writeByteToFile(bytes, path);

        Class<?> clazz = Class.forName("org.learn.asm.simple.SimpleDemo5");
        System.out.println(clazz);
        Object obj = clazz.getDeclaredConstructor().newInstance();
        Method test = clazz.getDeclaredMethod("test", int.class);
        test.invoke(obj, 1);
    }

}
