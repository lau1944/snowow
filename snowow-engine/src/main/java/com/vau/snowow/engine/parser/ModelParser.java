package com.vau.snowow.engine.parser;

import com.google.gson.JsonObject;
import com.vau.snowow.engine.utils.Deserializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Convert model into POJO classes
 */
public class ModelParser extends BaseParser<List<JsonObject>> {

    @Override
    public List<JsonObject> parse(String path) throws IllegalArgumentException, FileNotFoundException {
        File modelDir = new File(path);
        if (!modelDir.exists()) {
            return null;
        }
        if (!modelDir.isDirectory()) {
            throw new IllegalStateException("File structure is incorrect, please follow the instructions to create files");
        }
        List<JsonObject> modelList = new ArrayList<>();
        for (final File file : Objects.requireNonNull(modelDir.listFiles())) {
            if (!file.getPath().endsWith(".json")) {
                continue;
            }
            modelList.add(Deserializer.deserialize(file));
        }
        return modelList;
    }
}
