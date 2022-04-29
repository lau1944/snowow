package com.vau.snowow.engine.writer;

import com.vau.snowow.engine.core.SnowContext;
import com.vau.snowow.engine.models.DataHolder;
import com.vau.snowow.engine.utils.StringUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Responsible for writing class file
 *
 * @author liuquan
 */
@Slf4j
public class ClassWriter {
    private File file;
    private String className;
    private List<Annotation> annotations;
    private Boolean isPublic;
    private FileWriter fileWriter;
    private List<ClassComponents> components = new ArrayList<>();

    public ClassWriter(File file, String className, List<Annotation> annotations, Boolean isPublic) {
        this.file = file;
        this.annotations = annotations;
        this.className = className;
        this.isPublic = isPublic;
    }

    public ClassWriter(File file, String className, Boolean isPublic) {
        this.file = file;
        this.annotations = new ArrayList<>();
        this.className = className;
        this.isPublic = isPublic;
    }

    public ClassWriter(String className, Boolean isPublic, List<Annotation> annotations, FileWriter fileWriter) {
        this.className = className;
        this.annotations = annotations;
        this.isPublic = isPublic;
        this.fileWriter = fileWriter;
    }

    /**
     * This method is used to write the class files
     */
    public void wrap(String packageName, Consumer<List<ClassComponents>> componentsAdd, List<String> dependencies) throws IOException {
        if (Objects.isNull(fileWriter)) {
            fileWriter = new FileWriter(file, false);
        }

        writeDependency(packageName, dependencies);

        if (Objects.nonNull(annotations)) {
            for (Annotation annotation : annotations) {
                fileWriter.write(annotation.toString());
            }
        }
        fileWriter.write(isPublic ? "public " : "");
        fileWriter.write("class " + className + " {");

        // Class components added
        componentsAdd.accept(components);
        for (final ClassComponents component : components) {
            if (component instanceof Field) {
                // Write fields into file
                fileWriter.write(buildField((Field) component, false));
            } else if (component instanceof Method) {
                // Write methods into files
                writeMethod((Method) component);
            }
        }

        fileWriter.write("}");
        //fileWriter.close();
    }

    public void close() throws IOException {
        Objects.requireNonNull(fileWriter).close();
    }

    private void writeDependency(String packageName, List<String> dependencies) throws IOException {
        // Write package file
        fileWriter.write("package " + packageName + ";\n");
        // write import dependencies
        for (String dependency : dependencies) {
            fileWriter.write("import " + dependency + ";\n");
        }
    }

    private void writeMethod(Method method) throws IOException {
        fileWriter.write("\t");
        List<Annotation> annotations = method.getAnnotations();
        if (Objects.nonNull(annotations)) {
            for (Annotation annotation : annotations) {
                fileWriter.write(annotation.toString());
            }
        }
        fileWriter.write(method.getIsPublic() ? "public " : "private ");
        String type = "void";
        if (Objects.nonNull(method.getResponse())) {
            type = method.getResponse().getType();
        }
        if (!StringUtil.isPrimitiveType(method.getResponse().getType())) {
            type = "Map<String, Object>";
        }
        fileWriter.write(type  + " ");
        fileWriter.write(method.getMethodName());
        fileWriter.write("(");
        fileWriter.write(buildParams(method.params));
        fileWriter.write(")");
        fileWriter.write("{ \n");
        fileWriter.write(method.getContent());
        writeResponse(method.getResponse());
        fileWriter.write("\n" + "}");
    }

    private String buildParams(List<Field> fields) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fields.size(); ++i) {
             stringBuilder.append(buildField(fields.get(i), true));
             if (i < fields.size() - 1) {
                 stringBuilder.append(", ");
             }
         }
        return stringBuilder.toString();
    }

    private void writeResponse(DataHolder holder) throws IOException {
        String type = holder.getType();
        if (StringUtil.isPrimitiveType(type)) {
            fileWriter.write("return " + holder.getValue() + ";");
        }
        if (!SnowContext.containsModel(holder.getType())) {
            throw new IllegalStateException("Specific type " + holder.getType() + " has not been initialized");
        }

        ObjectWriter objectWriter = new ObjectWriter(fileWriter);
        objectWriter.buildMap(holder.getValue());
    }

    private String buildField(Field field, boolean isFromParams) {
        StringBuilder builder = new StringBuilder();
        builder.append("\t");
        List<Annotation> annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            builder.append(annotation.toString());
        }
        if (!isFromParams) {
            builder.append(field.getIsPublic() ? "public " : "private ");
        }
        builder.append(field.getFieldType() + " " + field.getFieldName());
        // Check if default value exists, if exists, add to string (Default value should is bound with annotation @Builder.Default)
        Object defaultValue = field.getDefaultValue();
        if (Objects.nonNull(field.getDefaultValue())) {
            builder.append(" = ");
            if (defaultValue instanceof String) {
                builder.append("\"" + defaultValue + "\"");
            } else {
                builder.append(defaultValue.toString());
            }
        }
        if (!isFromParams) {
            builder.append(";");
        }
        return builder.toString();
    }

    abstract static class ClassComponents {
    }

    @Getter
    public static class Annotation {
        private String name;
        private Map<String, Object> properties;

        Annotation(String name) {
            this.name = name;
        }

        Annotation(String name, Map<String, Object> properties) {
            this.name = name;
            this.properties = properties;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            builder.append("@").append(name).append("(");
            if (Objects.nonNull(properties)) {
                Iterator<Map.Entry<String, Object>> entries = properties.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, Object> entry = entries.next();
                    builder.append(entry.getKey());
                    builder.append("=");
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        builder.append("\"");
                        builder.append(value);
                        builder.append("\"");
                    } else {
                        builder.append(entry.getValue());
                    }
                    if (entries.hasNext()) {
                        builder.append(", ");
                    }
                }
            }
            builder.append(")");
            builder.append(" ");
            return builder.toString();
        }

        public static Annotation[] dataModelCollections() {
            Annotation[] annotations = new Annotation[]{
                    new ClassWriter.Annotation("Data"),
                    new ClassWriter.Annotation("Setter"),
                    new ClassWriter.Annotation("Getter"),
                    new ClassWriter.Annotation("Builder"),
                    new ClassWriter.Annotation("AllArgsConstructor"),
                    new ClassWriter.Annotation("NoArgsConstructor")
            };
            return annotations;
        }
    }

    @Getter
    public static class Field extends ClassComponents {
        private String fieldType;
        private String fieldName;
        private Object defaultValue;
        private Boolean isPublic;
        private List<Annotation> annotations;

        public Field(String fieldType, String fieldName, Boolean isPublic, List<Annotation> annotations, Object defaultValue) {
            this.fieldType = fieldType;
            this.fieldName = fieldName;
            this.isPublic = isPublic;
            this.annotations = annotations;
            this.defaultValue = defaultValue;
        }
    }

    @Getter
    public static class Method extends ClassComponents {
        private String methodName;
        private Boolean isPublic;
        private List<Field> params;
        private List<Annotation> annotations;
        private DataHolder response;
        private String content;

        public Method(String methodName, Boolean isPublic, DataHolder response, List<Annotation> annotations, String content, List<Field> params) {
            this.methodName = methodName;
            this.response = response;
            this.isPublic = isPublic;
            this.annotations = annotations;
            this.content = content;
            this.params = params;
        }
    }
}
