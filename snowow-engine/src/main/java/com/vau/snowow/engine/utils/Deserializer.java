package com.vau.snowow.engine.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Deserialize json file into target object
 *
 * @author liuquan
 */
public class Deserializer {

    static final Gson gson = new Gson();

    public static final <T> T deserializeTo(File targetFile, Class<T> tClass) throws FileNotFoundException {
        JsonObject jsonObject = (JsonObject) JsonParser.parseReader(new FileReader(targetFile));
        T obj = gson.fromJson(jsonObject, tClass);
        return obj;
    }

    public static final JsonObject deserialize(File targetFile) throws FileNotFoundException {
        return (JsonObject) JsonParser.parseReader(new FileReader(targetFile));
    }
}
