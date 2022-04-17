package com.vau.snowow.engine.utils;

public final class StringUtil {
    public static Boolean isPrimitiveType(String type) {
        return "Int".equals(type) || "String".equals(type) || "Byte".equals(type) || "Boolean".equals(type)
                || "Long".equals(type) || "Double".equals(type) || "Float".equals(type);
    }
}
