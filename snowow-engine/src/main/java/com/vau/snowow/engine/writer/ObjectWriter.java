package com.vau.snowow.engine.writer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vau.snowow.engine.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This writer is for writing new object creation code
 *
 * @author liuquan
 */
@Slf4j
public final class ObjectWriter {
    public ObjectWriter() {
    }

    public String buildMap(JsonElement element) throws IOException {
        return constructHashMap(element, true);
    }

    public String constructHashMap(Map<String, Object> value) {
        if (Objects.isNull(value)) {
            return "new HashMap<>()";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(" new HashMap<>() {{ ");
        Set<Map.Entry<String, Object>> entrySet = value.entrySet();
        entrySet.forEach(entry -> {
            builder.append("put(\"" + entry.getKey() + "\", ");
            Object entryValue = entry.getValue();
            if (entryValue instanceof String) {
                String entryStr = (String) entryValue;
                if (StringUtil.isFormValue(entryStr)) {
                    builder.append(StringUtil.extractFieldValue(entryValue.toString()));
                } else {
                    builder.append("\"" + entryStr + "\"");
                }
            } else {
                builder.append(entryValue);
            }
            builder.append(");");
        });
        builder.append(" }}");
        return builder.toString();
    }

    /**
     * return new HashMap<>(){
     * {
     * put("name", "ss");
     * put("school", new HashMap<>(){
     * {
     * put("name", "new york university");
     * }
     * });
     * }
     * };
     *
     * @param jsonElement
     * @param isForReturn
     * @throws IOException
     */
    public String constructHashMap(JsonElement jsonElement, Boolean isForReturn) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (isForReturn) {
            builder.append("return ");
        }

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
            builder.append("new HashMap<>(){{");
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                builder.append(getMapProperties(entry, jsonObject));
            }
            builder.append("}}");
        } else if (jsonElement.isJsonPrimitive()) {
            builder.append(extractFieldFromJson(jsonElement));
        } else if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            builder.append(getArrayProperties(array));
        } else if (jsonElement.isJsonNull()) {
            builder.append("null");
        }
        if (isForReturn) {
            builder.append(";");
        }

        return builder.toString();
    }

    public static Object extractFieldFromJson(JsonElement jsonElement) {
        if (!jsonElement.isJsonPrimitive()) {
            throw new IllegalStateException("Must be a primitive type to extract field");
        }

        String fieldName = jsonElement.getAsString();
        if (!StringUtil.isFormValue(fieldName)) {
            return jsonElement;
        }

        return StringUtil.extractFieldValue(fieldName.replace("\"", ""));
    }

    private String getArrayProperties(JsonArray array) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (array.isEmpty()) {
            return "new Object[]{}";
        }
        builder.append("new Object[]{");
        for (JsonElement e : array) {
            builder.append(constructHashMap(e, false)).append(",");
        }
        builder.append("}");
        return builder.toString();
    }

    private String getMapProperties(Map.Entry<String, JsonElement> entrySet, JsonObject jsonObject) throws IOException {
        StringBuilder builder = new StringBuilder();
        JsonElement value = jsonObject.get(entrySet.getKey());
        builder.append("put(\"").append(entrySet.getKey()).append("\"").append(",");
        builder.append(constructHashMap(value, false));
        builder.append(");");
        return builder.toString();
    }
}
