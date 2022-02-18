package org.learn.asm.transformation;

import org.learn.asm.classvisitor.ClassAddFieldVisitor;
import org.learn.asm.classvisitor.ClassRemoveFieldVisitor;
import org.learn.asm.utils.FileUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.reflect.Field;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ASM8;

/**
 * 使用ASM框架修改class
 *
 * @author : zhangzy
 * @date : 2022/2/18
 */
public class Transformation3 {
    public static void main(String[] args) throws ClassNotFoundException {
        String relativePath = "org/learn/asm/common/FieldObj.class";
        String path = FileUtil.getAbsolutePath(relativePath);
        byte[] bytes = FileUtil.readByteFromFile(path);
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        //新增字段 private Object obj
        ClassVisitor addFieldVisitor = new ClassAddFieldVisitor(ASM8, cw, "obj", "Ljava/lang/Object;", ACC_PRIVATE);
        //删除字段 info
        ClassVisitor removeFieldVisitor = new ClassRemoveFieldVisitor(ASM8, addFieldVisitor, "info", "Ljava/lang/String;");

        cr.accept(removeFieldVisitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        byte[] afterTransformBytes = cw.toByteArray();
        FileUtil.writeByteToFile(afterTransformBytes, path);

        Class<?> clazz = Class.forName("org.learn.asm.common.FieldObj");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }
    }
}
