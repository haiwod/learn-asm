package org.learn.asm.simple;

import org.learn.asm.utils.FileUtil;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.*;

/**
 * 使用ASM框架生成class文件
 *
 * @author : zhangzy
 * @date : 2022/02/13
 */
public class Simple1 {

    /**
     * 生成如下接口
     * public interface SimpleDemo1 {
     *
     * }
     */
    public static byte[] generateClazzByte() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_8, ACC_PUBLIC | ACC_ABSTRACT | ACC_INTERFACE, "org/learn/asm/simple/SimpleDemo1", null, "java/lang/Object", null);
        cw.visitEnd();
        return cw.toByteArray();
    }

    public static void main(String[] args) throws ClassNotFoundException {
        byte[] bytes = generateClazzByte();
        String path = FileUtil.getAbsolutePath("/org/learn/asm/simple/SimpleDemo1.class");
        FileUtil.writeByteToFile(bytes, path);

        Class<?> clazz = Class.forName("org.learn.asm.simple.SimpleDemo1");
        System.out.println(clazz);
    }
}
