package org.learn.asm.classvisitor;

import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.V1_7;

/**
 * class修改成v1.7
 *
 * @author : zhangzy
 * @date : 2022/2/16
 */
public class ClassVersionChangeClassVisitor extends ClassVisitor {
    public ClassVersionChangeClassVisitor(int api) {
        super(api);
    }

    public ClassVersionChangeClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(V1_7, access, name, signature, superName, interfaces);
    }
}
