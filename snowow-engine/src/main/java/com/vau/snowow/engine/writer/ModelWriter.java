package com.vau.snowow.engine.writer;

import com.vau.snowow.engine.models.Field;
import com.vau.snowow.engine.models.Model;
import com.vau.snowow.engine.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Writer for writing POJO model
 *
 * @author liuquan
 */
@Slf4j
public class ModelWriter extends BaseWriter<List<Model>> {

    private ClassWriter classWriter;

    @Override
    public int write(List<Model> models, String packageName) throws IOException {
        if (models.isEmpty()) {
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
            for (Model model : models) {
                String className = model.getName();
                File modelFile = new File(modelDir.getPath() + "/" + className + ".java");
                ClassWriter.Annotation[] annotations = ClassWriter.Annotation.dataModelCollections();
                classWriter = new ClassWriter(modelFile, className, Arrays.asList(annotations), true);
                writeClass(classWriter, model.getFields(), packageName);
            }
        } catch (Exception e) {
            log.error(e.toString());
            result = -1;
        } finally {
            lock.unlock();
        }

        return result;
    }

    private void writeClass(ClassWriter writer, Field[] fields, String packageName) throws IOException {
        List<String> dependencies = List.of("lombok.*");
        writer.wrap(packageName + ".models", classComponents -> {
            for (final Field field: fields) {
                String name = Objects.requireNonNull(field.getName());
                String type = Objects.requireNonNull(field.getType());
                List<ClassWriter.Annotation> annotations = new ArrayList<>();
                if (field.getNullable() == 0) {
                    annotations.add(new ClassWriter.Annotation("NonNull"));
                }
                if (field.getDefaultValue() != null) {
                    annotations.add(new ClassWriter.Annotation("Builder.Default"));
                }
                ClassWriter.Field fieldWriter = new ClassWriter.Field(type, name, false, annotations, field.getDefaultValue());
                classComponents.add(fieldWriter);
            }
        }, dependencies);
    }

    public static BaseWriter<List<Model>> newWriter() {
        return new ModelWriter();
    }
}
