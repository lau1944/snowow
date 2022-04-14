package com.vau.snowow.engine.writer;

import com.vau.snowow.engine.models.Constant;
import com.vau.snowow.engine.models.Controller;
import com.vau.snowow.engine.models.Path;
import com.vau.snowow.engine.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Writer class for writing controller classes
 *
 * @author liuquan
 */
@Slf4j
public class ControllerWriter extends BaseWriter<List<Controller>> {

    @Override
    public int write(List<Controller> controllers, String packageName) throws IOException {
        if (controllers.isEmpty() || !StringUtils.hasLength(packageName)) {
            return 0;
        }

        lock.lock();
        // Format package path into file path
        String targetPath = packageName.replace(".", "/");

        File controllersDir = new File(FileUtil.getApplicationPath() + "/java/" + targetPath + "/controllers");
        log.info(controllersDir.getPath());

        if (controllersDir.mkdirs()) {
            log.info("Controller directory was not found, controller directory is created, the path is {}", controllersDir.getPath());
        }

        try {
            for (final Controller controller : controllers) {
                String className = controller.getName() + "Controller";
                File controllerFile = new File(controllersDir.getPath() + "/" + className + ".java");
                if (controllerFile.createNewFile()) {
                    log.info("{} is created", controllerFile.getAbsolutePath());
                }

                Map<String, Object> requestMap = new HashMap(1);
                requestMap.put("value", controller.getPath());
                ClassWriter.Annotation[] classAnnotation = new ClassWriter.Annotation[]{
                        new ClassWriter.Annotation("RestController"),
                        new ClassWriter.Annotation("RequestMapping", requestMap)
                };
                ClassWriter classWriter = new ClassWriter(controllerFile, className, classAnnotation, true);
                classWriter.wrap(packageName, classComponents -> {
                    for (final Path path : controller.getPaths()) {
                        Map<String, Object> methodMap = new HashMap(8);
                        // push request path
                        methodMap.put("value", path.getPath());
                        // push response type
                        if (path.getResponse().getType().equals(Constant.JSON_FORM)) {
                            methodMap.put("produces", "MediaType.APPLICATION_JSON_VALUE");
                        }
                        ClassWriter.Annotation[] methodAnnotation = new ClassWriter.Annotation[]{
                                new ClassWriter.Annotation(mapMethodIntoAnnotation(path.getMethod()), methodMap)
                        };
                        ClassWriter.ClassComponents components = new ClassWriter.Method(
                                path.getName(),
                                true,
                                path.getResponse().getDataType(),
                                methodAnnotation,
                                ""
                        );
                        classComponents.add(components);
                    }
                });
            }
        } finally {
            // close writer
            close();
            lock.unlock();
        }

        return 0;
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
}
