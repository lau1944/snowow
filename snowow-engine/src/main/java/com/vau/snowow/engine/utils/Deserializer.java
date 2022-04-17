package com.vau.snowow.engine.utils;

import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Deserialize json file into target object
 *
 * @author liuquan
 */
public final class Deserializer {

    static final Gson gson = new Gson();

    public static final <T> T deserializeTo(File targetFile, Class<T> tClass) throws FileNotFoundException {
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(targetFile));
        T obj = gson.fromJson(jsonElement, tClass);
        return obj;
    }

    public static final <T> T deserialize(File targetFile) throws FileNotFoundException {
        return (T) JsonParser.parseReader(new FileReader(targetFile));
    }
}
