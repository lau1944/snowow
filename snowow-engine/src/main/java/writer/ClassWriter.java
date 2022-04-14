package writer;

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
    private Annotation[] annotations;
    private Boolean isPublic;
    private FileWriter fileWriter;
    private List<ClassComponents> components = new ArrayList<>();

    public ClassWriter(File file, String className, Annotation[] annotations, Boolean isPublic) {
        this.file = file;
        this.annotations = annotations;
        this.className = className;
        this.isPublic = isPublic;
    }

    public ClassWriter(File file, String className, Boolean isPublic) {
        this.file = file;
        this.annotations = new Annotation[]{};
        this.className = className;
        this.isPublic = isPublic;
    }

    public ClassWriter(String className, Boolean isPublic, Annotation[] annotations, FileWriter fileWriter) {
        this.className = className;
        this.annotations = annotations;
        this.isPublic = isPublic;
        this.fileWriter = fileWriter;
    }

    /**
     * This method is used to write the class files
     */
    public void wrap(Consumer<List<ClassComponents>> componentsAdd) throws IOException {
        if (Objects.isNull(fileWriter)) {
            fileWriter = new FileWriter(file, true);
        }

        for (Annotation annotation : annotations) {
            fileWriter.write(annotation.toString());
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

    private void writeMethod(Method method) throws IOException {
        fileWriter.write("\t");
        for (Annotation annotation : method.getAnnotations()) {
            fileWriter.write(annotation.toString());
        }
        fileWriter.write(method.getIsPublic() ? "public " : "private ");
        fileWriter.write(method.getReturnType() + " ");
        fileWriter.write(method.getMethodName() + " { \n");
        fileWriter.write(method.getContent());
        fileWriter.write("\n" + "}");
    }

    private void writeField(Field field) throws IOException {
        fileWriter.write("\t");
        for (Annotation annotation : field.getAnnotations()) {
            fileWriter.write(annotation.toString());
        }
        fileWriter.write(field.getIsPublic() ? "public " : "private ");
        fileWriter.write(field.getFieldType() + " " + field.getFieldName() + ";");
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
                Set<Map.Entry<String, Object>> entries = properties.entrySet();
                for (final Map.Entry<String, Object> entry : entries) {
                    builder.append(entry.getKey());
                    builder.append("=");
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        builder.append("\"");
                        builder.append(value);
                        builder.append("\"");
                        continue;
                    }
                    builder.append(entry.getValue());
                }
            }
            builder.append(")");
            builder.append(" ");
            return builder.toString();
        }
    }

    @Getter
    public static class Field extends ClassComponents {
        private String fieldType;
        private String fieldName;
        private Boolean isPublic;
        private Annotation[] annotations;

        public Field(String fieldType, String fieldName, Boolean isPublic, Annotation[] annotations) {
            this.fieldType = fieldType;
            this.fieldName = fieldName;
            this.isPublic = isPublic;
            this.annotations = annotations;
        }
    }

    @Getter
    public static class Method extends ClassComponents {
        private String methodName;
        private Boolean isPublic;
        private Field[] params;
        private Annotation[] annotations;
        private String returnType;
        private String content;

        public Method(String methodName, Boolean isPublic, String content) {
            this.methodName = methodName;
            this.returnType = "void";
            this.isPublic = isPublic;
            this.params = new Field[]{};
            this.annotations = new Annotation[]{};
            this.content = content;
        }

        public Method(String methodName, Boolean isPublic, Field[] params, String content) {
            this.methodName = methodName;
            this.returnType = "void";
            this.isPublic = isPublic;
            this.params = params;
            this.annotations = new Annotation[]{};
            this.content = content;
        }

        public Method(String methodName, Boolean isPublic, Field[] params, Annotation[] annotations, String content) {
            this.methodName = methodName;
            this.returnType = "void";
            this.isPublic = isPublic;
            this.params = params;
            this.annotations = annotations;
            this.content = content;
        }

        public Method(String methodName, Boolean isPublic, String returnType, Annotation[] annotations, String content) {
            this.methodName = methodName;
            this.returnType = returnType;
            this.isPublic = isPublic;
            this.params = new Field[]{};
            this.annotations = annotations;
            this.content = content;
        }

        public Method(String methodName, Boolean isPublic, String returnType, Field[] params, Annotation[] annotations, String content) {
            this.methodName = methodName;
            this.returnType = returnType;
            this.isPublic = isPublic;
            this.params = params;
            this.annotations = annotations;
            this.content = content;
        }
    }

}
