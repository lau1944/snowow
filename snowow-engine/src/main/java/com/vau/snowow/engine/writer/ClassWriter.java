package com.vau.snowow.engine.writer;

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
                writeField((Field) component);
            } else if (component instanceof Method) {
                // Write methods into files
                writeMethod((Method) component);
            }
        }

        fileWriter.write("}");
        fileWriter.close();
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
        fileWriter.write(method.getReturnType() + " ");
        fileWriter.write(method.getMethodName());
        fileWriter.write("() ");
        fileWriter.write("{ \n");
        //fileWriter.write("return \"hello world\";");
        fileWriter.write(method.getContent());
        fileWriter.write("\n" + "}");
    }

    private void writeField(Field field) throws IOException {
        fileWriter.write("\t");
        List<Annotation> annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            fileWriter.write(annotation.toString());
        }
        fileWriter.write(field.getIsPublic() ? "public " : "private ");
        fileWriter.write(field.getFieldType() + " " + field.getFieldName());
        // Check if default value exists, if exists, add to string (Default value should is bound with annotation @Builder.Default
        if (Objects.nonNull(field.getDefaultValue())) {
            fileWriter.write(" = " + field.getDefaultValue());
        }
        fileWriter.write(";");
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
                    if ("value".equals(entry.getKey()) && value instanceof String) {
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
        private Field[] params;
        private List<Annotation> annotations;
        private String returnType;
        private String content;

        public Method(String methodName, Boolean isPublic, String content) {
            this.methodName = methodName;
            this.returnType = "void";
            this.isPublic = isPublic;
            this.params = new Field[]{};
            this.annotations = new ArrayList<>();
            this.content = content;
        }

        public Method(String methodName, Boolean isPublic, Field[] params, String content) {
            this.methodName = methodName;
            this.returnType = "void";
            this.isPublic = isPublic;
            this.params = params;
            this.annotations = new ArrayList<>();
            this.content = content;
        }

        public Method(String methodName, Boolean isPublic, Field[] params, List<Annotation> annotations, String content) {
            this.methodName = methodName;
            this.returnType = "void";
            this.isPublic = isPublic;
            this.params = params;
            this.annotations = annotations;
            this.content = content;
        }

        public Method(String methodName, Boolean isPublic, String returnType, List<Annotation> annotations, String content) {
            this.methodName = methodName;
            this.returnType = returnType;
            this.isPublic = isPublic;
            this.params = new Field[]{};
            this.annotations = annotations;
            this.content = content;
        }

        public Method(String methodName, Boolean isPublic, String returnType, Field[] params, List<Annotation> annotations, String content) {
            this.methodName = methodName;
            this.returnType = returnType;
            this.isPublic = isPublic;
            this.params = params;
            this.annotations = annotations;
            this.content = content;
        }
    }
}
