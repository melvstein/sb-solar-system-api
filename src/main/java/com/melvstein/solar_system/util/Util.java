package com.melvstein.solar_system.util;

public class Util {
    public static String currentMethod() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // [0] is getStackTrace, [1] is currentMethod, [2] is the caller
        if (stackTrace.length > 2) {
            StackTraceElement element = stackTrace[2];
            return element.getClassName() + "::" + element.getMethodName();
        }

        return "UnknownMethod";
    }
}
