# learn-asm
ASM字节码框架，用于字节码的分析，生成，转换等方面

## 框架流程

**访问者模式 **

* `ClassReader`

```java
//核心方法 接收ClassVisitor，根据读取到class内容，执行对应的classvisitor方法
accpect(classvisitor,...);
```

* `ClassVisitor`

```java
//核心方法 ClassVisitor内部封装多种visitXxx()方法，按一定顺序调用
//注意到内部用链式调用，classvisitor可通过构造方法形成链表结构
visitXxx();
...
//生成MethodVisitor和FieldVisitor，也是用访问者模式
visitMethod();
visitField();
```

* `ClassWriter`

`ClassVisitor` 的实现类，主要用于class文件的形成和输出

与之类似的还有 ` MethodWriter` 和 ` FieldWriter`，用于方法和字段的形成

```java
//核心方法 将所有visit方法调用后的结果，class字节码转换成二进制字节
toByteArray();
```

