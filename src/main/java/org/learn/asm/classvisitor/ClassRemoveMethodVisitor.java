package org.learn.asm.classvisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * 删除方法
 *
 * @author : zhangzy
 * @date : 2022/2/18
 */
public class ClassRemoveMethodVisitor extends ClassVisitor {

    private String methodName;

    private String methodDesc;

    public ClassRemoveMethodVisitor(int api, ClassVisitor classVisitor, String methodName, String methodDesc) {
        super(api, classVisitor);
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (methodName.equals(name) && methodDesc.equals(descriptor)) {
            return null;
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
