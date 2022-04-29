package com.vau.snowow.engine.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.regex.Pattern;

/**
 * @author liuquan
 */
public final class StringUtil {

    private static final String VALUE_FORM_PATTERN = "^(@\\{)(.*)(\\})$";

    public static boolean isPrimitiveType(String type) {
        return "Int".equals(type) || "String".equals(type) || "Byte".equals(type) || "Boolean".equals(type)
                || "Long".equals(type) || "Double".equals(type) || "Float".equals(type);
    }

    /**
     * Check if the value string is in form @{xxx}
     * @param value
     * @return
     */
    public static boolean isFormValue(String value) {
        Pattern pattern = Pattern.compile(VALUE_FORM_PATTERN);
        return pattern.matcher(value).find();
    }

    /**
     * sample : @{params.name} -> params.name
     * @param fieldName
     * @return
     */
    public static String extractFieldValue(String fieldName) {
        if (!StringUtil.isFormValue(fieldName)) {
            return fieldName;
        }

        StringBuilder builder = new StringBuilder();
        String value = fieldName.substring(2, fieldName.length() - 1);
        String[] args = value.split("\\.");
        builder.append(args[0] + ".get(\"" + args[1] + "\") ");
        return builder.toString();
    }
}
