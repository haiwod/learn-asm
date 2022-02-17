package org.learn.asm.transformation;

import org.learn.asm.classvisitor.ClassCloneableVisitor;
import org.learn.asm.classvisitor.ClassVersionChangeClassVisitor;
import org.learn.asm.common.Member;
import org.learn.asm.utils.FileUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM8;

/**
 * 使用ASM框架修改class
 *
 * @author : zhangzy
 * @date : 2022/2/17
 */
public class Transformation2 {
    /**
     * Member实现Cloneable接口
     */
    public static void main(String[] args) throws CloneNotSupportedException {
        String relativePath = "org/learn/asm/common/Member.class";
        String path = FileUtil.getAbsolutePath(relativePath);
        byte[] bytes = FileUtil.readByteFromFile(path);

        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassVisitor classVersionCV = new ClassVersionChangeClassVisitor(ASM8, cw);
        ClassVisitor classCloneableCV = new ClassCloneableVisitor(ASM8, classVersionCV);

        cr.accept(classCloneableCV, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        byte[] afterTransformBytes = cw.toByteArray();
        FileUtil.writeByteToFile(afterTransformBytes,path);

        Member member = new Member("undefine", 100);
        Object clone = member.clone();
        System.out.println(member);
        System.out.println(clone);
    }
}
