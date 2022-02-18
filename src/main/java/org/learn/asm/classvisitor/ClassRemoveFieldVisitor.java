package org.learn.asm.classvisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

/**
 * 删除字段
 *
 * @author : zhangzy
 * @date : 2022/2/18
 */
public class ClassRemoveFieldVisitor extends ClassVisitor {

    private final String fieldName;

    private final String fieldDec;

    public ClassRemoveFieldVisitor(int api, ClassVisitor classVisitor, String fieldName, String fieldDec) {
        super(api, classVisitor);
        this.fieldName = fieldName;
        this.fieldDec = fieldDec;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (name.equals(fieldName) && descriptor.equals(fieldDec)) {
            return null;
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

}
