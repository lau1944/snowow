package com.vau.snowow.engine.parser;

import com.google.gson.JsonArray;
import com.vau.snowow.engine.models.Model;
import com.vau.snowow.engine.utils.Deserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Convert model into POJO classes
 * @author liuquan
 */
@Slf4j
public class ModelParser extends BaseParser<List<Model>> {

    @Override
    public List<Model> parse(String path) throws IllegalArgumentException, FileNotFoundException {
        File modelDir = new File(path + "/model");
        if (!modelDir.exists()) {
            return null;
        }
        if (!modelDir.isDirectory()) {
            throw new IllegalStateException("File structure is incorrect, please follow the instructions to create files");
        }
        List<Model> modelList = new ArrayList<>();
        for (final File file : Objects.requireNonNull(modelDir.listFiles())) {
            if (!file.getPath().endsWith(".json")) {
                continue;
            }
            modelList.add(Deserializer.deserializeTo(file, Model.class));
        }
        return modelList;
    }
}
