package org.learn.asm.common;

/**
 * description
 *
 * @author : zhangzy
 * @date : 2022/2/17
 */
public class Member {
    private String name;
    private int no;

    public Member(String name, int no) {
        this.name = name;
        this.no = no;
        System.out.println("user: name= " + name + "; no= " + no);
    }

    public Member(){}

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
