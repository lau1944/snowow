package com.vau.snowow.engine.writer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        String content = constructHashMap(element, true);
        writer.write(content);
    }

    /**
     * return new HashMap<>(){
     *             {
     *                 put("name", "ss");
     *                 put("school", new HashMap<>(){
     *                     {
     *                         put("name", "new york university");
     *                     }
     *                 });
     *             }
     *         };
     * @param jsonElement
     * @param isForReturn
     * @throws IOException
     */
    private String constructHashMap(JsonElement jsonElement, Boolean isForReturn) throws IOException {
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
        }

        else if (jsonElement.isJsonPrimitive()) {
            builder.append(jsonElement);
        }

        else if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            builder.append(getArrayProperties(array));
        }

        else if (jsonElement.isJsonNull()) {
            builder.append("null");
        }
        if (isForReturn) {
            builder.append(";");
        }

        return builder.toString();
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
        builder.append("put(\"").append(entrySet.getKey()).append("\"").append(",");
        JsonElement value = jsonObject.get(entrySet.getKey());
        builder.append(constructHashMap(value, false));
        builder.append(");");
        return builder.toString();
    }
}
