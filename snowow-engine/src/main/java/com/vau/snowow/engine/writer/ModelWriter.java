package com.vau.snowow.engine.writer;

import com.google.gson.JsonObject;
import com.vau.snowow.engine.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ModelWriter extends BaseWriter<List<JsonObject>> {

    @Override
    public int write(List<JsonObject> obj, String packageName) throws IOException {
        if (obj.isEmpty()) {
            return 1;
        }
        int result = 1;
        // Format package path into file path
        String targetPath = Objects.requireNonNull(packageName.replace(".", "/"));

        File modelDir = new File(FileUtil.getApplicationPath() + "/java/" + targetPath + "/models");
        if (modelDir.mkdirs()) {
            log.info("Model directory was not found, controller directory is created, the path is {}", modelDir.getPath());
        }

        lock.lock();
        try {
            for (final File file : Objects.requireNonNull(modelDir.listFiles())) {
                if (!file.getPath().endsWith(".json")) {
                    continue;
                }
                String className = file.getName();
                File modelFile = new File(modelDir.getPath() + "/" + className + ".java");
                writeClass(modelFile, className, packageName);
            }
        } catch (Exception e) {
            log.error(e.toString());
            result = -1;
        } finally {
            lock.unlock();
        }

        return result;
    }

    private void writeClass(File modelFile, String name, String packageName) throws IOException {
        ClassWriter.Annotation[] annotations = ClassWriter.Annotation.dataModelCollections();
        ClassWriter classWriter = new ClassWriter(modelFile, name, annotations, true);
        classWriter.wrap(packageName, classComponents -> {

        });
    }
}
