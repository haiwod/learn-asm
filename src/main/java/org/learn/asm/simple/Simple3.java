package org.learn.asm.simple;

import org.learn.asm.utils.FileUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

/**
 * 使用ASM框架生成class文件
 *
 * @author : zhangzy
 * @date : 2022/2/14
 */
public class Simple3 {
    /**
     * 生成如下类
     * public class SimpleDemo3 {
     * <p>
     * public SimpleDemo3(){
     * super();
     * }
     * <p>
     * static {
     * System.out.println("class initialization method");
     * }
     * <p>
     * public void test(){
     * User user =new User("undefine",1);
     * }
     * <p>
     * public void compare(int a, int b){
     * int max = Math.max(a,b);
     * System.out.println(max);
     * }
     * }
     */
    public static byte[] generateClazzByte() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_8, ACC_PUBLIC, "org/learn/asm/simple/SimpleDemo3", null, "java/lang/Object", null);
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
        //设置类初始化 <clinit>
        {
            MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            //方法指令开始
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("class initialization method");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitInsn(RETURN);
            //方法指令结束 operand stack; local variables
            mv.visitMaxs(2, 0);
            mv.visitEnd();
        }
        //设置test方法
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "test", "()V", null, null);
            //方法指令开始
            mv.visitCode();
            //创建对象指令 new dup(copy ref) invokespecial
            mv.visitTypeInsn(NEW, "org/learn/asm/common/User");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("undefine");
            mv.visitIntInsn(BIPUSH, 8);
            mv.visitMethodInsn(INVOKESPECIAL, "org/learn/asm/common/User", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitVarInsn(ASTORE, 1);
            mv.visitInsn(RETURN);
            //方法指令结束 operand stack; local variables
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }
        //设置compare方法
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "compare", "(II)V", null, null);
            //方法指令开始
            mv.visitCode();
            //Math.max(a,b) static invoke
            mv.visitVarInsn(ILOAD, 1);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
            mv.visitVarInsn(ISTORE, 3);
            //System.out.println(max) non-static invoke
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(ILOAD, 3);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);

            mv.visitInsn(RETURN);
            //方法指令结束 operand stack; local variables
            mv.visitMaxs(2, 4);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        byte[] bytes = generateClazzByte();
        String path = FileUtil.getAbsolutePath("/org/learn/asm/simple/SimpleDemo3.class");
        FileUtil.writeByteToFile(bytes, path);

        Class<?> clazz = Class.forName("org.learn.asm.simple.SimpleDemo3");
        System.out.println(clazz);
        //invoke test
        Object obj = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getDeclaredMethod("test");
        method.invoke(obj);
        //invoke compare
        Method compare = clazz.getDeclaredMethod("compare", int.class, int.class);
        compare.invoke(obj, 2, 1);

    }
}
