package org.learn.asm.transformation;

import org.learn.asm.classvisitor.ClassVersionChangeClassVisitor;
import org.learn.asm.utils.FileUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM8;

/**
 * 使用ASM框架修改class
 *
 * @author : zhangzy
 * @date : 2022/2/16
 */
public class Transformation1 {

    public static void main(String[] args) {
        String relativePath = "org/learn/asm/common/User.class";
        String path = FileUtil.getAbsolutePath(relativePath);
        byte[] bytes = FileUtil.readByteFromFile(path);
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassVisitor classVisitor = new ClassVersionChangeClassVisitor(ASM8, cw);

        cr.accept(classVisitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        byte[] afterTransformBytes = cw.toByteArray();
        FileUtil.writeByteToFile(afterTransformBytes, path);
    }
}
