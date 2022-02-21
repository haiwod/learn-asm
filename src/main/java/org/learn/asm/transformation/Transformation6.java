package org.learn.asm.transformation;

import org.learn.asm.classvisitor.MethodTimerVisitor;
import org.learn.asm.utils.FileUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.ASM8;

/**
 * 使用ASM框架修改class
 *
 * @author : zhangzy
 * @date : 2022/2/21
 */
public class Transformation6 {
    public static void main(String[] args) throws Exception {
        String relativePath = "org/learn/asm/common/TimerMethod.class";
        String path = FileUtil.getAbsolutePath(relativePath);
        byte[] bytes = FileUtil.readByteFromFile(path);
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassVisitor methodTimerVisitor = new MethodTimerVisitor(ASM8, cw);
        cr.accept(methodTimerVisitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        byte[] afterTransformBytes = cw.toByteArray();
        FileUtil.writeByteToFile(afterTransformBytes, path);

        Class<?> clazz = Class.forName("org.learn.asm.common.TimerMethod");
        Object obj = clazz.getDeclaredConstructor().newInstance();
        Field sub_timer = clazz.getField("sub_timer");
        Field add_timer = clazz.getField("add_timer");
        Method sub = clazz.getDeclaredMethod("sub", int.class, int.class);
        Method add = clazz.getDeclaredMethod("add", int.class, int.class);

        for (int i = 0; i < 10; i++) {
            sub.invoke(obj, 100, 40);
            System.out.println(sub_timer.get(obj));
            add.invoke(obj, 20, 3);
            System.out.println(add_timer.get(obj));
        }
    }
}
