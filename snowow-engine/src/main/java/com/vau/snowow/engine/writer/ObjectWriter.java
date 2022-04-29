package com.vau.snowow.engine.writer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vau.snowow.engine.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * This writer is for writing new object creation code
 *
 * @author liuquan
 */
@Slf4j
public final class ObjectWriter {
    private FileWriter writer;

    public ObjectWriter(FileWriter writer) {
        this.writer = writer;
    }

    public void buildMap(JsonElement element) throws IOException {
        String content = constructHashMap(element, null,true);
        writer.write(content);
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
    private String constructHashMap(JsonElement jsonElement, String entryKey, Boolean isForReturn) throws IOException {
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
            builder.append(extractFieldFromJson(jsonElement, entryKey));
        } else if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            builder.append(getArrayProperties(array, entryKey));
        } else if (jsonElement.isJsonNull()) {
            builder.append("null");
        }
        if (isForReturn) {
            builder.append(";");
        }

        return builder.toString();
    }

    public static Object extractFieldFromJson(JsonElement jsonElement, String entryKey) {
        if (!jsonElement.isJsonPrimitive()) {
            throw new IllegalStateException("Must be a primitive type to extract field");
        }

        String fieldName = jsonElement.getAsString();
        if (!StringUtil.isFormValue(fieldName)) {
            return jsonElement;
        }

        return StringUtil.extractFieldValue(fieldName.replace("\"", ""));
    }

    private String getArrayProperties(JsonArray array, String entryKey) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (array.isEmpty()) {
            return "new Object[]{}";
        }
        builder.append("new Object[]{");
        for (JsonElement e : array) {
            builder.append(constructHashMap(e, entryKey, false)).append(",");
        }
        builder.append("}");
        return builder.toString();
    }

    private String getMapProperties(Map.Entry<String, JsonElement> entrySet, JsonObject jsonObject) throws IOException {
        StringBuilder builder = new StringBuilder();
        JsonElement value = jsonObject.get(entrySet.getKey());
        builder.append("put(\"").append(entrySet.getKey()).append("\"").append(",");
        builder.append(constructHashMap(value, entrySet.getKey(), false));
        builder.append(");");
        return builder.toString();
    }
}
