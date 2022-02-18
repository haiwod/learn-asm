package org.learn.asm.transformation;

import org.learn.asm.classvisitor.ClassAddMethodVisitor;
import org.learn.asm.classvisitor.ClassRemoveMethodVisitor;
import org.learn.asm.utils.FileUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

/**
 * 使用ASM框架修改class
 *
 * @author : zhangzy
 * @date : 2022/2/18
 */
public class Transformation4 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String relativePath = "org/learn/asm/common/MethodObj.class";
        String path = FileUtil.getAbsolutePath(relativePath);
        byte[] bytes = FileUtil.readByteFromFile(path);
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        //添加mul方法
        ClassVisitor classAddMethodVisitor = new ClassAddMethodVisitor(ASM8, cw, ACC_PUBLIC, "mul", "(II)I", null, null) {
            @Override
            protected void generatorMethodCode(MethodVisitor mv) {
                //class method code instructions
                mv.visitCode();
                mv.visitVarInsn(ILOAD, 1);
                mv.visitVarInsn(ILOAD, 2);
                mv.visitInsn(IMUL);
                mv.visitInsn(IRETURN);
                mv.visitMaxs(2, 3);
                mv.visitEnd();
            }
        };
        //删除add方法
        ClassVisitor classRemoveMethodVisitor = new ClassRemoveMethodVisitor(ASM8, classAddMethodVisitor, "add", "(II)I");

        cr.accept(classRemoveMethodVisitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        byte[] afterTransformBytes = cw.toByteArray();
        FileUtil.writeByteToFile(afterTransformBytes, path);

        Class<?> clazz = Class.forName("org.learn.asm.common.MethodObj");
        Object obj = clazz.getDeclaredConstructor().newInstance();

        Method method = clazz.getDeclaredMethod("mul", int.class, int.class);
        System.out.println(method.invoke(obj, 3, 4));
    }
}
