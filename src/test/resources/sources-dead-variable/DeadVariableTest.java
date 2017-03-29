package com.aurea.deadcode;

public class DeadVariableTest {

    private int notUsedPrivateVariable;

    private int usedPrivateVariable;

    public int getVar() {
        return usedPrivateVariable;
    }
}
