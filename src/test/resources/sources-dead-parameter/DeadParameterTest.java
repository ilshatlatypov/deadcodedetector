package com.aurea.deadcode;

public class DeadParameterTest {

    public int method(int usedParameter, int notUsedParameter) {
        return usedParameter * 2;
    }
}
