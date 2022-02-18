package org.learn.asm.classvisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * 增加方法
 *
 * @author : zhangzy
 * @date : 2022/2/18
 */
public abstract class ClassAddMethodVisitor extends ClassVisitor {

    private final int methodAccess;

    private final String methodName;

    private final String methodDesc;

    private final String methodSig;

    private final String[] methodExs;

    private boolean ifPresent;

    public ClassAddMethodVisitor(int api, ClassVisitor classVisitor,
                                 int methodAccess, String methodName,
                                 String methodDesc, String methodSig,
                                 String[] methodExs) {
        super(api, classVisitor);
        this.methodAccess = methodAccess;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.methodSig = methodSig;
        this.methodExs = methodExs;
        this.ifPresent = false;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (methodName.equals(name) && methodDesc.equals(descriptor)) {
            ifPresent = true;
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        if (!ifPresent) {
            MethodVisitor mv = cv.visitMethod(methodAccess, methodName, methodDesc, methodSig, methodExs);
            if (mv != null) {
                generatorMethodCode(mv);
            }
        }
        super.visitEnd();
    }

    /**
     * 生成类文件方法的code内容
     *
     * @param mv
     */
    protected abstract void generatorMethodCode(MethodVisitor mv);
}
