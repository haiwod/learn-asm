package org.learn.asm.common;

/**
 * description
 *
 * @author : zhangzy
 * @date : 2022/2/14
 */
public class User {
    private String name;
    private int no;

    public User(String name, int no) {
        this.name = name;
        this.no = no;
        System.out.println("user: name= " + name + "; no= " + no);
    }

    public User(){}
}
