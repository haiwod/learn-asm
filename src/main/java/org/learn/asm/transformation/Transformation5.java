package org.learn.asm.transformation;

import org.learn.asm.classvisitor.MethodInvokeAfterVisitor;
import org.learn.asm.classvisitor.MethodInvokeBeforeVisitor;
import org.learn.asm.common.MethodObj;
import org.learn.asm.utils.FileUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM8;

/**
 * 使用ASM框架修改class
 *
 * @author : zhangzy
 * @date : 2022/2/21
 */
public class Transformation5 {
    public static void main(String[] args) throws ClassNotFoundException {
        String relativePath = "org/learn/asm/common/MethodObj.class";
        String path = FileUtil.getAbsolutePath(relativePath);
        byte[] bytes = FileUtil.readByteFromFile(path);
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        //invoke before
        ClassVisitor methodInvokeBeforeVisitor = new MethodInvokeBeforeVisitor(ASM8, cw);
        //invoke after
        ClassVisitor methodInvokeAfterVisitor = new MethodInvokeAfterVisitor(ASM8, methodInvokeBeforeVisitor);

        cr.accept(methodInvokeAfterVisitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        byte[] afterTransformBytes = cw.toByteArray();
        FileUtil.writeByteToFile(afterTransformBytes, path);

        MethodObj methodObj = new MethodObj();
        int sub = methodObj.sub(5, 3);
        System.out.println("method over... result: " + sub);
    }
}
