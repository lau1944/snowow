package com.vau.snowow.engine.writer;

import com.vau.snowow.engine.core.SnowContext;
import com.vau.snowow.engine.models.Constant;
import com.vau.snowow.engine.models.Field;
import com.vau.snowow.engine.models.Model;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
    public int write(List<Model> models, String targetPath, String packageName) {
        if (models.isEmpty()) {
            return 1;
        }
        int result = 1;

        File modelDir = new File(targetPath + "/models");
        if (!modelDir.exists()) {
            modelDir.mkdirs();
        }

        lock.lock();
        try {
            for (Model model : models) {
                String className = model.getName();
                File modelFile = new File(modelDir.getPath() + "/" + className + ".java");
                ClassWriter.Annotation[] annotations = ClassWriter.Annotation.dataModelCollections();
                classWriter = new ClassWriter(modelFile, className, Arrays.asList(annotations), true);
                writeClass(model.getFields(), packageName);
                classWriter.close();
                // Add  model to container
                SnowContext.addModel(className, model);
            }
        } catch (Exception e) {
            log.error(e.toString());
            result = -1;
        } finally {
            lock.unlock();
        }

        return result;
    }

    private void writeClass(Field[] fields, String packageName) throws IOException {
        List<String> dependencies = List.of("lombok.*");
        classWriter.wrap(packageName + ".models", classComponents -> {
            for (final Field field : fields) {
                String name = Objects.requireNonNull(field.getName());
                String type = Objects.requireNonNull(field.getType());
                List<ClassWriter.Annotation> annotations = new ArrayList<>();
                if (!field.getNullable()) {
                    annotations.add(new ClassWriter.Annotation("NonNull"));
                }
                ClassWriter.Field fieldWriter = new ClassWriter.Field(type, name, false, annotations, null);
                classComponents.add(fieldWriter);
            }
        }, dependencies);
    }

    public static BaseWriter<List<Model>> newWriter() {
        return new ModelWriter();
    }
}
