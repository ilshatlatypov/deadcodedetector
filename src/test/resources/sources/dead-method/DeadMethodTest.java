package com.aurea.deadcode;

public class DeadMethodTest {
    private void notUsedPrivateMethod() {
        usedPrivateMethod();
    }

    private void usedPrivateMethod() {
        System.out.println("From used method");
    }
}
