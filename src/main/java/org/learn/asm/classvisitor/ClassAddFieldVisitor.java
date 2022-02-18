package org.learn.asm.classvisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

/**
 * 增加字段
 *
 * @author : zhangzy
 * @date : 2022/2/18
 */
public class ClassAddFieldVisitor extends ClassVisitor {
    private final String fieldName;

    private final String fieldDesc;

    private final int fieldAccess;

    private boolean ifPresent;

    public ClassAddFieldVisitor(int api, ClassVisitor classVisitor, String fieldName, String fieldDec, int fieldAccess) {
        super(api, classVisitor);
        this.fieldName = fieldName;
        this.fieldDesc = fieldDec;
        this.fieldAccess = fieldAccess;
        this.ifPresent = false;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (name.equals(fieldName) && descriptor.equals(fieldDesc) && access == this.fieldAccess) {
            ifPresent = true;
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public void visitEnd() {
        if (!ifPresent) {
            FieldVisitor fv = cv.visitField(fieldAccess, fieldName, fieldDesc, null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        super.visitEnd();
    }
}
