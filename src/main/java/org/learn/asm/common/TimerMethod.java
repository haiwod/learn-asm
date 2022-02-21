package org.learn.asm.common;

/**
 * description
 *
 * @author : zhangzy
 * @date : 2022/2/21
 */
public class TimerMethod {
    public int add(int a, int b) {
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return a + b;
    }

    public int sub(int a, int b) {
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return a - b;
    }
}
