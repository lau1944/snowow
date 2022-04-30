package com.vau.snowow.engine.writer;

import com.vau.snowow.engine.core.SnowContext;
import com.vau.snowow.engine.models.Controller;
import com.vau.snowow.engine.models.Path;
import com.vau.snowow.engine.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

/**
 * Writer class for writing controller classes
 *
 * @author liuquan
 */
@Slf4j
public class ControllerWriter extends BaseWriter<List<Controller>> {

    private final String ACTION_KEY = "type";
    private final String REDIRECT_ACTION = "redirect";
    private ClassWriter classWriter;

    @Override
    public int write(List<Controller> controllers, String targetPath, String packageName) {
        if (controllers.isEmpty() || !StringUtils.hasLength(targetPath)) {
            return -1;
        }

        int writeResult = 1;

        File controllersDir = new File(targetPath + "/controllers");
        if (!controllersDir.exists()) {
            controllersDir.mkdirs();
        }

        lock.lock();
        try {
            for (final Controller controller : controllers) {
                int version = controller.getVersion();
                String className = controller.getName() + "V" + version + "Controller";
                File controllerFile = new File(controllersDir.getPath() + "/" + className + ".java");
                if (controllerFile.createNewFile()) {
                    log.info("{} is created", controllerFile.getAbsolutePath());
                }

                Map<String, Object> requestMap = new HashMap(5);
                requestMap.put("value", controller.getPath());
                ClassWriter.Annotation[] classAnnotation = new ClassWriter.Annotation[]{
                        new ClassWriter.Annotation("RestController"),
                        new ClassWriter.Annotation("RequestMapping", requestMap)
                };
                classWriter = new ClassWriter(controllerFile, className, Arrays.asList(classAnnotation), true);
                List<String> dependencies = new ArrayList<>(List.of(
                        "org.springframework.beans.factory.annotation.Autowired",
                        "org.springframework.web.bind.annotation.*",
                        "org.springframework.http.MediaType",
                        "java.util.Map",
                        "java.util.HashMap",
                        "com.vau.app.service.*"
                ));

                File modelDir = new File(targetPath + "/models");
                if (modelDir.exists()) {
                    dependencies.add(packageName + ".models.*");
                }

                classWriter.wrap(packageName + ".controllers", classComponents -> {
                    for (final Path path : controller.getPaths()) {
                        Map<String, Object> methodMap = new HashMap(8);
                        // push request path
                        methodMap.put("value", path.getPath());
                        // push response type
                        methodMap.put("produces", path.getResponse().getType());
                        ClassWriter.Annotation[] methodAnnotation = new ClassWriter.Annotation[]{
                                new ClassWriter.Annotation(mapMethodIntoAnnotation(path.getMethod()), methodMap)
                        };

                        // Add Headers
                        ClassWriter.Field headers = buildRequestHeaders();
                        // Add request params
                        ClassWriter.Field requestParams = buildRequestParams();
                        List<ClassWriter.Field> methodParams = Arrays.asList(headers, requestParams);

                        ClassWriter.ClassComponents components = new ClassWriter.Method(
                                path.getName(),
                                true,
                                path.getResponse().getData(),
                                Arrays.asList(methodAnnotation),
                                parseAction(path.getAction()),
                                methodParams
                        );

                        // Bound web service client object
                        ClassWriter.Annotation[] fieldAnnotations = new ClassWriter.Annotation[]{new ClassWriter.Annotation("Autowired")};
                        ClassWriter.Field field = new ClassWriter.Field("IRemoteService", "service",
                                false, Arrays.asList(fieldAnnotations), null);

                        classComponents.add(field);
                        classComponents.add(components);
                    }
                }, dependencies);
                // close writer
                classWriter.close();
                // Add controller to container
                SnowContext.addController(className, controller);
            }
        } catch (Exception e) {
            log.error(e.toString());
            writeResult = -1;
        } finally {
            lock.unlock();
        }

        return writeResult;
    }

    /**
     * Parse action
     */
    private String parseAction(Map<String, Object> action) {
        if (!action.containsKey(ACTION_KEY)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        String type = (String) action.get(ACTION_KEY);
        if (type.equals(REDIRECT_ACTION)) {
            builder.append("return " + "service.GET(");
            builder.append("\"" + StringUtil.extractFieldValue(action.get("url").toString()) + "\",");
            ObjectWriter objectWriter = new ObjectWriter();
            builder.append(objectWriter.constructHashMap((Map<String, Object>) action.get("params")));
            builder.append(",");
            builder.append(objectWriter.constructHashMap((Map<String, Object>) action.get("headers")));
            builder.append(");");
        }
        return builder.toString();
    }

    /**
     * Build request params
     */
    ClassWriter.Field buildRequestParams() {
        return new ClassWriter.Field(
                "Map<String, Object>",
                "params",
                false,
                Arrays.asList(new ClassWriter.Annotation("RequestParam")),
                null
        );
    }

    /**
     * Build request headers field
     */
    ClassWriter.Field buildRequestHeaders() {
        return new ClassWriter.Field(
                "Map<String, Object>",
                "headers",
                false,
                Arrays.asList(new ClassWriter.Annotation("RequestHeader")),
                null
        );
    }

    /**
     * Convert method GET, POST into GetMapping, PostMapping
     *
     * @return
     */
    private String mapMethodIntoAnnotation(String method) {
        switch (method) {
            case "GET":
                return "GetMapping";
            case "POST":
                return "PostMapping";
            case "PUT":
                return "PutMapping";
            case "DELETE":
                return "DeleteMapping";
            default:
                throw new IllegalArgumentException("Please pass a valid API method (ex. GET, POST");
        }
    }

    public static BaseWriter newWriter() {
        return new ControllerWriter();
    }
}
