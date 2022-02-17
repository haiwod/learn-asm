package org.learn.asm.classvisitor;

import org.objectweb.asm.ClassVisitor;

/**
 * 修改类实现Cloneable接口
 *
 * @author : zhangzy
 * @date : 2022/2/17
 */
public class ClassCloneableVisitor extends ClassVisitor {
    public ClassCloneableVisitor(int api) {
        super(api);
    }

    public ClassCloneableVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, new String[]{"java/lang/Cloneable"});
    }
}
